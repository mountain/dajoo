package org.dajoo.frame;

public class Trace {

    private String title;
    private String path;

    public Trace(String t, String p) {
        if(t==null || p==null)
            throw new IllegalArgumentException();
        title = t;
        path = p;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof Trace)) return false;
        Trace t = (Trace)obj;
        return title.equals(t.title) && path.equals(t.path);
    }

    public int hashCode() {
        return (title + path).hashCode();
    }
}
