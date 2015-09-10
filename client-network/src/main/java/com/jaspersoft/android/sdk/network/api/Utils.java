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

package com.jaspersoft.android.sdk.network.api;

import com.squareup.okhttp.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class Utils {
    private Utils() {
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static void checkArgument(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static int headerToInt(com.squareup.okhttp.Headers headers, String key) {
        String header = headers.get(key);
        if (header == null) {
            return 0;
        } else {
            return Integer.valueOf(header);
        }
    }

    public static String normalizeBaseUrl(String url) {
        // If url is empty skip appending slash
        if (url == null || url.length() == 0) {
            return url;
        }
        if (url.endsWith("/")) {
            return url;
        }
        return url + "/";
    }

    public static String bodyToString(ResponseBody responseBody) {
        try {
            InputStream inputStream = responseBody.byteStream();
            return streamToString(inputStream);
        } catch (IOException ex) {
            return null;
        }
    }

    private static String streamToString(InputStream stream) throws IOException {
        if (stream == null) {
            return null;
        }

        InputStreamReader is = new InputStreamReader(stream);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);

        try {
            String read = br.readLine();
            while (read != null) {
                sb.append(read);
                read = br.readLine();
            }
            return sb.toString();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                // close quietly
            }
        }
    }

    public static String joinString(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token: tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }
}
