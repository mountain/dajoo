/**
 * (X)MiscUtilities.java
 *
 * This file is a modefied version of MiscUtilities.java from jEdit.org
 *
 * Copyright (C) 1999, 2004 Slava Pestov
 * Portions copyright (C) 2000 Richard S. Hall
 * Portions copyright (C) 2001 Dirk Moebius
 *
 * http://www.jedit.org/
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
package org.dajoo.util;
//{{{ Imports
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.text.Segment;
//}}}

/**
 * Path name manipulation, string manipulation, and more.<p>
 *
 * The most frequently used members of this class are:<p>
 *
 * <b>Some path name methods:</b><p>
 * <ul>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getParentOfPath(String)}</li>
 * <li>{@link #constructPath(String,String)}</li>
 * </ul>
 * <b>String comparison:</b><p>

 * A {@link #compareStrings(String,String,boolean)} method that unlike
 * <function>String.compareTo()</function>, correctly recognizes and handles
 * embedded numbers.<p>
 *
 * This class also defines several inner classes for use with the
 * sorting features of the Java collections API:
 *
 * <ul>
 * <li>{@link MiscUtilities.StringCompare}</li>
 * <li>{@link MiscUtilities.StringICaseCompare}</li>
 * <li>{@link MiscUtilities.MenuItemCompare}</li>
 * </ul>
 *
 * For example, you might call:<p>
 *
 * <code>Arrays.sort(myListOfStrings,
 *     new MiscUtilities.StringICaseCompare());</code>
 *
 * @author Slava Pestov
 * @author John Gellene (API documentation)
 * @version $Id: MiscUtilities.java,v 1.77 2004/08/21 01:49:05 spestov Exp $
 */
public class MiscUtilities
{
    /**
     * This encoding is not supported by Java, yet it is useful.
     * A UTF-8 file that begins with 0xEFBBBF.
     */
    public static final String UTF_8_Y = "UTF-8Y";

    //{{{ Path name methods

    //{{{ getFileExtension() method
    /**
     * Returns the extension of the specified filename, or an empty
     * string if there is none.
     * @param name The file name or path
     */
    public static String getFileExtension(String name)
    {
        int fsIndex = Math.max(name.indexOf('/'),
            name.indexOf(File.separatorChar));
        int index = name.indexOf('.',fsIndex);
        if(index == -1)
            return "";
        else
            return name.substring(index+1);
    } //}}}

    //{{{ saveBackup() method
    /**
     * Saves a backup (optionally numbered) of a file.
     * @param file A local file
     * @param backups The number of backups. Must be >= 1. If > 1, backup
     * files will be numbered.
     * @param backupPrefix The backup file name prefix
     * @param backupSuffix The backup file name suffix
     * @param backupDirectory The directory where to save backups; if null,
     * they will be saved in the same directory as the file itself.
     * @since jEdit 4.0pre1
     */
    public static void saveBackup(File file, int backups,
        String backupPrefix, String backupSuffix,
        String backupDirectory)
    {
        saveBackup(file,backups,backupPrefix,backupSuffix,backupDirectory,0);
    } //}}}

    //{{{ saveBackup() method
    /**
     * Saves a backup (optionally numbered) of a file.
     * @param file A local file
     * @param backups The number of backups. Must be >= 1. If > 1, backup
     * files will be numbered.
     * @param backupPrefix The backup file name prefix
     * @param backupSuffix The backup file name suffix
     * @param backupDirectory The directory where to save backups; if null,
     * they will be saved in the same directory as the file itself.
     * @param backupTimeDistance The minimum time in minutes when a backup
     * version 1 shall be moved into version 2; if 0, backups are always
     * moved.
     * @since jEdit 4.2pre5
     */
    public static void saveBackup(File file, int backups,
        String backupPrefix, String backupSuffix,
        String backupDirectory, int backupTimeDistance)
    {
        if(backupPrefix == null)
            backupPrefix = "";
        if(backupSuffix == null)
            backupSuffix = "";

        String name = file.getName();

        // If backups is 1, create ~ file
        if(backups == 1)
        {
            File backupFile = new File(backupDirectory,
                backupPrefix + name + backupSuffix);
            long modTime = backupFile.lastModified();
            /* if backup file was created less than
             * 'backupTimeDistance' ago, we do not
             * create the backup */
            if(System.currentTimeMillis() - modTime
                >= backupTimeDistance)
            {
                backupFile.delete();
                file.renameTo(backupFile);
            }
        }
        // If backups > 1, move old ~n~ files, create ~1~ file
        else
        {
            /* delete a backup created using above method */
            new File(backupDirectory,
                backupPrefix + name + backupSuffix
                + backups + backupSuffix).delete();

            File firstBackup = new File(backupDirectory,
                backupPrefix + name + backupSuffix
                + "1" + backupSuffix);
            long modTime = firstBackup.lastModified();
            /* if backup file was created less than
             * 'backupTimeDistance' ago, we do not
             * create the backup */
            if(System.currentTimeMillis() - modTime
                >= backupTimeDistance)
            {
                for(int i = backups - 1; i > 0; i--)
                {
                    File backup = new File(backupDirectory,
                        backupPrefix + name
                        + backupSuffix + i
                        + backupSuffix);

                    backup.renameTo(
                        new File(backupDirectory,
                        backupPrefix + name
                        + backupSuffix + (i+1)
                        + backupSuffix));
                }

                file.renameTo(new File(backupDirectory,
                    backupPrefix + name + backupSuffix
                    + "1" + backupSuffix));
            }
        }
    } //}}}

    //{{{ fileToClass() method
    /**
     * Converts a file name to a class name. All slash characters are
     * replaced with periods and the trailing '.class' is removed.
     * @param name The file name
     */
    public static String fileToClass(String name)
    {
        char[] clsName = name.toCharArray();
        for(int i = clsName.length - 6; i >= 0; i--)
            if(clsName[i] == '/')
                clsName[i] = '.';
        return new String(clsName,0,clsName.length - 6);
    } //}}}

    //{{{ classToFile() method
    /**
     * Converts a class name to a file name. All periods are replaced
     * with slashes and the '.class' extension is added.
     * @param name The class name
     */
    public static String classToFile(String name)
    {
        return name.replace('.','/').concat(".class");
    } //}}}

    //}}}

    //{{{ Text methods

    //{{{ getLeadingWhiteSpace() method
    /**
     * Returns the number of leading white space characters in the
     * specified string.
     * @param str The string
     */
    public static int getLeadingWhiteSpace(String str)
    {
        int whitespace = 0;
loop:        for(;whitespace < str.length();)
        {
            switch(str.charAt(whitespace))
            {
            case ' ': case '\t':
                whitespace++;
                break;
            default:
                break loop;
            }
        }
        return whitespace;
    } //}}}

    //{{{ getTrailingWhiteSpace() method
    /**
     * Returns the number of trailing whitespace characters in the
     * specified string.
     * @param str The string
     * @since jEdit 2.5pre5
     */
    public static int getTrailingWhiteSpace(String str)
    {
        int whitespace = 0;
loop:        for(int i = str.length() - 1; i >= 0; i--)
        {
            switch(str.charAt(i))
            {
            case ' ': case '\t':
                whitespace++;
                break;
            default:
                break loop;
            }
        }
        return whitespace;
    } //}}}

    //{{{ getLeadingWhiteSpaceWidth() method
    /**
     * Returns the width of the leading white space in the specified
     * string.
     * @param str The string
     * @param tabSize The tab size
     */
    public static int getLeadingWhiteSpaceWidth(String str, int tabSize)
    {
        int whitespace = 0;
loop:        for(int i = 0; i < str.length(); i++)
        {
            switch(str.charAt(i))
            {
            case ' ':
                whitespace++;
                break;
            case '\t':
                whitespace += (tabSize - whitespace % tabSize);
                break;
            default:
                break loop;
            }
        }
        return whitespace;
    } //}}}

    //{{{ getVirtualWidth() method
    /**
     * Returns the virtual column number (taking tabs into account) of the
     * specified offset in the segment.
     *
     * @param seg The segment
     * @param tabSize The tab size
     * @since jEdit 4.1pre1
     */
    public static int getVirtualWidth(Segment seg, int tabSize)
    {
        int virtualPosition = 0;

        for (int i = 0; i < seg.count; i++)
        {
            char ch = seg.array[seg.offset + i];

            if (ch == '\t')
            {
                virtualPosition += tabSize
                    - (virtualPosition % tabSize);
            }
            else
            {
                ++virtualPosition;
            }
        }

        return virtualPosition;
    } //}}}

    //{{{ getOffsetOfVirtualColumn() method
    /**
     * Returns the array offset of a virtual column number (taking tabs
     * into account) in the segment.
     *
     * @param seg The segment
     * @param tabSize The tab size
     * @param column The virtual column number
     * @param totalVirtualWidth If this array is non-null, the total
     * virtual width will be stored in its first location if this method
     * returns -1.
     *
     * @return -1 if the column is out of bounds
     *
     * @since jEdit 4.1pre1
     */
    public static int getOffsetOfVirtualColumn(Segment seg, int tabSize,
        int column, int[] totalVirtualWidth)
    {
        int virtualPosition = 0;

        for (int i = 0; i < seg.count; i++)
        {
            char ch = seg.array[seg.offset + i];

            if (ch == '\t')
            {
                int tabWidth = tabSize
                    - (virtualPosition % tabSize);
                if(virtualPosition >= column)
                    return i;
                else
                    virtualPosition += tabWidth;
            }
            else
            {
                if(virtualPosition >= column)
                    return i;
                else
                    ++virtualPosition;
            }
        }

        if(totalVirtualWidth != null)
            totalVirtualWidth[0] = virtualPosition;
        return -1;
    } //}}}

    //{{{ createWhiteSpace() method
    /**
     * Creates a string of white space with the specified length.<p>
     *
     * To get a whitespace string tuned to the current buffer's
     * settings, call this method as follows:
     *
     * <pre>myWhitespace = MiscUtilities.createWhiteSpace(myLength,
     *     (buffer.getBooleanProperty("noTabs") ? 0
     *     : buffer.getTabSize()));</pre>
     *
     * @param len The length
     * @param tabSize The tab size, or 0 if tabs are not to be used
     */
    public static String createWhiteSpace(int len, int tabSize)
    {
        return createWhiteSpace(len,tabSize,0);
    } //}}}

    //{{{ createWhiteSpace() method
    /**
     * Creates a string of white space with the specified length.<p>
     *
     * To get a whitespace string tuned to the current buffer's
     * settings, call this method as follows:
     *
     * <pre>myWhitespace = MiscUtilities.createWhiteSpace(myLength,
     *     (buffer.getBooleanProperty("noTabs") ? 0
     *     : buffer.getTabSize()));</pre>
     *
     * @param len The length
     * @param tabSize The tab size, or 0 if tabs are not to be used
     * @param start The start offset, for tab alignment
     * @since jEdit 4.2pre1
     */
    public static String createWhiteSpace(int len, int tabSize, int start)
    {
        StringBuffer buf = new StringBuffer();
        if(tabSize == 0)
        {
            while(len-- > 0)
                buf.append(' ');
        }
        else if(len == 1)
            buf.append(' ');
        else
        {
            int count = (len + start % tabSize) / tabSize;
            if(count != 0)
                len += start;
            while(count-- > 0)
                buf.append('\t');
            count = len % tabSize;
            while(count-- > 0)
                buf.append(' ');
        }
        return buf.toString();
    } //}}}

    //{{{ globToRE() method
    /**
     * Converts a Unix-style glob to a regular expression.<p>
     *
     * ? becomes ., * becomes .*, {aa,bb} becomes (aa|bb).
     * @param glob The glob pattern
     */
    public static String globToRE(String glob)
    {
        final Object NEG = new Object();
        final Object GROUP = new Object();
        Stack<Object> state = new Stack<Object>();

        StringBuffer buf = new StringBuffer();
        boolean backslash = false;

        for(int i = 0; i < glob.length(); i++)
        {
            char c = glob.charAt(i);
            if(backslash)
            {
                buf.append('\\');
                buf.append(c);
                backslash = false;
                continue;
            }

            switch(c)
            {
            case '\\':
                backslash = true;
                break;
            case '?':
                buf.append('.');
                break;
            case '.':
            case '+':
            case '(':
            case ')':
                buf.append('\\');
                buf.append(c);
                break;
            case '*':
                buf.append(".*");
                break;
            case '|':
                if(backslash)
                    buf.append("\\|");
                else
                    buf.append('|');
                break;
            case '{':
                buf.append('(');
                if(i + 1 != glob.length() && glob.charAt(i + 1) == '!')
                {
                    buf.append('?');
                    state.push(NEG);
                }
                else
                    state.push(GROUP);
                break;
            case ',':
                if(!state.isEmpty() && state.peek() == GROUP)
                    buf.append('|');
                else
                    buf.append(',');
                break;
            case '}':
                if(!state.isEmpty())
                {
                    buf.append(")");
                    if(state.pop() == NEG)
                        buf.append(".*");
                }
                else
                    buf.append('}');
                break;
            default:
                buf.append(c);
            }
        }

        return buf.toString();
    } //}}}

    //{{{ escapesToChars() method
    /**
     * Converts "\n" and "\t" escapes in the specified string to
     * newlines and tabs.
     * @param str The string
     * @since jEdit 2.3pre1
     */
    public static String escapesToChars(String str)
    {
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            switch(c)
            {
            case '\\':
                if(i == str.length() - 1)
                {
                    buf.append('\\');
                    break;
                }
                c = str.charAt(++i);
                switch(c)
                {
                case 'n':
                    buf.append('\n');
                    break;
                case 't':
                    buf.append('\t');
                    break;
                default:
                    buf.append(c);
                    break;
                }
                break;
            default:
                buf.append(c);
            }
        }
        return buf.toString();
    } //}}}

    //{{{ charsToEscapes() method
    /**
     * Escapes newlines, tabs, backslashes, and quotes in the specified
     * string.
     * @param str The string
     * @since jEdit 2.3pre1
     */
    public static String charsToEscapes(String str)
    {
        return charsToEscapes(str,"\n\t\\\"'");
    } //}}}

    //{{{ charsToEscapes() method
    /**
     * Escapes the specified characters in the specified string.
     * @param str The string
     * @param toEscape Any characters that require escaping
     * @since jEdit 4.1pre3
     */
    public static String charsToEscapes(String str, String toEscape)
    {
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if(toEscape.indexOf(c) != -1)
            {
                if(c == '\n')
                    buf.append("\\n");
                else if(c == '\t')
                    buf.append("\\t");
                else
                {
                    buf.append('\\');
                    buf.append(c);
                }
            }
            else
                buf.append(c);
        }
        return buf.toString();
    } //}}}

    //{{{ compareVersions() method
    /**
     * @deprecated Call <code>compareStrings()</code> instead
     */
    public static int compareVersions(String v1, String v2)
    {
        return compareStrings(v1,v2,false);
    } //}}}

    //{{{ compareStrings() method
    /**
     * Compares two strings.<p>
     *
     * Unlike <function>String.compareTo()</function>,
     * this method correctly recognizes and handles embedded numbers.
     * For example, it places "My file 2" before "My file 10".<p>
     *
     * @param str1 The first string
     * @param str2 The second string
     * @param ignoreCase If true, case will be ignored
     * @return negative If str1 &lt; str2, 0 if both are the same,
     * positive if str1 &gt; str2
     * @since jEdit 4.0pre1
     */
    public static int compareStrings(String str1, String str2, boolean ignoreCase)
    {
        char[] char1 = str1.toCharArray();
        char[] char2 = str2.toCharArray();

        int len = Math.min(char1.length,char2.length);

        for(int i = 0, j = 0; i < len && j < len; i++, j++)
        {
            char ch1 = char1[i];
            char ch2 = char2[j];
            if(Character.isDigit(ch1) && Character.isDigit(ch2)
                && ch1 != '0' && ch2 != '0')
            {
                int _i = i + 1;
                int _j = j + 1;

                for(; _i < char1.length; _i++)
                {
                    if(!Character.isDigit(char1[_i]))
                    {
                        //_i--;
                        break;
                    }
                }

                for(; _j < char2.length; _j++)
                {
                    if(!Character.isDigit(char2[_j]))
                    {
                        //_j--;
                        break;
                    }
                }

                int len1 = _i - i;
                int len2 = _j - j;
                if(len1 > len2)
                    return 1;
                else if(len1 < len2)
                    return -1;
                else
                {
                    for(int k = 0; k < len1; k++)
                    {
                        ch1 = char1[i + k];
                        ch2 = char2[j + k];
                        if(ch1 != ch2)
                            return ch1 - ch2;
                    }
                }

                i = _i - 1;
                j = _j - 1;
            }
            else
            {
                if(ignoreCase)
                {
                    ch1 = Character.toLowerCase(ch1);
                    ch2 = Character.toLowerCase(ch2);
                }

                if(ch1 != ch2)
                    return ch1 - ch2;
            }
        }

        return char1.length - char2.length;
    } //}}}

    //{{{ stringsEqual() method
    /**
     * @deprecated Call <code>objectsEqual()</code> instead.
     */
    public static boolean stringsEqual(String s1, String s2)
    {
        return objectsEqual(s1,s2);
    } //}}}

    //{{{ objectsEqual() method
    /**
     * Returns if two strings are equal. This correctly handles null pointers,
     * as opposed to calling <code>o1.equals(o2)</code>.
     * @since jEdit 4.2pre1
     */
    public static boolean objectsEqual(Object o1, Object o2)
    {
        if(o1 == null)
        {
            if(o2 == null)
                return true;
            else
                return false;
        }
        else if(o2 == null)
            return false;
        else
            return o1.equals(o2);
    } //}}}

    //{{{ charsToEntities() method
    /**
     * Converts &lt;, &gt;, &amp; in the string to their HTML entity
     * equivalents.
     * @param str The string
     * @since jEdit 4.2pre1
     */
    public static String charsToEntities(String str)
    {
        StringBuffer buf = new StringBuffer(str.length());
        for(int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            switch(ch)
            {
            case '<':
                buf.append("&lt;");
                break;
            case '>':
                buf.append("&gt;");
                break;
            case '&':
                buf.append("&amp;");
                break;
            default:
                buf.append(ch);
                break;
            }
        }
        return buf.toString();
    } //}}}

    //{{{ formatFileSize() method
    public static final DecimalFormat KB_FORMAT = new DecimalFormat("#.# KB");
    public static final DecimalFormat MB_FORMAT = new DecimalFormat("#.# MB");

    /**
     * Formats the given file size into a nice string (123 bytes, 10.6 KB,
     * 1.2 MB).
     * @param length The size
     * @since jEdit 4.2pre1
     */
    public static String formatFileSize(long length)
    {
        if(length < 1024)
            return length + " bytes";
        else if(length < 1024*1024)
            return KB_FORMAT.format((double)length / 1024);
        else
            return MB_FORMAT.format((double)length / 1024 / 1024);
    } //}}}

    //{{{ getLongestPrefix() method
    /**
     * Returns the longest common prefix in the given set of strings.
     * @param str The strings
     * @param ignoreCase If true, case insensitive
     * @since jEdit 4.2pre2
     */
    public static String getLongestPrefix(List str, boolean ignoreCase)
    {
        if(str.size() == 0)
            return "";

        int prefixLength = 0;

loop:        for(;;)
        {
            String s = str.get(0).toString();
            if(prefixLength >= s.length())
                break loop;
            char ch = s.charAt(prefixLength);
            for(int i = 1; i < str.size(); i++)
            {
                s = str.get(i).toString();
                if(prefixLength >= s.length())
                    break loop;
                if(!compareChars(s.charAt(prefixLength),ch,ignoreCase))
                    break loop;
            }
            prefixLength++;
        }

        return str.get(0).toString().substring(0,prefixLength);
    } //}}}

    //{{{ getLongestPrefix() method
    /**
     * Returns the longest common prefix in the given set of strings.
     * @param str The strings
     * @param ignoreCase If true, case insensitive
     * @since jEdit 4.2pre2
     */
    public static String getLongestPrefix(String[] str, boolean ignoreCase)
    {
        return getLongestPrefix((Object[])str,ignoreCase);
    } //}}}

    //{{{ getLongestPrefix() method
    /**
     * Returns the longest common prefix in the given set of strings.
     * @param str The strings (calls <code>toString()</code> on each object)
     * @param ignoreCase If true, case insensitive
     * @since jEdit 4.2pre6
     */
    public static String getLongestPrefix(Object[] str, boolean ignoreCase)
    {
        if(str.length == 0)
            return "";

        int prefixLength = 0;

        String first = str[0].toString();

loop:        for(;;)
        {
            if(prefixLength >= first.length())
                break loop;
            char ch = first.charAt(prefixLength);
            for(int i = 1; i < str.length; i++)
            {
                String s = str[i].toString();
                if(prefixLength >= s.length())
                    break loop;
                if(!compareChars(s.charAt(prefixLength),ch,ignoreCase))
                    break loop;
            }
            prefixLength++;
        }

        return first.substring(0,prefixLength);
    } //}}}

    //}}}

    //{{{ Sorting methods

    //{{{ quicksort() method
    /**
     * Sorts the specified array. Equivalent to calling
     * <code>Arrays.sort()</code>.
     * @param obj The array
     * @param compare Compares the objects
     * @since jEdit 4.0pre4
     */
    public static void quicksort(Object[] obj, Comparator<Object> compare)
    {
        Arrays.sort(obj,compare);
    } //}}}

    //{{{ quicksort() method
    /**
     * Sorts the specified vector.
     * @param vector The vector
     * @param compare Compares the objects
     * @since jEdit 4.0pre4
     */
    public static void quicksort(Vector vector, Comparator<Object> compare)
    {
        Collections.sort(vector,compare);
    } //}}}

    //{{{ quicksort() method
    /**
     * Sorts the specified list.
     * @param list The list
     * @param compare Compares the objects
     * @since jEdit 4.0pre4
     */
    public static void quicksort(List list, Comparator compare)
    {
        Collections.sort(list,compare);
    } //}}}

    //{{{ quicksort() method
    /**
     * Sorts the specified array. Equivalent to calling
     * <code>Arrays.sort()</code>.
     * @param obj The array
     * @param compare Compares the objects
     */
    public static void quicksort(Object[] obj, Compare compare)
    {
        Arrays.sort(obj,compare);
    } //}}}

    //{{{ quicksort() method
    /**
     * Sorts the specified vector.
     * @param vector The vector
     * @param compare Compares the objects
     */
    public static void quicksort(Vector vector, Compare compare)
    {
        Collections.sort(vector,compare);
    } //}}}

    //{{{ Compare interface
    /**
     * An interface for comparing objects. This is a hold-over from
     * they days when jEdit had its own sorting API due to JDK 1.1
     * compatibility requirements. Use <code>java.util.Comparable</code>
     * instead.
     */
    public interface Compare extends Comparator
    {
        int compare(Object obj1, Object obj2);
    } //}}}

    //{{{ StringCompare class
    /**
     * Compares strings.
     */
    public static class StringCompare implements Compare
    {
        public int compare(Object obj1, Object obj2)
        {
            return compareStrings(obj1.toString(),
                obj2.toString(),false);
        }
    } //}}}

    //{{{ StringICaseCompare class
    /**
     * Compares strings ignoring case.
     */
    public static class StringICaseCompare implements Compare
    {
        public int compare(Object obj1, Object obj2)
        {
            return compareStrings(obj1.toString(),
                obj2.toString(),true);
        }
    } //}}}

    //{{{ MenuItemCompare class
    /**
     * Compares menu item labels.
     */
    public static class MenuItemCompare implements Compare
    {
        public int compare(Object obj1, Object obj2)
        {
            return compareStrings(((JMenuItem)obj1).getText(),
                ((JMenuItem)obj2).getText(),true);
        }
    } //}}}

    //}}}

    //{{{ buildToVersion() method
    /**
     * Converts an internal version number (build) into a
     * `human-readable' form.
     * @param build The build
     */
    public static String buildToVersion(String build)
    {
        if(build.length() != 11)
            return "<unknown version: " + build + ">";
        // First 2 chars are the major version number
        int major = Integer.parseInt(build.substring(0,2));
        // Second 2 are the minor number
        int minor = Integer.parseInt(build.substring(3,5));
        // Then the pre-release status
        int beta = Integer.parseInt(build.substring(6,8));
        // Finally the bug fix release
        int bugfix = Integer.parseInt(build.substring(9,11));

        return major + "." + minor
            + (beta != 99 ? "pre" + beta :
            (bugfix != 0 ? "." + bugfix : "final"));
    } //}}}

    //{{{ parsePermissions() method
    /**
     * Parse a Unix-style permission string (rwxrwxrwx).
     * @param s The string (must be 9 characters long).
     * @since jEdit 4.1pre8
     */
    public static int parsePermissions(String s)
    {
        int permissions = 0;

        if(s.length() == 9)
        {
            if(s.charAt(0) == 'r')
                permissions += 0400;
            if(s.charAt(1) == 'w')
                permissions += 0200;
            if(s.charAt(2) == 'x')
                permissions += 0100;
            else if(s.charAt(2) == 's')
                permissions += 04100;
            else if(s.charAt(2) == 'S')
                permissions += 04000;
            if(s.charAt(3) == 'r')
                permissions += 040;
            if(s.charAt(4) == 'w')
                permissions += 020;
            if(s.charAt(5) == 'x')
                permissions += 010;
            else if(s.charAt(5) == 's')
                permissions += 02010;
            else if(s.charAt(5) == 'S')
                permissions += 02000;
            if(s.charAt(6) == 'r')
                permissions += 04;
            if(s.charAt(7) == 'w')
                permissions += 02;
            if(s.charAt(8) == 'x')
                permissions += 01;
            else if(s.charAt(8) == 't')
                permissions += 01001;
            else if(s.charAt(8) == 'T')
                permissions += 01000;
        }

        return permissions;
    } //}}}

    //{{{ throwableToString() method
    /**
     * Returns a string containing the stack trace of the given throwable.
     * @since jEdit 4.2pre6
     */
    public static String throwableToString(Throwable t)
    {
        StringWriter s = new StringWriter();
        t.printStackTrace(new PrintWriter(s));
        return s.toString();
    } //}}}

    //{{{ Private members
    private MiscUtilities() {}

    //{{{ compareChars()
    /** should this be public? */
    private static boolean compareChars(char ch1, char ch2, boolean ignoreCase)
    {
        if(ignoreCase)
            return Character.toUpperCase(ch1) == Character.toUpperCase(ch2);
        else
            return ch1 == ch2;
    } //}}}

    //}}}
}