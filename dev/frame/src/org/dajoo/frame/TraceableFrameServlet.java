package org.dajoo.frame;

import java.io.CharArrayWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.dajoo.httpd.Content;
import org.dajoo.httpd.HttpRequest;
import org.dajoo.httpd.HttpResponse;
import org.dajoo.httpd.Servlet;
import org.dajoo.httpd.ServletException;
import org.dajoo.httpd.StringContent;

import freemarker.template.Template;

public abstract class TraceableFrameServlet implements Servlet, Traceable {

    protected freemarker.template.Configuration templateCfg;
    protected ToolbarManager tlbMgr;
    protected TraceManager traceMgr;

    public TraceableFrameServlet(freemarker.template.Configuration tc, ToolbarManager tlbm, TraceManager trcm) {
        templateCfg = tc;
        tlbMgr = tlbm;
        traceMgr = trcm;
    }

    public void service(HttpRequest req, HttpResponse resp) throws ServletException {
        traceMgr.addTrace(getTrace(req));
        resp.setContent(getContent(req));
        resp.sendNormally();
    }

    protected Content getContent(HttpRequest req) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("title", getTitle(req));
        root.put("path", getPath(req));
        root.put("toolbar", tlbMgr.getButtonsForPath(req.getPath()));
        root.put("traces", traceMgr.getTraces());

        setBody(req, root);

        Writer out = new CharArrayWriter(500);
        try {
            Template template = templateCfg.getTemplate(getTemplate(), "utf-8");
            template.process(root, out);
            out.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return new StringContent(out.toString(), "text/html");
    }

    protected abstract String getTemplate();
    protected abstract String getTitle(HttpRequest req);
    protected abstract String getPath(HttpRequest req);
    protected abstract void setBody(HttpRequest req, Map<String, Object> root);

    public Trace getTrace(HttpRequest req) {
        return new Trace(getTitle(req), req.getPath());
    }

}
