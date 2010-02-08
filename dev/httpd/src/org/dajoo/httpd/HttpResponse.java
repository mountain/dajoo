/**
 * (X)HttpResponse.java
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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Date;

public class HttpResponse {

	private SelectionKey key;
	private SocketChannel channel;

	private Content content;
	private CharBuffer headerBuffer = CharBuffer.allocate(1024);
	private static CharsetEncoder encoder =
		Charset.forName("UTF-8").newEncoder();

	private static String CRLF = "\r\n";
	private static String VERSION = "HTTP/1.1";

	public HttpResponse(SelectionKey key) {
		this.key = key;
		channel = (SocketChannel) key.channel();
	}

	public void setContent(Content c) {
		content = c;
	}

	public void sendNormally() throws ServletException {
		headerBuffer.clear();
		headerBuffer.put(VERSION + StatusCode.OK + CRLF);
		appendHeader("Date", new Date().toString());
		appendHeader("Connection", "Keep-Alive");
		appendHeader("Daemon", "Dajoo httpd/1.0");

		if(content!=null) {
			appendHeader("Content-Type", content.getType());
			appendHeader("Content-Length", String.valueOf(content.getLength()));
		}

		headerBuffer.put(CRLF);
		headerBuffer.flip();

		ByteBuffer data = content.getContent();

		try {
			channel.write(encoder.encode(headerBuffer));
			while (data.hasRemaining())
				channel.write(data);
			data.rewind();
		} catch(IOException e) {
			throw new ServletException(e);
		}
	}

	public void sendError(StatusCode code) throws ServletException {
		headerBuffer.clear();
		headerBuffer.put(VERSION + code + CRLF);
		headerBuffer.put("Date: " + new Date() + CRLF);
		headerBuffer.put(CRLF);
		headerBuffer.flip();

		try {
			channel.write(encoder.encode(headerBuffer));
		} catch(IOException e) {
			throw new ServletException(e);
		}
	}

	public void close() throws ServletException {
		try {
			key.channel().close();
		} catch(IOException e) {
			throw new ServletException(e);
		}
		key.selector().wakeup();
	}

	private void appendHeader(String name, String value) {
		headerBuffer.put(name);
		headerBuffer.put(": ");
		headerBuffer.put(value);
		headerBuffer.put(CRLF);
	}

}
