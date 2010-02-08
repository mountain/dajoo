/**
 * (X)RenderPlugin.java
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
package org.dajoo.render;

import java.io.File;

import org.dajoo.kernel.AbstractDajooPlugin;
import org.dajoo.kernel.DajooConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 * @author Mingli Yuan
 *
 */
public class RenderPlugin extends AbstractDajooPlugin {

    /*
     * @Override org.dajoo.kernel.AbstractDajooPlugin.assemble
     * @see org.dajoo.kernel.DajooPlugin#assemble()
     */
    @Override
    public void register() {
        MutablePicoContainer pico = (MutablePicoContainer)getContainer();
        pico.registerComponentImplementation(WikiRender.class, WikiRenderImpl.class);
        pico.registerComponentImplementation(WikiNameMgrImpl.class);
        pico.registerComponentImplementation(HorizontalRuleProcessor.class);
        pico.registerComponentImplementation(HeadingProcessor.class);
        pico.registerComponentImplementation(QuotesProcessor.class);
        pico.registerComponentImplementation(InternalLinkProcessor.class);
        pico.registerComponentImplementation(ExternalLinkProcessor.class);
        pico.registerComponentImplementation(TableProcessor.class);
        pico.registerComponentImplementation(ListProcessor.class);
        pico.registerComponentImplementation(ParagraphProcessor.class);

        if(DajooConfiguration.getConfiguration("render.dir")==null)
        	DajooConfiguration.setConfiguration("render.dir", System.getProperty("user.dir") + File.separatorChar + "wiki");
    }

    /*
     * Default implementation of the intialization method: just do nothing.
     * @see org.picocontainer.Startable#start()
     */
    @Override
    public void start() {
        WikiRender wikiParser = (WikiRender) getContainer().getComponentInstance(WikiRender.class);
        wikiParser.start();
    }


}
