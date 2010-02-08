/**
 * (X)WikiRenderImpl.java
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

/**
 * @author Mingli Yuan
 *
 */
public class WikiRenderImpl implements WikiRender{

    /*
     * Processor list
     */
    private WikiProcessor[] textProcessors;

    private WikiNameManager wikiNameMgr;

    /*
     * private construction
     */
    public WikiRenderImpl(WikiProcessor[] processors, WikiNameManager w) {
        textProcessors = processors;
        wikiNameMgr = w;
    }

    /*
     * intialization of the parser
     */
    public void start(){
        int len = textProcessors.length;
        for(int i=0;i<len;i++)
            textProcessors[i].start();

        wikiNameMgr.start();
    }

    /*
     * destroy of the parser
     */
    public void stop(){
        //Do nothing
    }

    /*
     * parse the wiki text
     */
	public String parse(String text) {
        int len = textProcessors.length;
        for(int i=0;i<len;i++) {
            text = textProcessors[i].process(text);
        }
		return text;
	}

}
