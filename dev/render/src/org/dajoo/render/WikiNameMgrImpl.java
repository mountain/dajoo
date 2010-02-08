/**
 * (X)WikiNameMgrImpl.java
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
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.dajoo.kernel.DajooConfiguration;

/**
 * @author Mingli Yuan
 *
 */
public class WikiNameMgrImpl implements WikiNameManager {

    private static Logger logger = Logger.getLogger(WikiNameMgrImpl.class);

    private HashSet<String> wikiNames;

    /*
     * intialization the processor
     * @Override
     */
    public void start() {
        wikiNames = new HashSet<String>();
        File wikiRoot = new File(DajooConfiguration.getConfiguration("render.dir"));
        addWikiNames(wikiRoot);
    }

    /* (non-Javadoc)
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        //Do nothing
    }

    public void addName(String name) {
        name = normalizeName(name);
        logger.debug("add into repository: " + name);
        wikiNames.add(name);
    }

    public void removeName(String name) {
        name = normalizeName(name);
        logger.debug("remove from repository: " + name);
        wikiNames.remove(name);
    }

    /* (non-Javadoc)
     * @see org.dajoo.render.WikiNameManager#contains(java.lang.String)
     */
    public boolean contains(String name) {
        name = normalizeName(name);
        logger.debug("query from repository: " + name);
        return wikiNames.contains(name);
    }

    private void addWikiNames(File file) {
        if(!file.isDirectory()) {
            String rootPath = DajooConfiguration.getConfiguration("render.dir");
            int rootPathLen = rootPath.length();
            String path = file.getPath();
            int p = path.lastIndexOf(".txt");
            if( p>0 ) {
                String relativePath = path.substring(0, p).substring(rootPathLen+1);
                String name = relativePath.replace(File.separatorChar, ':');
                addName(name);
            }
        } else {
            File[] subfiles = file.listFiles();
            for(int i=0; i<subfiles.length; i++) {
                addWikiNames(subfiles[i]);
            }
        }
    }

    public String normalizeName(String name) {
        if(name==null) return null;
        name = name.trim();
        while(true) {
            if(name.startsWith(":"))
                name = name.substring(1);
            else
                break;
        }
        String[] segments = name.split(":");
        name = "";
        int len = segments.length;
        for(int i=0;i<len;i++) {
            name += capitalizeName(segments[i]);
            if(i!=len-1)name += ":";
        }
        name = name.replace(" ", "_");
        return name;
    }

    private String capitalizeName(String name) {
        if(name==null)
            return null;
        else if(name.length()==0)
            return "";
        else
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

}
