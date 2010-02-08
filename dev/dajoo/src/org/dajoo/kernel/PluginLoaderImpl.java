/**
 * (X)PluginLoaderImpl.java
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.dajoo.util.MiscUtilities;

public class PluginLoaderImpl implements PluginLoader {

    private static Logger logger = Logger.getLogger(PluginLoaderImpl.class);

    private HashMap<String, PluginBean> pluginBeans;
    private ArrayList<Class> pluginClasses;

    public PluginLoaderImpl() {
        pluginBeans = new HashMap<String, PluginBean>();
        pluginClasses = new ArrayList<Class>();
    }

    /* (non-Javadoc)
     * @see org.dajoo.kernel.PluginLoader#loadPluginJar()
     */
    public void loadPluginJar() {
        File pluginRoot = new File(
                DajooConfiguration.getConfiguration("kernel.dir.plugins"));
        if(pluginRoot.isDirectory()) {
            File[] subfiles = pluginRoot.listFiles();
            for(int i=0; i<subfiles.length; i++) {
                File subFile = subfiles[i];
                if(!subFile.isDirectory()) {
                    String ext = MiscUtilities.getFileExtension(subFile.getName());
                    if("jar".equals(ext)) addBean(subFile);
                }
            }
        }
    }

    private void addBean(File jarFile) {
        try {

            PluginJar pluginJar = new PluginJar(jarFile);

            Manifest mf = pluginJar.getJarFile().getManifest();
            Attributes section = mf.getAttributes("Plugin");
            String name = section.getValue("Plugin-Name");
            String version = section.getValue("Plugin-Version");
            String className = section.getValue("Plugin-Class");
            String combinedNames = section.getValue("Dependencies");

            String[] dependencies = null;
            if(combinedNames!=null) {
                ArrayList<String> list = new ArrayList<String>();
                StringTokenizer tokens = new StringTokenizer(combinedNames, " ");
                while(tokens.hasMoreTokens())
                    list.add(tokens.nextToken());
                dependencies = new String[list.size()];
                dependencies = list.toArray(dependencies);
            }

            PluginBean bean = new PluginBean(name, version, className, dependencies, pluginJar);
            pluginBeans.put(name, bean);

        } catch(IOException e) {
            logger.error("error on getting the plugin name: jarFile = " + jarFile.getName());
        }
    }

    /* (non-Javadoc)
     * @see org.dajoo.kernel.PluginLoader#buidDependencies()
     */
    public void buidDependencies() throws IOException{
        Iterator<String> iter = pluginBeans.keySet().iterator();
        while(iter.hasNext()) {
            PluginBean bean = pluginBeans.get(iter.next());
            String[] dependencies = bean.getDependencies();
            if(dependencies!=null) {
                for(int i=0;i<dependencies.length;i++) {
                    PluginBean parent = pluginBeans.get(dependencies[i]);
                    bean.dependOn(parent);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.dajoo.kernel.PluginLoader#loadPluginClasses()
     */
    public Class[] loadPluginClasses() throws IOException,ClassNotFoundException{
        Iterator<String> iter = pluginBeans.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            loadPlugin(pluginBeans.get(key));
        }
        Class[] classes = new Class[pluginClasses.size()];
        classes = pluginClasses.toArray(classes);
        return classes;
    }

    private void loadPlugin(PluginBean bean) throws IOException,ClassNotFoundException {
        if(bean.isLoaded()) return;
        PluginBean[] parents = bean.getParents();
        for(int i=0;i<parents.length;i++)
            loadPlugin(parents[i]);
        String clazz = bean.getClassName();
        JarClassLoader classLoader = new JarClassLoader(bean.getJar());
        classLoader.activate();
        logger.info("plugin loading: " + bean.getName());
        Class pluginClass = classLoader.loadClass(clazz, true);
        pluginClasses.add(pluginClass);
        bean.setLoaded(true);
    }
}
