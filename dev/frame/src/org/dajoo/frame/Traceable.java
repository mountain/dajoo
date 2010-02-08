package org.dajoo.frame;

import org.dajoo.httpd.HttpRequest;

public interface Traceable {

    public Trace getTrace(HttpRequest req);

}
