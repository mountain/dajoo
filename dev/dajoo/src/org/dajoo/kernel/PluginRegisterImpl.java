/**
 * (X)PluginRegisterImpl.java
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
package org.dajoo.kernel;

import org.apache.log4j.Logger;

/**
 * @author Mingli Yuan
 *
 */
public class PluginRegisterImpl implements PluginRegister {

	private static Logger logger = Logger.getLogger(PluginRegisterImpl.class);

    private DajooPlugin[] plugins;

    public PluginRegisterImpl() {
    }

    public PluginRegisterImpl(DajooPlugin[] p) {
        plugins = p;
    }

    /*
     * @see org.dajoo.kernel.PluginRegister#assemble()
     */
    public void register() {
        if(plugins==null) return;
        logger.info("assembling...");
        for(int i=0;i<plugins.length;i++)
            plugins[i].register();
    }

    /* (non-Javadoc)
     * @see org.dajoo.kernel.PluginRegister#getPlugins()
     */
    public DajooPlugin[] getPlugins() {
        return plugins;
    }

    /* (non-Javadoc)
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        if(plugins==null) return;
        logger.info("starting...");
        for(int i=0;i<plugins.length;i++)
            plugins[i].start();
    }

    /* (non-Javadoc)
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        if(plugins==null) return;
        logger.info("stopping...");
        for(int i=0;i<plugins.length;i++)
            plugins[i].stop();
    }

}
