/**
 * (X)RequestPipeQueue.java
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

import java.util.*;

import org.apache.log4j.Logger;

public class RequestPipeQueue {

    private static Logger logger = Logger.getLogger(RequestPipeQueue.class);

    private int waitingWorkers = 0;

    private LinkedList<RequestPipe> list = new LinkedList<RequestPipe>();

    public synchronized void enqueue(RequestPipe req) {
        list.addLast(req);
        notify();
        logger.debug("Request is enqueued.");
    }

    public synchronized RequestPipe dequeue() {
        if (isEmpty()) {
            try {
                waitingWorkers++;
                wait();
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            waitingWorkers--;
        }
        logger.debug("Request is dequeued.");
        return list.removeFirst();
    }

    public boolean isEmpty() {
        return (list.size() - waitingWorkers <= 0);
    }
}
