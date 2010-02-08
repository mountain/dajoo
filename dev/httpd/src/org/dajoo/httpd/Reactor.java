/**
 * (X)Reactor.java
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
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class Reactor implements Runnable{

	private static Logger logger = Logger.getLogger(Reactor.class);

    private Selector selector;

    Reactor() throws IOException {
	    selector = Selector.open();
    }

    public void run() {
    	while(true)
    		handleEvents();
    }

    private void handleEvents() {
		SelectionKey key = null;
	    try {
	    	while(true) {
			    selector.select();
			    Iterator iter = selector.selectedKeys().iterator();
			    while (iter.hasNext()) {
			        key = (SelectionKey)iter.next();
			        iter.remove();
			        EventHandler handler = (EventHandler)key.attachment();
			        handler.handle(key);
			    }
	    	}
   	    } catch (IOException ioe) {
			key.cancel();
   	    	logger.error("Error when handle events", ioe);
   	    } catch (NullPointerException e) {
   	    	logger.error("Error when handle events", e);
		}
   }

    public SelectionKey register(SelectableChannel channel, int ops, EventHandler handler) throws IOException {
    	return channel.register(selector, ops, handler);
    }

}
