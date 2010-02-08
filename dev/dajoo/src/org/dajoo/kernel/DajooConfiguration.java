/**
 * (X)DajooConfiguration.java
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
import java.util.*;

import org.picocontainer.PicoContainer;

public class DajooConfiguration {

    private static Hashtable<String, String> map = new Hashtable<String, String>();
    private static PicoContainer container;

    public static void initialize() {
        map.put("kernel.dir.base",
                System.getProperty("user.dir") + File.separatorChar);
        map.put("kernel.dir.conf",
                System.getProperty("user.dir") + File.separatorChar + "conf"  + File.separatorChar);
        map.put("kernel.dir.images",
                System.getProperty("user.dir") + File.separatorChar + "images"  + File.separatorChar);
        map.put("kernel.dir.log",
                System.getProperty("user.dir") + File.separatorChar + "log"  + File.separatorChar);
        map.put("kernel.dir.plugins",
                System.getProperty("user.dir") + File.separatorChar + "plugins"  + File.separatorChar);
    }

    public static PicoContainer getContainer() {
        return container;
    }

    public static void setContainer(PicoContainer c) {
        container = c;
    }

    public static String getConfiguration(String key) {
    	return map.get(key);
    }

    public static void setConfiguration(String key, String value) {
    	if(!"kernel.dir.base".equals(key)
    	&& !"kernel.dir.conf".equals(key)
    	&& !"kernel.dir.images".equals(key)
    	&& !"kernel.dir.log".equals(key)
    	&& !"kernel.dir.plugins".equals(key))
    	    map.put(key, value);
    }

}
