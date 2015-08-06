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

package com.jaspersoft.android.sdk.data.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jaspersoft.android.sdk.data.server.ServerVersion;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ServerVersionAdapter extends TypeAdapter<com.jaspersoft.android.sdk.data.server.ServerVersion> {
    @Override
    public void write(JsonWriter out, com.jaspersoft.android.sdk.data.server.ServerVersion value) throws IOException {
        out.value(value.getRawValue());
    }

    @Override
    public com.jaspersoft.android.sdk.data.server.ServerVersion read(JsonReader in) throws IOException {
        String rawValue = in.nextString();
        return ServerVersion.defaultParser().parse(rawValue);
    }
}
