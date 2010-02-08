/**
 * (X)StatusCode.java
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

public class StatusCode {

	static StatusCode OK = new StatusCode(200, "OK");
	static StatusCode NOT_MODIFIED = new StatusCode(304, "Not Modified");
	static StatusCode BAD_REQUEST = new StatusCode(400, "Bad Request");
	static StatusCode NOT_FOUND = new StatusCode(404, "Not Found");
	static StatusCode METHOD_NOT_ALLOWED = new StatusCode(405, "Method Not Allowed");

	private int number;
	private String reason;

	private StatusCode(int i, String r) {
		number = i; reason = r;
	}

	public String toString() {
		return number + " " + reason;
	}

}
