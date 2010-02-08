/**
 * (X)HeadingProcessor.java
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
public class HeadingProcessor extends AbstractWikiProcessor {

    private Pattern Heading6Pattern =
        Pattern.compile("^======([ \t]*(.+)[ \t]*)======(\\s|$)", Pattern.DOTALL);//$NON-NLS-1$
    private Pattern Heading5Pattern =
        Pattern.compile("^=====([ \t]*(.+)[ \t]*)=====(\\s|$)", Pattern.DOTALL);//$NON-NLS-1$
    private Pattern Heading4Pattern =
        Pattern.compile("^====([ \t]*(.+)[ \t]*)====(\\s|$)", Pattern.DOTALL);//$NON-NLS-1$
    private Pattern Heading3Pattern =
        Pattern.compile("^===([ \t]*(.+)[ \t]*)===(\\s|$)", Pattern.DOTALL);//$NON-NLS-1$
    private Pattern Heading2Pattern =
        Pattern.compile("^==([ \t]*(.+)[ \t]*)==(\\s|$)", Pattern.DOTALL);//$NON-NLS-1$

    /*
     * Convert things like "===xxx===" to headings.
     * @param text input string
     * @return processed string
     * @see org.dajoo.wiki.WikiTextProcessor#process(java.lang.String)
     */
    public String process(String text) {
        text = doHeadings(Heading6Pattern, text, 6);
        text = doHeadings(Heading5Pattern, text, 5);
        text = doHeadings(Heading4Pattern, text, 4);
        text = doHeadings(Heading3Pattern, text, 3);
        text = doHeadings(Heading2Pattern, text, 2);
        return text;
    }

    private String doHeadings(Pattern p, String text, int i) {
        String newText = "";//$NON-NLS-1$
        StringTokenizer tokens = new StringTokenizer(text, "\r\n");//$NON-NLS-1$
        while(tokens.hasMoreTokens()) {
            String line = tokens.nextToken();
            Matcher m = null;
            boolean found = true;
            while (found) {
                m = p.matcher(line);
                found = m.find();
                if(found) {
                    String head = m.group(1);
                    line = m.replaceFirst("<h" + i + ">" + head + "</h" + i + ">\n");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
            }
            newText += (line + "\r\n");//$NON-NLS-1$
        }
        return newText;
    }

}
