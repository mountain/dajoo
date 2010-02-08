/**
 * (X)SrvSaveWiki.java
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
package org.dajoo.wiki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

import org.dajoo.frame.FrameServlet;
import org.dajoo.frame.ToolbarManager;
import org.dajoo.frame.TraceManager;
import org.dajoo.httpd.HttpRequest;
import org.dajoo.kernel.DajooConfiguration;
import org.dajoo.render.WikiNameManager;
import org.dajoo.render.WikiRender;

public class SrvSaveWiki extends FrameServlet {

    private static Charset utf8 = Charset.forName("UTF-8");

    private WikiRender wikiRender;
    private WikiNameManager wikiNameMgr;

    public SrvSaveWiki(freemarker.template.Configuration tc, ToolbarManager tlbm, TraceManager trcm, WikiRender p, WikiNameManager w) {
        super(tc, tlbm, trcm);
        wikiRender = p;
        wikiNameMgr = w;
    }

    protected String getTemplate() {
        return "wiki_show.ftl";
    }

    protected String getTitle(HttpRequest req) {
        return getTitle(ommitParam(req.getPath()));
    }

    protected String getPath(HttpRequest req) {
        return ommitParam(req.getPath());
    }

    protected void setBody(HttpRequest req, Map<String, Object> root) {
        String title = getTitle(ommitParam(req.getPath()));
        String text = wikiRender.parse(req.getAttribute("wikitext"));
        root.put("text", text);
        saveText(title, req.getAttribute("wikitext"));
        wikiNameMgr.addName(title);
    }

    private String ommitParam(String path) {
        int p = path.indexOf("?");
        return p<0?path:path.substring(0, p);
    }

    private String getTitle(String path) {
        int len = path.length();
        assert(len>6);
        String title = path.substring(6, len);
        try {
            title = URLDecoder.decode(title, "UTF-8");
        } catch(UnsupportedEncodingException e) {}
        if("".equals(title)) title = "start";
        title = wikiNameMgr.normalizeName(title);
        return title;
    }

    private void saveText(String title, String content) {
        title = wikiNameMgr.normalizeName(title);
        File file = new File(DajooConfiguration.getConfiguration("wiki.dir"),
                title.replace(':',
                       File.separatorChar) + ".txt");
        try {
            if( !file.exists() ) file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, utf8);
            osw.append(content);
            osw.close();
            fos.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
