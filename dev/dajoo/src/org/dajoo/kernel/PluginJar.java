/**
 * (X)PluginJar.java
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

package org.dajoo.kernel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.dajoo.util.MiscUtilities;

public class PluginJar {

	private static Logger logger = Logger.getLogger(PluginJar.class);

	private String path;
	private File file;
	private JarFile jarFile;
	private String[] classes;

	PluginJar(File file) {
		this.path = file.getPath();
		this.file = file;
	}

	/**
	 * Returns the full path name of this plugin's JAR file.
	 */
	public String getPath()	{
		return path;
	}

	/**
	 * Returns a file pointing to the plugin JAR.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns the plugin's JAR file, opening it if necessary.
	 */
	public synchronized JarFile getJarFile() throws IOException {
		if(jarFile == null)
		{
			jarFile = new JarFile(path);
		}
		return jarFile;
	}

    public String[] getClasses() throws IOException {
    	if(classes==null)
    	{
    		Enumeration entries = getJarFile().entries();
    		List<String> classList = new ArrayList<String>();
			logger.debug("loading all classes from " + path);
    		while(entries.hasMoreElements())
    		{
    			JarEntry entry = (JarEntry)entries.nextElement();
    			String name = entry.getName();
    			if(name.endsWith(".class"))
    			{
    				String className = MiscUtilities.fileToClass(name);
    				classList.add(className);
    			}
    		}
    		classes = new String[classList.size()];
    		classes = classList.toArray(classes);
    	}
		return classes;
    }

	@Override
	public String toString() {
		return path;
	}

}
