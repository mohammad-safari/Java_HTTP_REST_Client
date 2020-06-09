package Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * a generic class for grouping objects in a file-system-like collection
 * 
 * @author M.Safari
 * @version 1399.03.13
 */
public class FileSystemCollection<T> implements Serializable {

    /**
     *
     */
    public final Class<T> genericInstanceClass;
    private static final long serialVersionUID = -705057808584791795L;
    private Folder<T> root; // root of file-system-like collection

    /**
     * initializing a root folder
     * 
     * @param name
     */
    public FileSystemCollection(String name, Class<T> t) {
        root = new Folder<T>(name);
        genericInstanceClass = t;

    }

    /**
     * 
     * @return the root folder
     */
    public Folder<T> getRoot() {
        return root;
    }

    /**
     * separating the path to folders
     * 
     * @param fullPath in (/)xxx/yyy/... format or \ instead but the tailing zero
     *                 length strings are ignored (e.p. /.../aaa/// three last null
     *                 strings are ignored)
     * @return
     */
    public String[] toDirectories(String fullPath) {
        // seprating according to / if exists else according to \
        char separator = /* fullPath.contains("\\") ? '\\' : */ '/';
        // removes first seprator to remove the first null string directory
        return (fullPath.charAt(0) == separator) ? fullPath.substring(1).split("" + separator)
                : fullPath.split("" + separator);
    }

    /**
     * 
     * @param fullPath
     * @param child
     * @param isFolder
     * @return the parent folder of given directory
     * @throws Exception
     * @see {@link Node#parent}
     */
    @SuppressWarnings("unchecked")
    public Folder<T> openParentFolder(String[] directory) throws Exception {
        Folder<T> folder = root;
        // if parent is root folder
        if (directory.length == 1)
            return root;
        // else if parent folder is not root
        for (int i = 0; i < directory.length - 1; i++)

            if ((folder = (Folder<T>) folder.getChild(directory[i], true)) == null)
                throw new Exception("Folder " + directory[i] + " does not Exist!");

        return folder;
    }

    /**
     * 
     * @param path
     * @return the folder in the path if exists
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Folder<T> openFolder(String path) throws Exception {
        // if target has no parents
        if (path == "/" || path == "" || path == "\\")
            return root;
        // else if target has at least one parent(root)
        String[] directory = toDirectories(path);
        Folder<T> folder = openParentFolder(directory);
        folder = (Folder<T>) folder.getChild(directory[directory.length - 1], true);
        return folder;
    }

    /**
     * 
     * @param path
     * @return the file in the path if exists
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public File<T> openFile(String path) throws Exception {
        String[] directory = toDirectories(path);
        Folder<T> folder = openParentFolder(directory);
        File<T> file = ((folder != null) ? ((File<T>) folder.getChild(directory[directory.length - 1], false)) : null);
        if (file == null)
            throw new Exception("File does not Exist");
        return file;
    }

    /**
     * 
     * @param path to new folder
     * @return the new folder or null if parent does not exist
     * @throws Exception
     */
    public Folder<T> createFolder(String path) throws Exception {
        String[] directory = toDirectories(path);
        Folder<T> folder = openParentFolder(directory);
        return folder.addFolder(directory[directory.length - 1]);
    }

    /**
     * 
     * @param path to new file
     * @return the parent folder or null if parent does not exists
     * @throws Exception
     */
    public Folder<T> createFile(String path, T request) throws Exception {
        String[] directory = toDirectories(path);
        Folder<T> folder = openParentFolder(directory);
        return folder.addFile(directory[directory.length - 1], request);
    }

}

/**
 * desiging structure like a tree by implementing a childless node
 * 
 * @param isFolder reassures that its should remain childless or not
 */
abstract class Node implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5829976214457658225L;
    protected String name;
    protected Node parent;
    protected final boolean isFolder;

    /**
     * used only for creating root nodes
     * 
     * @param name
     */
    Node(String name) {
        this.name = name;
        this.parent = this;
        this.isFolder = true;
    }

    /**
     * used for non-root nodes
     * 
     * @param name
     * @param parent
     * @param isFolder
     */
    public Node(String name, Node parent, boolean isFolder) {
        this.name = name;
        this.parent = parent;
        this.isFolder = isFolder;
    }
}

class Folder<T> extends Node {
    /**
     *
     */
    private static final long serialVersionUID = 6611689744693871854L;
    private ArrayList<Node> childs = new ArrayList<Node>();

    /**
     * used only creating root folder
     * 
     * @param name
     */
    Folder(String name) {
        super(name);
    }

    public Folder(String name, Node parent) {
        super(name, parent, true);
    }

    @SuppressWarnings("unchecked")
    public Folder<T> addFolder(String name) {
        for (Node child : childs) {
            if (child.name.equals(name) && child.isFolder == true)
                return (Folder<T>) child;
        }
        Folder<T> folder = new Folder<T>(name, this);
        childs.add(folder);
        return folder;
    }

    @SuppressWarnings("unchecked")
    public Folder<T> addFile(String name, T object) {
        for (Node child : childs) {
            if (child.name.equals(name) && child.isFolder == false) {
                ((File<T>) child).setObject(object);
                return this;
            }
        }
        childs.add(new File<T>(name, this, object));
        return this;
    }

    public void removeFile(File<T> node) {
        childs.remove(node);
    }

    public void removeFolder(Folder<T> node) {
        childs.remove(node);
    }

    public void removeChild(String name, boolean isFolder) {
        for (Node node : childs) {
            if (node.name.equals(name) && node.isFolder == isFolder)
                childs.remove(node);
        }
    }

    public ArrayList<Node> getChilds() {
        return childs;
    }

    public ArrayList<String> getChildNames() {
        return new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = -6199362412683596445L;

            {
                for (Node node : childs)
                    add(node.name);
            }
        };
    }

    public Node getChild(String name, boolean isFolder) throws Exception {
        for (Node node : childs)
            if (node.name.equals(name) && node.isFolder == isFolder)
                return node;
        throw new Exception((isFolder ? "Folder " : "File ") + name + " does not Exist");
    }

    @SuppressWarnings("unchecked")
    public Folder<T> getParent() {
        return (Folder<T>) parent;
    }
}

class File<T> extends Node {
    /**
     *
     */
    private static final long serialVersionUID = -6772728741937095961L;
    private T object;

    public File(String name, Node parent) {
        super(name, parent, false);
    }

    public File(String name, Node parent, T object) {
        super(name, parent, false);
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
