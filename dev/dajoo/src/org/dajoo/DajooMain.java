/**
 * (X)DajooMain.java
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
package org.dajoo;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.dajoo.kernel.Daemon;
import org.dajoo.kernel.DajooConfiguration;
import org.dajoo.kernel.PluginRegister;
import org.dajoo.kernel.PluginRegisterImpl;
import org.dajoo.kernel.PluginLoader;
import org.dajoo.kernel.PluginLoaderImpl;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.alternatives.CachingPicoContainer;

/**
 * The main entry point of the whole Dajoo Agent.
 *
 * @author Mingli Yuan
 * @version 1.0
 *
 */
public class DajooMain implements ActionListener{

    private Daemon daemon;

    private MutablePicoContainer pico;

    private TrayIcon trayIcon;
    private PopupMenu popup;
    private MenuItem startItem;
    private MenuItem shutdownItem;
    private MenuItem exitItem;
    private MenuItem aboutItem;

    public DajooMain() {
    	initialize();
        buildTrayIcon();
        loadPlugins();
    }

    private void initialize() {
    	pico = new CachingPicoContainer();

    	DajooConfiguration.initialize();
    	DajooConfiguration.setContainer(pico);

        PropertyConfigurator.configure(DajooConfiguration.getConfiguration("kernel.dir.conf") + File.separatorChar + "logger.ini");
    }

    private void buildTrayIcon() {

        if (!SystemTray.isSupported()) return;

        SystemTray tray = SystemTray.getSystemTray();
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(DajooConfiguration.getConfiguration("kernel.dir.images") + File.separatorChar + "logo.png");

        popup = new PopupMenu();
        startItem = new MenuItem("start");
        startItem.setEnabled(false);
        startItem.addActionListener(this);
        popup.add(startItem);
        shutdownItem =  new MenuItem("shutdown");
        shutdownItem.addActionListener(this);
        popup.add(shutdownItem);
        exitItem = new MenuItem("exit");
        exitItem.addActionListener(this);
        popup.add(exitItem);
        aboutItem = new MenuItem("about");
        aboutItem.addActionListener(this);
        popup.add(aboutItem);

        trayIcon = new TrayIcon(image, "Dajoo", popup);
        trayIcon.addActionListener(this);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }

    }

    private void loadPlugins() {
        try {

            pico.registerComponentImplementation(PluginLoader.class, PluginLoaderImpl.class);
            PluginLoader pluginsLoader = (PluginLoader) pico.getComponentInstance(PluginLoader.class);;
        	pluginsLoader.loadPluginJar();
        	pluginsLoader.buidDependencies();
            Class[] pluginClasses = pluginsLoader.loadPluginClasses();
        	for(int i=0;i<pluginClasses.length;i++) {
        	    pico.registerComponentImplementation(pluginClasses[i]);
        	}

            pico.registerComponentImplementation(PluginRegister.class, PluginRegisterImpl.class);
            PluginRegister assembler = (PluginRegister) pico.getComponentInstance(PluginRegister.class);
            assembler.register();
            assembler.start();

            daemon = (Daemon)pico.getComponentInstance(Daemon.class);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        String action = e.getActionCommand();
        if(action.equals("start")) {
            boolean successful = false;
            try {
                daemon.run();
                successful = true;
            } catch (Exception err) {
                err.printStackTrace();
            }
            if(successful) startItem.setEnabled(false);
        } else if(action.equals("exit")) {
            if(daemon!=null) daemon.stop();
            SystemTray tray = SystemTray.getSystemTray();
            tray.remove(trayIcon);
            System.exit(0);
        } else if(action.equals("about")) {
            //Do nothing
        }
    }

    public Daemon getServer() { return daemon; }
    public TrayIcon getTrayIcon() { return trayIcon; }

    public static void main(String[] args) {
        new DajooMain();
    }
}
