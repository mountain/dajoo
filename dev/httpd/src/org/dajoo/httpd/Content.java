package org.dajoo.httpd;

import java.nio.ByteBuffer;

public interface Content {

	public long getLength();

	public String getType();

	public ByteBuffer getContent();

}
