package org.dajoo.kernel;

import java.io.IOException;

public interface PluginLoader {

    public void loadPluginJar();

    public void buidDependencies() throws IOException;

    public Class[] loadPluginClasses() throws IOException,
            ClassNotFoundException;

}