/**
 * (X)RequestProcessor.java
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

import org.apache.log4j.Logger;
import org.dajoo.kernel.DaemonException;


public class RequestProcessor implements Runnable {

	private static Logger logger = Logger.getLogger(RequestProcessor.class);

	private ServletManager svltMgr;
	private ServletContext svltCtx;
	private RequestPipeQueue requestQueue;

	public RequestProcessor(ServletManager m, ServletContext c, RequestPipeQueue q) {
		svltMgr = m;
		svltCtx = c;
		requestQueue = q;
	}

	public void run() {
		while (true) {
			RequestPipe pipe = (RequestPipe) requestQueue.dequeue();
			try {
				while (true) {
					HttpRequest req = new HttpRequest(pipe.getPipedInputStream(), svltCtx);
					HttpResponse resp = new HttpResponse(pipe.getSelectionKey());
					String path = req.getPath();
					Servlet servlet = svltMgr.getServlet(path);
					if(servlet!=null) {
						logger.debug("Servlet was selected: servlet = " + servlet.getClass().getName());
						servlet.service(req, resp);
					} else {
						throw new DaemonException("Servlet selection failed.");
					}
					if (pipe.notifyServiceDone()) break;
				}
			} catch (Exception e) {
				logger.error("Error when process request.", e);
				pipe.getSelectionKey().cancel();
				pipe.getSelectionKey().selector().wakeup();
			}
		}
	}

}
