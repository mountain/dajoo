/**
 * (X)TemplatePlugin.java
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
package org.dajoo.template;

import java.io.File;

import org.dajoo.kernel.AbstractDajooPlugin;
import org.dajoo.kernel.DajooConfiguration;
import org.picocontainer.MutablePicoContainer;

import freemarker.template.DefaultObjectWrapper;

/**
 * @author Mingli Yuan
 *
 */
public class TemplatePlugin extends AbstractDajooPlugin {

    /*
     * @Override org.dajoo.kernel.AbstractDajooPlugin.assemble
     * @see org.dajoo.kernel.DajooPlugin#assemble()
     */
    @Override
    public void register() {
        MutablePicoContainer pico = (MutablePicoContainer)getContainer();
        pico.registerComponentImplementation(freemarker.template.Configuration.class);

        if(DajooConfiguration.getConfiguration("template.dir")==null)
        	DajooConfiguration.setConfiguration("template.dir", System.getProperty("user.dir") + File.separatorChar + "template");
    }

    /*
     * @see org.picocontainer.Startable#start()
     * @Override org.dajoo.kernel.AbstractDajooPlugin.start
     */
    @Override
    public void start() {
        freemarker.template.Configuration templateCfg = (freemarker.template.Configuration) getContainer().getComponentInstance(freemarker.template.Configuration.class);
        try {
            templateCfg.setDirectoryForTemplateLoading(new File(DajooConfiguration.getConfiguration("template.dir")));
            templateCfg.setObjectWrapper(new DefaultObjectWrapper());
        } catch (Exception e) {
            e.printStackTrace();
        };
    }

}
