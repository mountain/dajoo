/**
 * (X)StringContent.java
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

import java.io.*;
import java.nio.*;
import java.nio.charset.Charset;

public class StringContent implements Content {

    private static Charset utf8 = Charset.forName("UTF-8");

    private String type;
    private ByteBuffer content;

    public StringContent(CharSequence c, String t) {
    	String temp = c.toString();
    	if (!temp.endsWith("\n")) temp += "\n";
    	content = utf8.encode(CharBuffer.wrap(temp));
    	type = t + "; charset=utf-8";
    }

    public StringContent(CharSequence c) {
    	this(c, "text/plain");
    }

    StringContent(Exception e) {
    	StringWriter sw = new StringWriter();
    	e.printStackTrace(new PrintWriter(sw));
    	content = utf8.encode(CharBuffer.wrap(sw.toString()));
    	type = "text/plain; charset=utf-8";
    }

	public long getLength() {
		return content.remaining();
	}

	public String getType() {
		return type;
	}

	public ByteBuffer getContent() {
		return content;
	}

}
