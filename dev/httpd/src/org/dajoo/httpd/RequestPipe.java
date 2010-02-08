/**
 * (X)RequestPipe.java
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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.channels.SelectionKey;

public class RequestPipe {

    private SelectionKey key;
    private PipedInputStream pipedIn;
    private BufferedOutputStream streamOut;
    private RequestPipeQueue pipeQueue;

    private boolean readyForService = false;

    public RequestPipe(RequestPipeQueue q) throws IOException {
        pipeQueue = q;
        PipedOutputStream pipeOut = new PipedOutputStream();
        pipedIn = new PipedInputStream(pipeOut);
        streamOut = new BufferedOutputStream(pipeOut, 1048);
    }

    public void setSelectionKey(SelectionKey key) {
        assert(key.isReadable());
        this.key = key;
    }

    public SelectionKey getSelectionKey() {
        return key;
    }

    public PipedInputStream getPipedInputStream() {
        return pipedIn;
    }

    public synchronized void write(byte[] data) throws IOException {
        streamOut.write(data);
        streamOut.flush();
        if (!readyForService) {
            readyForService = true;
            pipeQueue.enqueue(this);
        }
    }

    public synchronized boolean notifyServiceDone() throws IOException {
        if (pipedIn.available() <= 0) readyForService = false;
        return !readyForService;
    }

}
