/**
 * (X)HttpdPlugin.java
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

import org.apache.log4j.Logger;
import org.dajoo.kernel.AbstractDajooPlugin;
import org.dajoo.kernel.Daemon;
import org.dajoo.kernel.DajooConfiguration;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;

/**
 * @author Mingli Yuan
 *
 */
public class HttpdPlugin extends AbstractDajooPlugin {

    private static Logger logger = Logger.getLogger(HttpdPlugin.class);

    private Daemon httpd;

    /*
     * @see org.dajoo.kernel.DajooPlugin#register()
     * @Override org.dajoo.kernel.AbstractDajooPlugin.register
     */
    @Override
    public void register() {
        MutablePicoContainer pico = (MutablePicoContainer)getContainer();
        pico.registerComponentImplementation(Daemon.class, HttpDaemon.class);
        pico.registerComponentImplementation(ServletManager.class, ServletManagerImpl.class,
            new Parameter[]{ new ComponentParameter(Servlet.class, false) }
        );
        pico.registerComponentImplementation(":", FileServlet.class);
        pico.registerComponentImplementation("images:", FileServlet.class);

        if(DajooConfiguration.getConfiguration("httpd.dir")==null)
            DajooConfiguration.setConfiguration("httpd.dir", System.getProperty("user.dir") + File.separatorChar + "httpd");
    }

    /*
     * @see org.picocontainer.Startable#start()
     * @Override org.dajoo.kernel.AbstractDajooPlugin.start
     */
    @Override
    public void start() {
        httpd = (Daemon) getContainer().getComponentInstance(Daemon.class);
        httpd.start();
        try {
            httpd.run();
        } catch(Exception e) {
            logger.error("Error when start httpd daemon.", e);
        }
    }

    public Daemon getHttpd() {
        return httpd;
    }

}
