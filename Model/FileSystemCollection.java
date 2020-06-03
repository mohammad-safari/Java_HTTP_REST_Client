package Model;

import java.util.ArrayList;

/**
 * a generic class for grouping objects in a file-system-like collection
 * 
 * @author M.Safari
 * @version 1399.03.13
 */
public class FileSystemCollection<T> {

    private Folder<T> root; // root of file-system-like collection

    /**
     * initializing a root folder
     * 
     * @param name
     */
    public FileSystemCollection(String name) {
        root = new Folder<T>(name);
    }

    /**
     * 
     * @return the root folder
     */
    public Folder<T> getRoot() {
        return root;
    }
}

/**
 * desiging structure like a tree with basic (and also intentionally not
 * complete due to difference of file node and folder node) features
 */
abstract class Node {
    protected String name;
    protected Node parent;
    protected final boolean isFolder;

    /**
     * used only for creating root nodes
     */
    Node(String name) {
        this.name = name;
        this.parent = this;
        this.isFolder = true;
    }

    /**
     * used for non-root nodes
     * 
     * @param parent
     * @param isEndNode
     */
    public Node(String name, Node parent, boolean isFolder) {
        this.name = name;
        this.parent = parent;
        this.isFolder = isFolder;
    }
}

class Folder<T> extends Node {
    private ArrayList<Node> childs;

    /**
     * used only creating root folder
     */
    Folder(String name) {
        super(name);
    }

    public Folder(String name, Node parent) {
        super(name, parent, true);
    }

    public Folder<T> addFolder(String name) {
        if (childs == null)
            childs = new ArrayList<Node>();
        for (Node child : childs) {
            if (child.name == name && child.isFolder == true)
                return (Folder<T>) child;
        }
        Folder<T> folder = new Folder<T>(name, this);
        childs.add(folder);
        return folder;
    }

    public Folder<T> addFile(String name, T object) {
        if (childs == null)
            childs = new ArrayList<Node>();
        for (Node child : childs) {
            if (child.name == name && child.isFolder == false) {
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
            if (node.name == name && node.isFolder == isFolder)
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

    public Node getChild(String name, boolean isFolder) {
        for (Node node : childs)
            if (node.name == name && node.isFolder == isFolder)
                return node;
        return null;
    }

    public Folder<T> getParent() {
        return (Folder<T>) parent;
    }
}

class File<T> extends Node {
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
