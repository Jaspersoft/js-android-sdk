/*
* Copyright © 2014 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.client.test.support;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public final class TestResources {
    private TestResources() {
    }

    private static class Holder {
        private static final TestResources INSTANCE = new TestResources();
    }

    public <T> T fromXML(Class<T> clazz, String fileName) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName + ".xml");

        Serializer serializer = new Persister();
        try {
            return serializer.read(clazz, stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static TestResources get() {
        return Holder.INSTANCE;
    }
}
