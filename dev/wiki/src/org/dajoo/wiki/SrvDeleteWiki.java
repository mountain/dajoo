/**
 * (X)SrvDeleteWiki.java
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

import java.util.Map;

import org.dajoo.frame.FrameServlet;
import org.dajoo.frame.ToolbarManager;
import org.dajoo.frame.TraceManager;
import org.dajoo.httpd.HttpRequest;

public class SrvDeleteWiki extends FrameServlet {

    public SrvDeleteWiki(freemarker.template.Configuration tc, ToolbarManager tlbm, TraceManager trcm) {
        super(tc, tlbm, trcm);
    }

    protected String getTemplate() {
        return "";
    }

    protected String getTitle(HttpRequest req) {
        return "Wiki:";
    }

    protected String getPath(HttpRequest req) {
        return "";
    }

    protected void setBody(HttpRequest req, Map<String, Object> root) {
        //NOOP
    }
}
