/**
 * (X)ListProcessor.java
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

/**
 * @author Mingli Yuan
 *
 */
public class ListProcessor extends AbstractWikiProcessor {

    private boolean dtIsOpen = false;

    /*
     * process text.
     * @see org.dajoo.render.WikiProcessor#process(java.lang.String)
     */
    public String process(String text) {
        String newText = "";//$NON-NLS-1$
        String lastPrefix = "";//$NON-NLS-1$
        String prefix = null;
        StringTokenizer tokens = new StringTokenizer(text, "\n\r");//$NON-NLS-1$
        while(tokens.hasMoreTokens()) {
            String line = tokens.nextToken();
            prefix = getPrefix(line);

            String lastDelta = getDeltaOnLastPrefix(lastPrefix, prefix);
            newText += closeList(lastDelta);
            String commonPrefix = getCommonPrefix(lastPrefix, prefix);
            newText += nextItem(commonPrefix);
            String curDelta = getDeltaOnCurPrefix(lastPrefix, prefix);
            newText += openList(curDelta);

            String content = getContent(line);
            newText += content;
            newText += "\n";//$NON-NLS-1$
            lastPrefix = prefix;
        }
        if(!lastPrefix.equals("")) {
            prefix = "";//$NON-NLS-1$
            String lastDelta = getDeltaOnLastPrefix(lastPrefix, prefix);
            newText += closeList(lastDelta);
        }
        return newText;
    }

    /**
     * get prefix of the line
     */
    private String getPrefix(String line) {
        String prefix = "";//$NON-NLS-1$
        int len = line.length();
        for (int i=0;i<len;i++) {
            char c = line.charAt(i);
            if(c == ' ' || c == '\t') {
                //NOOP
            } else
            if(c == '*' || c == '#' || c == ':' || c == ';') {
                prefix += c;
            } else {
                break;
            }
        }
        return prefix;
    }

    /**
     * get content of the line
     */
    private String getContent(String line) {
        int len = line.length();
        int i = 0;
        for (;i<len;i++) {
            char c = line.charAt(i);
            if(c != ' ' && c != '\t' &&
               c != '*' && c != '#' && c != ':' && c != ';') {
                break;
            }
        }
        return line.substring(i);
    }

    /**
     * getCommon() returns the length of the longest common substring
     * of both arguments, starting at the beginning of both.
     */
    private int getCommon(String str1, String str2) {
        int len1 = str1.length();
        int shorter = str2.length();
        if (len1 < shorter) shorter = len1;
        int i = 0;
        for (;i<shorter;i++) {
            if (str1.charAt(i) != str2.charAt(i)) break;
        }
        return i;
    }
    /**
     * get common prefix
     */
    private String getCommonPrefix(String str1, String str2) {
        return str1.substring(0, getCommon(str1, str2));
    }
    /**
     * get delta on last prefix
     */
    private String getDeltaOnLastPrefix(String str1, String str2) {
        String result = null;
        int common = getCommon(str1, str2);
        int len1 = str1.length();
        if(common==len1)
            result = "";//$NON-NLS-1$
        else
            result = str1.substring(common);
        return result;
    }
    /**
     * get delta on current prefix
     */
    private String getDeltaOnCurPrefix(String str1, String str2) {
        String result = null;
        int common = getCommon(str1, str2);
        int len2 = str2.length();
        if(common==len2)
            result = "";//$NON-NLS-1$
        else
            result = str2.substring(common);
        return result;
    }

    /**
     * These next three functions open, continue, and close the list
     * element appropriate to the prefix character passed into them.
     */
    private String openList(char c) {
        String result = null;
        if ('*' == c ) {
            result = "<ul><li>";//$NON-NLS-1$
        } else if ('#' == c) {
            result = "<ol><li>";//$NON-NLS-1$
        } else if ( ':' == c ) {
            result = "<dl><dd>";//$NON-NLS-1$
        } else if ( ';' == c ) {
            result = "<dl><dt>";//$NON-NLS-1$
            this.dtIsOpen = true;
        } else {
            result = "<!-- ERR 1 -->";//$NON-NLS-1$
        }
        return result;
    }
    private String openList(String s) {
        String result = "";//$NON-NLS-1$
        int len = s.length();
        for(int i=len-1;i>=0;i--) {
            result += openList(s.charAt(i));
        }
        return result;
    }

    private String nextItem( char c ) {
        String close = null;
        if ( '*' == c || '#' == c ) { return "</li><li>"; }//$NON-NLS-1$
        else if ( ':' == c || ';' == c ) {
            close = "</dd>";//$NON-NLS-1$
            if ( this.dtIsOpen ) { close = "</dt>"; }//$NON-NLS-1$
            String delta = null;
            if ( ';' == c ) {
                this.dtIsOpen = true;
                delta = "<dt>";//$NON-NLS-1$
            } else {
                this.dtIsOpen = false;
                delta = "<dd>";//$NON-NLS-1$
            }
            return close + delta;
        }
        return "<!-- ERR 2 -->";//$NON-NLS-1$
    }
    private String nextItem(String s) {
        String result = "";//$NON-NLS-1$
        int len = s.length();
        if(len!=0) {
            result = nextItem(s.charAt(len-1));
        }
        return result;
    }

    private String closeList( char c ) {
        String text = null;
        if ( '*' == c ) { text = "</li></ul>"; }//$NON-NLS-1$
        else if ( '#' == c ) { text = "</li></ol>"; }//$NON-NLS-1$
        else if ( ':' == c ) {
            if ( this.dtIsOpen ) {
                this.dtIsOpen = false;
                text = "</dt></dl>";//$NON-NLS-1$
            } else {
                text = "</dd></dl>";//$NON-NLS-1$
            }
        }
        else {  return "<!-- ERR 3 -->"; }//$NON-NLS-1$
        return text + "\n";//$NON-NLS-1$
    }
    private String closeList(String s) {
        String result = "";//$NON-NLS-1$
        int len = s.length();
        for(int i=len-1;i>=0;i--) {
            result += closeList(s.charAt(i));
        }
        return result;
    }

}
