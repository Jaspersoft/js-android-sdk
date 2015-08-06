/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.test.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class TestResource {
    private final File mResource;

    private TestResource(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            throw new IllegalArgumentException("Resource name should not be null");
        }
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(".");
        File classesFolder = new File(url.getFile());
        File resourcesFolder = new File(
                new File(classesFolder.getParent()).getParent() + "/resources/test"
        );

        File file = new File(resourcesFolder, fileName);
        if (!file.exists()) {
            throw new RuntimeException(
                    new FileNotFoundException("Resource on path: " + file.getPath() + " not found")
            );
        }
        mResource = file;
    }

    public static TestResource create(String fileName) {
        return new TestResource(fileName);
    }

    public String asString() {
        return readFile(asFile());
    }

    public InputStream asStream() {
        try {
            return new FileInputStream(mResource);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public File asFile() {
        return mResource;
    }

    @Override
    public String toString() {
        return "TestResource{" +
                "path='" + mResource.getPath() + '\'' +
                '}';
    }

    private static String readFile(File f) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine);
            }

        } catch (IOException e) {
            System.err.println("I/O Exception:" + e.getMessage());
            return null;
        }
        return sb.toString();
    }

}
