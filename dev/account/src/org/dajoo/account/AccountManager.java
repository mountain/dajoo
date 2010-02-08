/**
 * (X)AccountManager.java
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
package org.dajoo.account;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.dajoo.kernel.DajooConfiguration;

public class AccountManager {

    private String confFilePath;

    private Vector<String> siteList = new Vector<String>();
    private Hashtable<String, String> userRepo = new Hashtable<String, String>();
    private Hashtable<String, String> pwdRepo = new Hashtable<String, String>();

    public AccountManager() {
        String dir = DajooConfiguration.getConfiguration("account.dir");
        confFilePath = dir  + File.separatorChar + "account.properties";
        loadAccounts(confFilePath);
    }

    public void addAccount(String site, String user, String password) {
        siteList.add(site);
        userRepo.put(site, user);
        pwdRepo.put(site, password);
        storeAccounts(confFilePath);
    }

    public String getUser(String site) {
        return userRepo.get(site);
    }

    public String getPassword(String site) {
        return pwdRepo.get(site);
    }

    public void removeAccount(String site) {
        siteList.remove(site);
        userRepo.remove(site);
        pwdRepo.remove(site);
        storeAccounts(confFilePath);
    }

    public void loadAccounts(String path) {
        Properties props = new Properties();
        try {
            InputStream ins = new FileInputStream(path);
            props.load(ins);
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator keyIter = props.keySet().iterator();
        while(keyIter.hasNext()) {
            String site = (String)keyIter.next();
            String value = (String)props.get(site);
            String[] pair = value.split("/");
            addAccount(site, pair[0], pair[1]);
        }
    }

    public void storeAccounts(String path) {
        Properties props = new Properties();
        Iterator keyIter = userRepo.keySet().iterator();
        while(keyIter.hasNext()) {
            String site = (String)keyIter.next();
            String user = (String)userRepo.get(site);
            String pwd = (String)pwdRepo.get(site);
            String pair = user + "/" + pwd;
            props.put(site, pair);
        }
        try {
            FileOutputStream out = new FileOutputStream(path);
            props.store(out, "---No Comment---");
            out.close();
        } catch (IOException ioe) {

        }
    }

}
