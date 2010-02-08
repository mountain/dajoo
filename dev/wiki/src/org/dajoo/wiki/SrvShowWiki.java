/**
 * (X)SrvShowWiki.java
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

import org.dajoo.frame.ToolbarManager;
import org.dajoo.frame.TraceManager;
import org.dajoo.frame.TraceableFrameServlet;
import org.dajoo.httpd.HttpRequest;
import org.dajoo.kernel.DajooConfiguration;
import org.dajoo.render.WikiNameManager;
import org.dajoo.render.WikiRender;

public class SrvShowWiki extends TraceableFrameServlet {

    private static Charset utf8 = Charset.forName("UTF-8");

    private WikiRender wikiRender;
    private WikiNameManager wikiNameMgr;

    public SrvShowWiki(freemarker.template.Configuration tc, ToolbarManager tlbm, TraceManager trcm, WikiRender p, WikiNameManager w) {
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
        String shortTitle = getTitle(ommitParam(req.getPath()));
        String text = wikiRender.parse(getWikiText(shortTitle));
        root.put("text", text);
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

    private String getWikiText(String title) {
        File file = new File(DajooConfiguration.getConfiguration("wiki.dir"), title.replace(':', File.separatorChar)+".txt");
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis, utf8);
            int i = reader.read();
            while(i!=-1) {
                buffer.append((char)i);
                i = reader.read();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

}
