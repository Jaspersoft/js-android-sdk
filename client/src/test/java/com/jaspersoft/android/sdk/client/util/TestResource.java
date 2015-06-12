/*
* Copyright © 2015 TIBCO Software, Inc. All rights reserved.
* http://community.jaspersoft.com/project/jaspermobile-android
*
* Unless you have purchased a commercial license agreement from Jaspersoft,
* the following license terms apply:
*
* This program is part of Jaspersoft Mobile for Android.
*
* Jaspersoft Mobile is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Jaspersoft Mobile is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with Jaspersoft Mobile for Android. If not, see
* <http://www.gnu.org/licenses/lgpl>.
*/

package com.jaspersoft.android.sdk.client.util;

import org.apache.commons.io.IOUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class TestResource {
    public enum DataFormat {
        JSON, XML
    }

    private final DataFormat mFormat;

    public static TestResource get(DataFormat format) {
        return new TestResource(format);
    }

    public static TestResource getXml() {
        return new TestResource(DataFormat.XML);
    }

    public static TestResource getJson() {
        return new TestResource(DataFormat.JSON);
    }

    private TestResource(DataFormat format) {
        mFormat = format;
    }

    public <T> T from(Class<T> clazz, String fileName) {
        if (mFormat == DataFormat.XML) {
            return fromXML(clazz, fileName);
        }
        if (mFormat == DataFormat.JSON) {
            return fromXML(clazz, fileName);
        }
        throw new UnsupportedOperationException();
    }

    public String rawData(String fileName) {
        InputStream inputStream = getStream(fileName);

        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(inputStream, writer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return writer.toString();
    }

    public InputStream getStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName + "." + mFormat.toString().toLowerCase());
    }

    public byte[] getBytes(String fileName) throws IOException {
        InputStream stream = getStream(fileName);
        try {
            return IOUtils.toByteArray(stream);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public <T> T fromXML(Class<T> clazz, String fileName) {
        InputStream stream = getStream(fileName);

        Serializer serializer = new Persister();
        try {
            return serializer.read(clazz, stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}