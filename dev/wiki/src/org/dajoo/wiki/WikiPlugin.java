/**
 * (X)WikiPlugin.java
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
package org.dajoo.wiki;

import java.io.File;

import org.dajoo.kernel.AbstractDajooPlugin;
import org.dajoo.kernel.DajooConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 * @author Mingli Yuan
 * @version 1.0
 *
 */
public class WikiPlugin extends AbstractDajooPlugin {

    /*
     * @Override org.dajoo.kernel.AbstractDajooPlugin.assemble
     * @see org.dajoo.kernel.DajooPlugin#assemble()
     */
    @Override
    public void register() {
        MutablePicoContainer pico = (MutablePicoContainer)getContainer();
        pico.registerComponentImplementation("wiki:", SrvShowWiki.class);//$NON-NLS-1$
        pico.registerComponentImplementation("wiki:show", SrvShowWiki.class);//$NON-NLS-1$
        pico.registerComponentImplementation("wiki:edit", SrvEditWiki.class);//$NON-NLS-1$
        pico.registerComponentImplementation("wiki:save", SrvSaveWiki.class);//$NON-NLS-1$
        pico.registerComponentImplementation("wiki:delete", SrvDeleteWiki.class);//$NON-NLS-1$
        pico.registerComponentImplementation("wiki:move", SrvMoveWiki.class);//$NON-NLS-1$

        if(DajooConfiguration.getConfiguration("wiki.dir")==null)
        	DajooConfiguration.setConfiguration("wiki.dir", System.getProperty("user.dir") + File.separatorChar + "wiki");
    }


}
