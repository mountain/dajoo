/**
 * (X)FileContent.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;
import org.dajoo.util.MiscUtilities;

public class FileContent implements Content{

    private static Logger logger = Logger.getLogger(FileContent.class);

    private long length;
    private String type;
    private ByteBuffer content;

    public FileContent(String realPath) throws IOException{
        logger.debug("Try to open file: file = " + realPath);
        File f = new File(realPath);
        if (!f.exists()) throw new IllegalArgumentException();
        length = f.length();
        type = getMimeType(realPath);
        logger.debug("File type: " + type);
        FileChannel channel = new FileInputStream(f).getChannel();
        content = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        channel.close();
    }

    private String getMimeType(String file) {
        String ext = MiscUtilities.getFileExtension(file);
        logger.debug("File extension: " + ext);
        return MimeTypes.getMimeType(ext);
    }

    public long getLength() {
        return length;
    }

    public String getType() {
        return type;
    }

    public ByteBuffer getContent() {
        return content;
    }

}
