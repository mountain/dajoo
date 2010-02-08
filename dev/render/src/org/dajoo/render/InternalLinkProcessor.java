/**
 * (X)InternalLinkProcessor.java
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mingli Yuan
 *
 */
public class InternalLinkProcessor extends AbstractWikiProcessor {

    private Pattern internalLinkPattern = Pattern
            .compile("\\[\\[([^\\]]*)\\]\\]");

    private Pattern targetShowPattern = Pattern
            .compile("([\\S]+)\\|([^\\|]+)");

    private Pattern targetPattern = Pattern.compile("([\\S]*):([^:]+)");

    private WikiNameManager wikiNameMgr;

    public InternalLinkProcessor(WikiNameManager w) {
        wikiNameMgr = w;
    }

    public String process(String text) {
        Matcher m = null;
        boolean found = true;
        while (found) {
            m = internalLinkPattern.matcher(text);
            found = m.find();
            if (found) {
                String g1 = m.group(1);
                String link = constructInternalLink(g1);
                text = m.replaceFirst(link);
            }
        }
        return text;
    }

    private String constructInternalLink(String wikiLink) {
        String showName = "";
        String targetName = "";
        String namespace = "";
        String realName = "";
        Matcher m1 = targetShowPattern.matcher(wikiLink);
        if (m1.find()) {
            targetName = m1.group(1);
            showName = m1.group(2);
        } else {
            targetName = wikiLink;
            showName = wikiLink;
        }
        Matcher m2 = targetPattern.matcher(targetName);
        if (m2.find()) {
            namespace = m2.group(1);
            realName = m2.group(2);
        } else {
            realName = wikiLink;
            if (showName == null || showName.equals(""))
                showName = wikiLink;
        }
        String link = null;
        try {
            if (wikiNameMgr.contains(targetName))
                link = "<a class=\"wikilink1\" href=\""
                        + URLEncoder.encode(targetName, "UTF-8") + "\">"
                        + showName + "</a>";
            else
                link = "<a class=\"wikilink2\" href=\""
                        + URLEncoder.encode(targetName, "UTF-8")
                        + "?action=edit\">" + showName + "</a>";
        } catch (UnsupportedEncodingException e) {
        }

        return link;
    }

}
