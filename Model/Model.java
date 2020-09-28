package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import Controller.Controller;

// import java.io.File;
// import java.io.FileWriter;
// import java.io.InputStream;

// import Controller.Controller;

public class Model {
    private Controller controller;
    private String workspaceName;
    private FileSystemCollection<Request> workspace;

    public Model(Controller controller, String workspaceName) {
        this.controller = controller;
        this.workspaceName = workspaceName;
        // loadWorkspace();
    }

    /**
     * load all saved requests from file
     */
    @SuppressWarnings("unchecked")
    public void loadWorkspace(JTree tree) {
        new SwingWorker<FileSystemCollection<Request>, String>() {
            @Override
            protected FileSystemCollection<Request> doInBackground() throws Exception {
                // addressing savee file(!should be defined in constants!)
                File file = new File("./Data/\"" + workspaceName + "\"/REQUESTS.FS");
                if (file.exists()) {
                    // if addressed file exists then read and load ist
                    Object obj;
                    // reading file
                    try {
                        FileInputStream fileStream = new FileInputStream(file);
                        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                        obj = objectStream.readObject();
                        objectStream.close();
                        fileStream.close();
                        // validating file as request collections then loading to ram(!might not be
                        // proper!)
                        if ((obj instanceof FileSystemCollection<?>)
                                && ((FileSystemCollection<?>) obj).genericInstanceClass == Request.class) {
                            return (FileSystemCollection<Request>) obj;
                        } else {
                            System.out.println("Saved request might be corrupted");
                            System.out.println(((FileSystemCollection<?>) obj).genericInstanceClass);
                            System.out.println(Request.class);
                            return new FileSystemCollection<Request>("Requests", Request.class);
                        }
                    } catch (Exception e) {
                        System.out.println("could not load the workspace request list");
                        return new FileSystemCollection<Request>("Requests", Request.class);
                    }

                } else
                    System.out.println("workspace going to be created");
                JOptionPane.showMessageDialog(null, "loading new workspace");
                // use an empty file system collection for storing data
                return new FileSystemCollection<Request>("Requests", Request.class);
            }

            protected void done() {
                JOptionPane.showMessageDialog(null, "successfully loaded workspace");
                try {
                    // creating obj
                    workspace = get();
                    // loading obj on jtree
                    workspace.loadCollection(tree);
                    TreeNode root = (TreeNode) tree.getModel().getRoot();
                    workspace.addFolderTreeNode((DefaultTreeModel) tree.getModel(), "folderName");
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println(e);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.execute();
        // return workspace;
    }

    /**
     * saving changed version of request collection to file (!when actually an
     * instance of many instances of class is finished - need thread memory
     * management i think!) !!critical!!
     */
    public void saveWorkspace() {
        // addressing savee file(!should be defined in constants!)
        File file = new File("./Data/\"" + workspaceName + "\"/REQUESTS.FS");
        try {
            new File("./Data/\"" + workspaceName).mkdirs();
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(workspace);
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public FileSystemCollection<Request> getWorkspace() {
        return workspace;
    }

}