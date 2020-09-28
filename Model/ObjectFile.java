package Model;

public class ObjectFile<T> extends Node {
    /**
     *
     */
    private static final long serialVersionUID = -6772728741937095961L;
    private T object;

    public ObjectFile(String name, Node parent) {
        super(name, parent, false);
    }

    public ObjectFile(String name, Node parent, T object) {
        super(name, parent, false);
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    @SuppressWarnings("unchecked")
    public Folder<T> getParent() {
        return (Folder<T>) parent;
    }
}