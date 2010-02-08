/**
 * (X)MimeTypes.java
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

public class MimeTypes {

    public static HashMap<String, String> mimeMap = new HashMap<String, String>();

    static {
        mimeMap.put("", "content/unknown");
        mimeMap.put("gif", "image/gif");
        mimeMap.put("png", "image/png");
        mimeMap.put("jpg", "image/jpeg");
        mimeMap.put("jpeg", "image/jpeg");
        mimeMap.put("htm", "text/html");
        mimeMap.put("html", "text/html");
        mimeMap.put("text", "text/plain");
        mimeMap.put("txt", "text/plain");
        mimeMap.put("css", "text/css");
        mimeMap.put("xml", "text/xml");
    };

    public static String getMimeType(String ext) {
        String type = (String) mimeMap.get(ext);
        if (type == null) type = "unkown/unkown";
        return type;
    }

}
