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

package com.jaspersoft.android.sdk.network.api;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okio.Buffer;
import retrofit.Converter;

/**
 * Temporary workaround around {@link TypeAdapter} issue which causes null fields to be parsed.
 * The issue submitted on following <a href="https://github.com/square/retrofit/issues/1062">link</a>
 *
 * @author Tom Koptel
 * @since 2.0
 */
final class GsonConverterFactory implements Converter.Factory {
    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFactory create(Gson gson) {
        return new GsonConverterFactory(gson);
    }

    private final Gson gson;

    private GsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    /** Create a converter for {@code type}. */
    @Override public Converter<?> get(Type type) {
        return new GsonConverter<>(type, gson);
    }

    private static class GsonConverter<T> implements Converter<T> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson mGson;
        private final Type mType;

        GsonConverter(Type type, Gson gson) {
            mType = type;
            mGson = gson;
        }

        @Override public T fromBody(ResponseBody body) throws IOException {
            Reader in = body.charStream();
            try {
                return mGson.fromJson(in, mType);
            } finally {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

        @Override public RequestBody toBody(T value) {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            try {
                mGson.toJson(value, mType, writer);
                writer.flush();
            } catch (IOException e) {
                throw new AssertionError(e); // Writing to Buffer does no I/O.
            }
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }
}