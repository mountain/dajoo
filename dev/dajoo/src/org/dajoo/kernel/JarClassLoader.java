/**
 * (X)JarClassLoader.java
 *
 * This file is a modefied version of JarClassLoader.java from jEdit.org
 *
 * Copyright (C) 2006-2009 Slava Pestov, Mike Dillon, Mingli Yuan
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

package org.dajoo.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.dajoo.util.MiscUtilities;

/**
 * A class loader implementation that loads classes from JAR files. All
 * instances share the same set of classes.
 *
 * @author Slava Pestov
 * @author Mike Dillon
 * @author Mingli Yuan
 * @version 1.0
 */
public class JarClassLoader extends ClassLoader {

	//{{{ JarClassLoader constructor
	/**
	 * This constructor creates a class loader for loading classes from all
	 * plugins. For example BeanShell uses one of these so that scripts can
	 * use plugin classes.
	 */
	public JarClassLoader()
	{
		// for debugging
		id = INDEX++;
		live++;
	} //}}}

	//{{{ loadClass() method
	/**
	 * @exception ClassNotFoundException if the class could not be found
	 */
	public Class<?> loadClass(String clazz, boolean resolveIt)
		throws ClassNotFoundException
	{
		// see what JarClassLoader this class is in
		Object obj = classHash.get(clazz);
		if(obj == NO_CLASS)
		{
			// we remember which classes we don't exist
			// because BeanShell tries loading all possible
			// <imported prefix>.<class name> combinations
			throw new ClassNotFoundException(clazz);
		}
		else if(obj instanceof JarClassLoader)
		{
			JarClassLoader classLoader = (JarClassLoader)obj;
			return classLoader._loadClass(clazz,resolveIt);
		}

		// if it's not in the class hash, and not marked as
		// non-existent, try loading it from the CLASSPATH

		try
		{
			Class cls;

			/* Defer to whoever loaded us (such as JShell,
			 * Echidna, etc) */
			ClassLoader parentLoader = getClass().getClassLoader();
			if (parentLoader != null)
				cls = parentLoader.loadClass(clazz);
			else
				cls = findSystemClass(clazz);

			return cls;
		}
		catch(ClassNotFoundException cnf)
		{
			// remember that this class doesn't exist for
			// future reference
			classHash.put(clazz,NO_CLASS);

			throw cnf;
		}

	} //}}}

	//{{{ getResourceAsStream() method
	public InputStream getResourceAsStream(String name)
	{
		if(jar == null)
			return null;

		try
		{
			JarFile jarFile = jar.getJarFile();
			JarEntry entry = jarFile.getJarEntry(name);
			if(entry == null)
				return getSystemResourceAsStream(name);
			else
				return jarFile.getInputStream(entry);
		}
		catch(IOException io)
		{
			logger.error(this,io);

			return null;
		}
	} //}}}

	//{{{ dump() method
	/**
	 * For debugging.
	 */
	public static void dump()
	{
		logger.debug("Total instances created: " + INDEX);
		logger.debug("Live instances: " + live);
		synchronized(classHash)
		{
			Iterator entries = classHash.entrySet().iterator();
			while(entries.hasNext())
			{
				Map.Entry entry = (Map.Entry)entries.next();
				if(entry.getValue() != NO_CLASS)
				{
					logger.debug(entry.getKey() + " ==> "
						+ entry.getValue());
				}
			}
		}
	} //}}}

	//{{{ toString() method
	public String toString()
	{
		if(jar == null)
			return "<anonymous>(" + id + ")";
		else
			return jar.getPath() + " (" + id + ")";
	} //}}}

	//{{{ finalize() method
	protected void finalize()
	{
		live--;
	} //}}}

	//{{{ Package-private members

	//{{{ JarClassLoader constructor
	JarClassLoader(PluginJar jar)
	{
		this();
		this.jar = jar;
	} //}}}

	//{{{ activate() method
	void activate() throws IOException
	{
		String[] classes = jar.getClasses();
		if(classes != null)
		{
			for(int i=0;i<classes.length;i++) {
				String clazz = classes[i];
				if(clazz!=null)
				    classHash.put(clazz,this);
			}
		}
	} //}}}

	//{{{ deactivate() method
	void deactivate() throws IOException
	{
		String[] classes = jar.getClasses();
		if(classes != null)
		{
			for(int i=0;i<classes.length;i++) {
				String clazz = classes[i];
				if(clazz!=null) {
					Object loader = classHash.get(clazz);
					if(loader == this)
						classHash.remove(clazz);
					else
						/* two plugins provide same class! */;
				}
			}
		}

	} //}}}

	//}}}

	//{{{ Private members

	// used to mark non-existent classes in class hash
	private static final Object NO_CLASS = new Object();

	private static int INDEX;
	private static int live;
	private static Hashtable<String, Object> classHash = new Hashtable<String, Object>();
	private static Logger logger = Logger.getLogger(JarClassLoader.class);

	private int id;
	private PluginJar jar;

	//{{{ _loadClass() method
	/**
	 * Load class from this JAR only.
	 */
	private synchronized Class _loadClass(String clazz, boolean resolveIt)
		throws ClassNotFoundException
	{
		//jar.activatePlugin();

		synchronized(this)
		{
			Class cls = findLoadedClass(clazz);
			if(cls != null)
			{
				if(resolveIt)
					resolveClass(cls);
				return cls;
			}

			String name = MiscUtilities.classToFile(clazz);

			try
			{
				JarFile jarFile = jar.getJarFile();
				JarEntry entry = jarFile.getJarEntry(name);

				if(entry == null)
					throw new ClassNotFoundException(clazz);

				InputStream in = jarFile.getInputStream(entry);

				int len = (int)entry.getSize();
				byte[] data = new byte[len];
				int success = 0;
				int offset = 0;
				while(success < len)
				{
					len -= success;
					offset += success;
					success = in.read(data,offset,len);
					if(success == -1)
					{
						logger.error("Failed to load class "
							+ clazz + " from " + jarFile.getName());
						throw new ClassNotFoundException(clazz);
					}
				}

				cls = defineClass(clazz,data,0,data.length);

				if(resolveIt)
					resolveClass(cls);

				return cls;
			}
			catch(IOException io)
			{
				logger.error(this,io);

				throw new ClassNotFoundException(clazz);
			}
		}
	} //}}}

	//}}}

}
