/**
 * (X)QuotesProcessor.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


/**
 * @author Mingli Yuan
 *
 */
public class QuotesProcessor extends AbstractWikiProcessor {

    private static final int NO_APOSTROPHIC = 0;
    private static final int ONE_APOSTROPHIC = 1;
    private static final int TWO_APOSTROPHIC = 2;
    private static final int THREE_APOSTROPHIC = 3;
    private static final int FOUR_APOSTROPHIC = 4;
    private static final int FIVE_APOSTROPHIC = 5;
    private static final int MORE_APOSTROPHIC = 6;

    /* (non-Javadoc)
     * @see org.dajoo.wiki.WikiTextProcessor#process(java.lang.String)
     */
    public String process(String text) {
        String newText = "";//$NON-NLS-1$
        StringTokenizer tokens = new StringTokenizer(text, "\n");//$NON-NLS-1$
        while(tokens.hasMoreTokens()) {
            String line = tokens.nextToken();
            newText += generateQuotesHTML(splitQuotes(line));
            newText += "\n";//$NON-NLS-1$
        }
        return newText;
    }

    private List<String> splitQuotes(String text) {
        List<String> tokenList = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(text, "'", true);//$NON-NLS-1$
        int numBold = 0;
        int numItalics = 0;
        int state = NO_APOSTROPHIC;
        String apostrophics = "";//$NON-NLS-1$
        while(tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            switch(state) {
            case NO_APOSTROPHIC:
                if(token.equals("'")) {//$NON-NLS-1$
                    apostrophics += token;
                    state = ONE_APOSTROPHIC;
                    if(tokenList.size()==0) tokenList.add("");//$NON-NLS-1$
                } else {
                    tokenList.add(token);
                    state = NO_APOSTROPHIC;
                }
                break;
            case ONE_APOSTROPHIC:
                if(token.equals("'")) {//$NON-NLS-1$
                    apostrophics += token;
                    state = TWO_APOSTROPHIC;
                } else {
                    token += "'";//$NON-NLS-1$
                    tokenList.add(token);
                    state = NO_APOSTROPHIC;
                }
                break;
            case TWO_APOSTROPHIC:
                if(token.equals("'")) {//$NON-NLS-1$
                    apostrophics += token;
                    state = THREE_APOSTROPHIC;
                } else {
                    numItalics++;
                    tokenList.add("''");//$NON-NLS-1$
                    tokenList.add(token);
                    state = NO_APOSTROPHIC;
                }
                break;
            case THREE_APOSTROPHIC:
                if(token.equals("'")) {//$NON-NLS-1$
                    apostrophics += token;
                    state = FOUR_APOSTROPHIC;
                } else {
                    numBold++;
                    tokenList.add("'''");//$NON-NLS-1$
                    tokenList.add(token);
                    state = NO_APOSTROPHIC;
                }
                break;
            case FOUR_APOSTROPHIC:
                if(token.equals("'")) {//$NON-NLS-1$
                    apostrophics += token;
                    state = FIVE_APOSTROPHIC;
                } else {
                    int last = tokenList.size()-1;
                    tokenList.set(last, tokenList.get(last) + "'");//$NON-NLS-1$
                    numBold++;
                    tokenList.add("'''");//$NON-NLS-1$
                    state = NO_APOSTROPHIC;
                }
                break;
            case FIVE_APOSTROPHIC:
                if(token.equals("'")) {//$NON-NLS-1$
                    apostrophics += token;
                    state = MORE_APOSTROPHIC;
                } else {
                    numBold++;
                    numItalics++;
                    token += apostrophics;
                    tokenList.add(token);
                    state = NO_APOSTROPHIC;
                }
                break;
            case MORE_APOSTROPHIC:
                if(token.equals("'")) {//$NON-NLS-1$
                    apostrophics += token;
                    state = MORE_APOSTROPHIC;
                } else {
                    String temp = apostrophics.substring(0,apostrophics.length()-5);
                    int last = tokenList.size()-1;
                    tokenList.set(last, tokenList.get(last) + temp);
                    numBold++;
                    numItalics++;
                    tokenList.add("'''''");//$NON-NLS-1$
                    tokenList.add(token);
                    apostrophics = "";
                    state = NO_APOSTROPHIC;
                }
                break;
            }
        }
        return tokenList;
    }

    private String generateQuotesHTML(List<String> tokens) {
        String output = "";//$NON-NLS-1$
        String buffer = "";//$NON-NLS-1$
        String state = "";//$NON-NLS-1$
        int i = 0;
        Iterator<String> iter = tokens.iterator();
        while(iter.hasNext()) {
            String r = iter.next();
            if ((i % 2) == 0) {
                if (state.equals("both"))//$NON-NLS-1$
                    buffer += r;
                else
                    output += r;
            } else {
                if (r.equals("''")) {//$NON-NLS-1$
                    if (state.equals("i"))//$NON-NLS-1$
                    { output += "</i>"; state = ""; }//$NON-NLS-1$
                    else if (state.equals("b"))//$NON-NLS-1$
                    { output += "<i>"; state = "bi"; }//$NON-NLS-1$
                    else if (state.equals("bi"))//$NON-NLS-1$
                    { output += "</i>"; state = "b"; }//$NON-NLS-1$
                    else if (state.equals("ib"))//$NON-NLS-1$
                    {
                        output += "</b></i><b>"; //$NON-NLS-1$
                        state = "b";//$NON-NLS-1$
                    }
                    else if (state.equals("both"))//$NON-NLS-1$
                    {
                        output += "<b><i>" + buffer + "</i>";//$NON-NLS-1$
                        state = "b";//$NON-NLS-1$
                    }
                    else // state can be ""
                    {
                        output += "<i>";//$NON-NLS-1$
                        state += "i";//$NON-NLS-1$
                    }
                } else
                if (r.equals("'''")) {
                    if (state.equals("i"))
                    { output += "<b>"; state = "ib"; }
                    else if (state.equals("b"))
                    { output += "</b>"; state = ""; }
                    else if (state.equals("bi"))
                    { output +="</i></b><i>"; state = "i"; }
                    else if (state.equals("ib"))
                    { output += "</b>"; state = "i"; }
                    else if (state.equals("both"))
                    { output += "<i><b>" + buffer + "</b>"; state = "i"; }
                    else //state can be ""
                    { output += "<b>"; state += "b"; }
                } else
                if (r.equals("'''''")) {
                    if (state.equals("b"))
                    { output += "</b><i>"; state = "i"; }
                    else if (state.equals("i"))
                    { output += "</i><b>"; state = "b"; }
                    else if (state.equals("bi"))
                    { output += "</i></b>"; state = ""; }
                    else if (state.equals("ib"))
                    { output += "</b></i>"; state = ""; }
                    else if (state.equals("both"))
                    { output += "<i><b>" + buffer + "</b></i>"; state = ""; }
                    else // (state.equals("")
                    { buffer = ""; state = "both"; }
                }
            }
            i++;
        }
        // Now close all remaining tags.  Notice that the order is important.
        if (state.equals("b") || state.equals("bi"))//$NON-NLS-1$
            output += "</b>";//$NON-NLS-1$
        if (state.equals("i") || state.equals("bi") || state.equals("bi"))//$NON-NLS-1$
            output += "</i>";//$NON-NLS-1$
        if (state.equals("bi"))//$NON-NLS-1$
            output += "</b>";//$NON-NLS-1$
        if (state.equals("both"))//$NON-NLS-1$
            output += "<b><i>"+ buffer + "</i></b>";//$NON-NLS-1$
        return output;
    }

}
