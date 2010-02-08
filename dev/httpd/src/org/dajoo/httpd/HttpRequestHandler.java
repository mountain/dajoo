/**
 * (X)HttpRequestHandler.java
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
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class HttpRequestHandler implements EventHandler {

	private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
	private RequestPipe pipe;

	public HttpRequestHandler(RequestPipe p) {
		pipe = p;
	}

	public void handle(SelectionKey key) throws IOException {
		if (!key.isReadable()) return;
		int count = ((SocketChannel)key.channel()).read(byteBuffer);
		if ( count > 0) {
			byteBuffer.flip();
			byte[] data = new byte[count];
			byteBuffer.get(data, 0, count);
			pipe.write(data);
		} else if ( count < 0) {
			key.channel().close();
		}
		byteBuffer.clear();
	}

}
