/**
 * (X)ParagraphProcessor.java
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

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mingli Yuan
 *
 */
public class ParagraphProcessor extends AbstractWikiProcessor {

    private Pattern openPattern =
        Pattern.compile("^(<table|<blockquote|<h1|<h2|<h3|<h4|<h5|<h6|<pre|<tr|<p|<ul|<ol|<li|<dl|<dd|<\\/tr|<\\/td|<\\/th)");//$NON-NLS-1$
    private Pattern closePattern =
        Pattern.compile("^(<\\/table|<\\/blockquote|<\\/h1|<\\/h2|<\\/h3|<\\/h4|<\\/h5|<\\/h6|<td|<th|<div|<\\/div|<hr|<\\/pre|<\\/p|<\\/li|<\\/ul|<\\/ol|<\\/dl|<\\/dd)");//$NON-NLS-1$

    /*
     * process wiki text
     * @see org.dajoo.render.WikiProcessor#process(java.lang.String)
     */
    public String process(String text) {
        String newText = "";//$NON-NLS-1$
        StringTokenizer tokens = new StringTokenizer(text, "\r\n");//$NON-NLS-1$
        while(tokens.hasMoreTokens()) {
            String line = tokens.nextToken();
            Matcher openMatcher = openPattern.matcher(line);
            Matcher closeMatcher = closePattern.matcher(line);
            if(!openMatcher.find() && !closeMatcher.find())
                line = "<p>" + line + "</p>";//$NON-NLS-1$
            newText += ( line + "\r\n" );//$NON-NLS-1$
        }
        return newText;
    }

}
