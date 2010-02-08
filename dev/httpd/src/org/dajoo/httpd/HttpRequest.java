/**
 * (X)HttpRequest.java
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
package org.dajoo.httpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {

    private static Pattern satrtLinePattern	=
    	Pattern.compile("^([A-Z]+) ([^ ]+) HTTP/([0-9\\.]+)$");
    private static Pattern headerPattern =
    	Pattern.compile("^([^ ]+): (.*)$",Pattern.DOTALL);
    private static Pattern nameValuePairPattern =
    	Pattern.compile("([^&]+)");
    private static Pattern nameValuePattern =
    	Pattern.compile("([^=]+)=([^=]+)");

	private HashMap<String, String> headers=new HashMap<String, String>();
    private HashMap<String, String> attributes = new HashMap<String, String>();

    private String method;
    private String path;
    private String version;

    private ServletContext servletContext;

	public HttpRequest(InputStream is, ServletContext ctx) throws IOException {
		InputStreamReader reader = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(reader);
		parseHeader(br);
		parseBody(br);
		servletContext = ctx;
	}

	private void parseHeader(BufferedReader reader) throws IOException {
		String startLine = reader.readLine();
    	Matcher m = satrtLinePattern.matcher(startLine);
    	if (!m.matches()) throw new IllegalArgumentException("Wrong start line of the request: startLine = " + startLine);
	    method = m.group(1);
	    path = m.group(2);
	    version = m.group(3);
		String header = reader.readLine();
    	while(!"".equals(header)) {
         	m = headerPattern.matcher(header);
        	if (!m.matches()) throw new IllegalArgumentException("Wrong header of the request: header = " + header);
    	    String fieldName = m.group(1);
    	    String fieldValue = m.group(2);
    	    headers.put(fieldName, fieldValue);
    		header = reader.readLine();
    	}
	}

    private void parseBody(BufferedReader reader) throws IOException {

    	String strContenLength = headers.get("Content-Length");
    	String contenType = headers.get("Content-Type");
    	if(strContenLength!=null && contenType!=null) {
        	int contenLength = Integer.parseInt(strContenLength);
        	String body = readRemaining(reader, contenLength).toString();
        	if(contenType.equals("application/x-www-form-urlencoded")) {
            	Matcher pairMatcher = nameValuePairPattern.matcher(body);
            	while(pairMatcher.find()) {
            		String pair = pairMatcher.group(0);
                	Matcher nameValueMatcher = nameValuePattern.matcher(pair);
                	if(!nameValueMatcher.matches()) throw new IllegalArgumentException("Wrong attribute: " + pair);
                	String name = nameValueMatcher.group(1);
                	String value = URLDecoder.decode(nameValueMatcher.group(2), "UTF-8");
                	attributes.put(name, value);
            	}
        	}
    	}

    }

    private StringBuffer readRemaining(BufferedReader reader, int len) throws IOException {
    	StringBuffer sb = new StringBuffer(len);
		int in = 0;
    	while( in!=-1 && len>0) {
    		in = reader.read();
    		char c = (char) in;
    	    sb.append(c);
    	    len--;
    	}
    	return sb;
    }

    public String getMethod() {
    	return method;
    }

    public String getPath() {
    	return path;
    }

    public String getVersion() {
    	return version;
    }

    public String getRequestedResource() {
		return (path.indexOf('?') > 0) ? path.split("?")[0] : path;
	}

	public String getHeader(String key) {
		return (String)headers.get(key);
	}

	public String getAttribute(String key) {
		return (String)attributes.get(key);
	}

	public boolean closeAfterResponse() {
		return getHeader("Connection").charAt(0) == 'c';
	}

	public int getContentLength() {
		String lengthStr = (String) headers.get("Content-Length");
		return (lengthStr != null) ? Integer.parseInt(lengthStr) : 0;
	}

	public long getModifiedSince() throws ServletException{
		String tmp = (String) headers.get("If-Modified-Since");
		DateFormat format = DateFormat.getDateInstance();
		long modifiedSince = 0;
		if(tmp != null) {
			try {
				modifiedSince = format.parse(tmp).getTime();
			} catch (ParseException e) {
				throw new ServletException(e);
			}
		}
		return modifiedSince;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
}
