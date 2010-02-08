/**
 * (X)AbstractDajooPlugin.java
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

import org.picocontainer.PicoContainer;

/**
 * @author Mingli Yuan
 *
 */
public abstract class AbstractDajooPlugin implements DajooPlugin {


    public AbstractDajooPlugin() {
    }

    /* deploy the plugin
     * @see org.dajoo.kernel.DajooPlugin#deploy()
     */
    public void deploy() {
        // Do nothing
    }

    /* undeploy the plugin
     * @see org.dajoo.kernel.DajooPlugin#undeploy()
     */
    public void undeploy() {
        // Do nothing
    }

    /* configure the plugin before running
     * @see org.dajoo.kernel.DajooPlugin#register()
     */
    public void register() {
        // Do nothing
    }

    /* get container
     * @see org.dajoo.kernel.DajooPlugin#getContainer()
     */
    public PicoContainer getContainer() {
        return DajooConfiguration.getContainer();
    }

    /*
     * Default implementation of the intialization method: just do nothing.
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        //Do nothing
    }

    /*
     * Default implementation of the destroy method: just do nothing.
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        //Do nothing
    }

}
