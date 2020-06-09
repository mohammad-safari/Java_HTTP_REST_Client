package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;

public class JURL {
    private static FileSystemCollection<Request> requests; // collection of requests that must be static
    private static Folder<Request> root; // root folder of collection

    /**
     * load all saved requests from file
     */
    @SuppressWarnings("unchecked")
    public static void loadRequests() {
        // addressing savee file(!should be defined in constants!)
        File file = new File("./Data/REQUESTS.FS");
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
                    requests = (FileSystemCollection<Request>) obj;
                } else {
                    System.out.println("Saved request might be corrupted");
                    System.out.println(((FileSystemCollection<?>) obj).genericInstanceClass);
                    System.out.println(Request.class);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else
            // use an empty file system collection for storing data
            requests = new FileSystemCollection<Request>("Requests", Request.class);
    }

    /**
     * saving changed version of request collection to file (!when actually an
     * instance of many instances of class is finished - need thread memory
     * management i think!) !!critical!!
     */
    public static void saveRequests() {
        // addressing savee file(!should be defined in constants!)
        File file = new File("./Data/REQUESTS.FS");
        try {
            new File("./Data").mkdir();
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(requests);
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // loading set of requests
        loadRequests();
        // root folder of request collection
        root = requests.getRoot();
        if (args.length == 0)
            return;
        // working with saved collection of requests
        switch (args[0]) {
            case "create":
                if (args.length > 1) {
                    try {
                        requests.createFolder(args[1]);
                        saveRequests();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else
                    System.out.println("folder argument is missing!");
                // break;
                return;
            case "fire":
                // implement addressing in fscol and name validation
                // break;
                return;
            case "list":
                Folder<Request> folder;
                try {
                    folder = requests.openFolder(args.length > 1 ? args[1] : "");
                    ArrayList<Node> childs = folder.getChilds();
                    if (childs != null)
                        for (Node child : childs)
                            if (child.isFolder)
                                System.out.println(child.name + " (Folder)");
                            else
                                System.out.println(child.name);
                } catch (Exception e) {
                    // System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                // break;
                return;
        }
        // validating url
        Request current;
        try {
            current = new Request(args[0]);
            requests.getRoot().addFolder("name").addFolder("name").addFile("name", current);

        } catch (MalformedURLException e) {
            System.out.println("malformed url, please enter url in correct format");
            return;
            // Thread.currentThread().stop();
        } catch (IOException e) {
            System.out.println("enter proper url!");
            return;
            // Thread.currentThread().stop();
        }

        // getting other options
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-' && arg.charAt(1) == '-') {
                switch (arg.substring(2)) {
                    case "--header":
                    case "-H":
                        break;
                    case "--method":
                    case "-M":
                        // if argument value is given correctly
                        if (i + 1 < args.length)
                            try {
                                current.setMethod(args[++i]);
                            } catch (ProtocolException e) {
                                System.out.println("unexpected method argument entered!");
                                return;
                            }
                        else {
                            System.out.println("method argument is missing!");
                            return;
                        }
                        break;
                    // message body
                    case "--json":
                        break;
                    case "--data":
                    case "-d":
                        break;
                    case "--upload":
                    case "-U":
                        break;
                    //
                    case "--save":
                    case "-S":
                        break;
                    case "--output":
                    case "-O":
                        break;
                    case "--help":
                    case "-h":
                        break;
                    // showing header Responses
                    case "-i":
                        break;
                    // follow redirect
                    case "-f":
                        break;

                }

            } else if (true) {

            } else {
                System.out.println("unexpected argument/parameter " + arg + " entered!");
                return;
                // Thread.currentThread().stop();
            }
        }
        // try {
        // System.out.println(current.getConnection().getResponseMessage());
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        saveRequests();
    }
}