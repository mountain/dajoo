/**
 * (X)ServletManagerImpl.java
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
package org.dajoo.httpd;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class ServletManagerImpl implements ServletManager {

	private static Logger logger = Logger.getLogger(ServletManagerImpl.class);

	private HashMap servlets = null;

    public ServletManagerImpl(HashMap h) {
        servlets = h;
    }

    /* (non-Javadoc)
	 * @see org.dajoo.httpd.ServletManager#getServlet(java.lang.String)
	 */
    public Servlet getServlet(String path) {
        String key = parseTopDir(path) + ":" + parseAction(path);
		logger.debug("path = " + path);
		logger.debug("key = " + key);
        return (Servlet)servlets.get(key);
	}

    private String parseTopDir(String path) {
    	if(path==null) return "";
    	String[] dilms = path.split("/");
    	//System.out.print("dilms = ");
    	//for(int i=0;i<dilms.length;i++)
    	//	System.out.print("," + dilms[i]);
    	//System.out.println("");
    	if(dilms.length<2 || dilms.length==2 && !path.endsWith("/"))
    		return "";
    	else
    	    return dilms[1];
    }

    private String parseAction(String path) {
		if(path==null || path.indexOf("?")<0 ) return "";
		String paramList = path.split("\\?")[1];
		String actionParam;
		if(paramList.indexOf("&")<0)
			actionParam = paramList;
		else
			actionParam = paramList.split("&")[1];
		String action;
		if(actionParam.indexOf("=")<0)
			action = actionParam;
		else
			action = actionParam.split("=")[1];
    	return action;
    }

}
