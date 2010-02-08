/**
 * (X)TableProcessor.java
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TableProcessor
 *
 * This class is migrated from Parser.php in MediwWiki 1.5.0.
 *
 * created at 2006/04/07 by
 * @author Mingli Yuan
 *
 */
public class TableProcessor extends AbstractWikiProcessor {

    private Pattern beginPattern =
        Pattern.compile("^(:*)\\{\\|(.*)$", Pattern.DOTALL);//$NON-NLS-1$

    /*
     * process the wiki text
     * @see org.dajoo.render.WikiProcessor#process(java.lang.String)
     */
    public String process(String text) {
        Stack<Boolean> tdStack = new Stack<Boolean>();//Is currently a td tag open?
        Stack<String> ltdStack = new Stack<String>();//Was it TD or TH?
        Stack<Boolean> trStack = new Stack<Boolean>();//Is currently a tr tag open?
        Stack<String> ltrStack = new Stack<String>();//tr attributes
        int indentLevel = 0;//indent level of the table

        String newText = "";//$NON-NLS-1$
        StringTokenizer tokens = new StringTokenizer(text, "\r\n");//$NON-NLS-1$
        while(tokens.hasMoreTokens()) {
            String line = tokens.nextToken();
            line = line.trim();
            if(!line.equals("")) {//$NON-NLS-1$
                char firstChar = line.charAt(0);
                Matcher beginMatcher = beginPattern.matcher(line);
                if (beginMatcher.matches()) {
                    indentLevel = beginMatcher.group(1).length();
                    String attributes = beginMatcher.group(2);
                    newText += ( openDldd(indentLevel) + "<table " + attributes + ">\r\n" );//$NON-NLS-1$ //$NON-NLS-2$
                    tdStack.push(false);
                    ltdStack.push("");//$NON-NLS-1$
                    trStack.push(false);
                    ltrStack.push("");//$NON-NLS-1$
                } else if(tdStack.size()!=0 && line.length()>=2 && line.substring(0,2).equals("|}")) {//$NON-NLS-1$
                    String temp = ("</table>" + line.substring(2));//$NON-NLS-1$
                    String tag = ltdStack.pop();
                    if ( trStack.pop() ) temp = "</tr>" + temp;//$NON-NLS-1$
                    if ( tdStack.pop() ) temp = ("</" + tag + ">") + temp;//$NON-NLS-1$ //$NON-NLS-2$
                    ltrStack.pop();
                    newText += ( temp + closeDldd(indentLevel) + "\r\n");//$NON-NLS-1$
                } else if(tdStack.size()!=0 && line.length()>=2 && line.substring(0,2).equals("|-")) {//$NON-NLS-1$
                    String attributes = getAttributes(line);
                    String temp = "";//$NON-NLS-1$
                    String tag = ltdStack.pop();
                    if ( tdStack.pop() ) temp += ("</" + tag + ">");//$NON-NLS-1$ //$NON-NLS-2$
                    if ( trStack.pop() ) temp += "</tr>";//$NON-NLS-1$
                    ltrStack.pop();
                    newText += ( temp + "\r\n");//$NON-NLS-1$
                    tdStack.push(false);
                    ltdStack.push("");//$NON-NLS-1$
                    trStack.push(false);
                    ltrStack.push(attributes);//$NON-NLS-1$
                } else if(tdStack.size()!=0 && (firstChar=='|' || firstChar=='!' || line.length()>=2 && line.substring(0,2).equals("|+"))) {//$NON-NLS-1$
                    if (line.length()>=2 && line.substring(0,2).equals("|+")) {//$NON-NLS-1$
                        firstChar = '+';
                        line = line.substring(1);
                    }
                    String after = line.substring(1);
                    if (firstChar=='!') after = after.replace("!!", "||") ;//$NON-NLS-1$ //$NON-NLS-2$
                    List<String> cellList = splitCells(after);

                    Iterator<String> iter = cellList.iterator();
                    while(iter.hasNext()) {

                        String cell = iter.next();
                        String temp = "";//$NON-NLS-1$
                        if (firstChar!='+') {
                            String trAttr = ltrStack.pop();
                            if (!trStack.pop()) temp = "<tr" + trAttr + ">\r\n";//$NON-NLS-1$ //$NON-NLS-2$
                            trStack.push(true);
                            ltrStack.push("");//$NON-NLS-1$
                        }

                        String l = ltdStack.pop();
                        if (tdStack.pop()) temp += "</" + l + ">";//$NON-NLS-1$ //$NON-NLS-2$
                        if (firstChar=='|') l = "td";//$NON-NLS-1$
                        else if (firstChar=='!') l = "th";//$NON-NLS-1$
                        else if (firstChar=='+') l = "caption";//$NON-NLS-1$
                        else l = "";//$NON-NLS-1$
                        ltdStack.push(l);

                        //Cell parameters
                        List<String> y = splitCellAttrAndText(cell);
                        if ( y.size() == 1 ) {
                            temp = temp + "<" + l + ">" + y.get(0);//$NON-NLS-1$ //$NON-NLS-2$
                        } else if( y.size() == 2 ) {
                            String attributes = y.get(0);
                            temp = temp + "<" + l + " " + attributes + ">" + y.get(1);//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        }
                        newText += temp;
                        tdStack.push(true);
                    }
                } else {
                    newText += (line + "\r\n");//$NON-NLS-1$
                }
            }
        }

        //Closing open td, tr && table
        while ( tdStack.size()>0 ) {
            if ( tdStack.pop() ) newText += "</td>\r\n";//$NON-NLS-1$
            if ( trStack.pop() ) newText += "</tr>\r\n";//$NON-NLS-1$
            newText += "</table>\r\n";//$NON-NLS-1$
        }

        return newText;
    }

    private String openDldd(int indentLevel){
        String text = "";//$NON-NLS-1$
        for(int i=0;i<indentLevel;i++)
            text += "<dl><dd>";//$NON-NLS-1$
        return text;
    }

    private String closeDldd(int indentLevel){
        String text = "";//$NON-NLS-1$
        for(int i=0;i<indentLevel;i++)
            text += "</dd></dl>";//$NON-NLS-1$
        return text;
    }

    private String getAttributes(String line) {
        line = line.substring(1);
        while ( !line.equals("") && line.substring(0,1).equals("-")) {//$NON-NLS-1$ //$NON-NLS-2$
            line = line.substring(1);
        }
        return line;
    }

    //Should move to class TextUtil
    private List<String> splitCells(String cellsText) {
        int pos = 0;
        int lastPos = 0;
        int len = cellsText.length();
        List<String> list = new ArrayList<String>();
        while(pos!=-1 && lastPos<len) {
            pos = cellsText.indexOf("||", lastPos);//$NON-NLS-1$
            if(pos!=-1) {
                list.add(cellsText.substring(lastPos, pos));
                lastPos = pos + 2;
            } else {
                list.add(cellsText.substring(lastPos, len));
            }
        }
        return list;
    }

    private List<String> splitCellAttrAndText(String cell) {
        int pos = 0;
        int lastPos = 0;
        int len = cell.length();
        List<String> list = new ArrayList<String>();
        while(pos!=-1 && lastPos<len) {
            pos = cell.indexOf("|", lastPos);//$NON-NLS-1$
            if(pos!=-1) {
                list.add(cell.substring(lastPos, pos));
                lastPos = pos + 1;
                if(lastPos>=len) {
                    list.add("");//$NON-NLS-1$
                }
            } else {
                list.add(cell.substring(lastPos, len));
            }
        }
        return list;
    }

}
