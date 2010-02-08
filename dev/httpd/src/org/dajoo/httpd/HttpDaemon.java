/**
 * (X)HttpDaemon.java
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

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import org.dajoo.kernel.Daemon;
import org.dajoo.kernel.DaemonException;
import org.dajoo.kernel.DajooConfiguration;

public class HttpDaemon implements Daemon {

	private ServletManager svltMgr;
	private ServletContext svltCtx;

	private ServerSocketChannel serverChannel;
	private RequestPipeQueue queue;

	private Reactor reactor;
	private RequestProcessor processor;

    public HttpDaemon(ServletManager mgr) throws DaemonException{
    	svltMgr = mgr;
    	svltCtx = new ServletContext(new File(DajooConfiguration.getConfiguration("httpd.dir")));
        queue = new RequestPipeQueue();

        try {

            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().setReuseAddress(true);
            serverChannel.socket().bind(new InetSocketAddress(8080), 1024);
            serverChannel.configureBlocking(false);

            reactor = new Reactor();
            reactor.register(serverChannel, SelectionKey.OP_ACCEPT,
                   new HttpAcceptor(serverChannel, reactor, queue));
            processor = new RequestProcessor(svltMgr, svltCtx, queue);

        } catch (IOException e) {
            throw new DaemonException(e);
        }
    }

    public void start() {

    }

    public void stop() {

    }

    public void run() throws DaemonException {
        Thread reactorThread = new Thread(reactor);
        reactorThread.start();
        Thread processorThread = new Thread(processor);
        processorThread.start();
    }

}
