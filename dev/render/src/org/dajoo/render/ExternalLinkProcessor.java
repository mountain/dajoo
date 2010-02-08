/**
 * (X)ExternalLinkProcessor.java
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mingli Yuan
 *
 */
public class ExternalLinkProcessor extends AbstractWikiProcessor {

    private Pattern externalLinkPattern =
        Pattern.compile("\\[([^\\]]*)\\]");//$NON-NLS-1$
    private Pattern targetShowPattern =
        Pattern.compile("([^ ]+) ([^ ]+)");//$NON-NLS-1$

    /*
     * process wiki text
     * @see org.dajoo.render.WikiProcessor#process(java.lang.String)
     */
    public String process(String text) {
        Matcher m = null;
        boolean found = true;
        while (found) {
            m = externalLinkPattern.matcher(text);
            found = m.find();
            if(found) {
                String g1 = m.group(1);
                String link = constructExternalLink(g1);
                text = m.replaceFirst(link);
            }
        }
        return text;
    }

    private String constructExternalLink(String wikiLink){
        String showName = "";//$NON-NLS-1$
        String targetName = "";//$NON-NLS-1$
        Matcher m1 = targetShowPattern.matcher(wikiLink);
        if(m1.find()) {
            targetName = m1.group(1);
            showName = m1.group(2);
        } else {
            targetName = wikiLink;
            showName = "&#x261c;";//$NON-NLS-1$
        }
        String link = "<a class=\"urlextern\" href=\"" + targetName + "\">" + showName + "</a>";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return link;
    }

}
