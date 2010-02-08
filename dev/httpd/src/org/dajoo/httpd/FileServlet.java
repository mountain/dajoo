/**
 * (X)FileServlet.java
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

import java.io.IOException;
import java.util.HashMap;

public class FileServlet implements Servlet {

    private static HashMap<String, FileContent> cache = new HashMap<String, FileContent>();

    /*
     * (non-Javadoc)
     *
     * @see org.dajoo.httpd.Servlet#service(org.dajoo.httpd.HttpRequest,
     *      org.dajoo.httpd.HttpResponse)
     */
    public void service(HttpRequest req, HttpResponse resp)
            throws ServletException {
        String path = req.getRequestedResource();
        FileContent fileContent = cache.containsKey(path) ? (FileContent) cache
                .get(path) : cacheFile(req, path);
        resp.setContent(fileContent);
        resp.sendNormally();

        if (req.closeAfterResponse())
            resp.close();
    }

    private FileContent cacheFile(HttpRequest req, String path)
            throws ServletException {
        FileContent fileContent = null;
        String realPath = req.getServletContext().getRealPath(path);
        try {
            fileContent = new FileContent(realPath);
        } catch (IOException e) {
            throw new ServletException(e);
        }
        cache.put(path, fileContent);
        return fileContent;
    }

}
