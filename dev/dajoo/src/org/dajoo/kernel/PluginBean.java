/**
 * (X)PluginBean.java
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

import java.util.ArrayList;
import java.util.List;

public class PluginBean {

	private String name;
	private String version;
	private String className;
	private String[] dependencies;

	private boolean loaded = false;
	private List<PluginBean> parents = new ArrayList<PluginBean>();

	private PluginJar jar;

	public PluginBean(String name, String version, String className, String[] dependencies, PluginJar jar) {
		this.name = name;
		this.version = version;
		this.className = className;
		this.dependencies = dependencies;
		this.jar = jar;
	}

    public String getName() {
    	return name;
    }
    public String getVersion() {
    	return version;
    }
    public String getClassName() {
    	return className;
    }

    public String[] getDependencies() {
    	return dependencies;
    }

    public void dependOn(PluginBean anotherBean) {
        parents.add(anotherBean);
    }

    public PluginBean[] getParents() {
    	PluginBean[] parentArray = new PluginBean[parents.size()];
    	return parents.toArray(parentArray);
    }

    public PluginJar getJar() {
    	return jar;
    }

    public boolean isLoaded() {
    	return loaded;
    }

    public void setLoaded(boolean b) {
    	loaded = b;
    }

}
