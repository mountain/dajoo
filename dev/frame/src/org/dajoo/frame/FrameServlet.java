/**
 * (X)FrameServlet.java
 *
 * Copyright (C) 2006-2009 Mingli Yuan
 * http://www.dajoo.org/
 * http://dajoo.sourceforge.net/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * http://www.gnu.org/copyleft/gpl.html
 *
 */

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

public abstract class FrameServlet implements Servlet {

    protected freemarker.template.Configuration templateCfg;
    protected ToolbarManager tlbMgr;
    protected TraceManager traceMgr;

    public FrameServlet(freemarker.template.Configuration tc, ToolbarManager tlbm, TraceManager trcm) {
        templateCfg = tc;
        tlbMgr = tlbm;
        traceMgr = trcm;
    }

    public void service(HttpRequest req, HttpResponse resp) throws ServletException {
        resp.setContent(getContent(req));
        resp.sendNormally();
    }

    public Content getContent(HttpRequest req) {
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

}
