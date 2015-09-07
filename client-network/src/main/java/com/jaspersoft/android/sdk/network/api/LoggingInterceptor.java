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

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import okio.Buffer;

/**
 * @author Tom Koptel
 * @since 2.2
 */
final class LoggingInterceptor implements Interceptor {
    private final RestApiLog logger;

    public LoggingInterceptor(RestApiLog logger) {
        this.logger = logger;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        logger.log(String.format("Sending request %s on %s%n%s \nWith body: \n%s",
                request.url(), chain.connection(), request.headers(), bodyToString(request)));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        logger.log(String.format("Received response for %s in %.1fms%n%sBody: \n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers(), bodyToString(request)));

        return response;
    }

    private static String bodyToString(final Request request){
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() != null) {
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            } else {
                return "No Body";
            }
        } catch (final IOException e) {
            return String.format("<Can not decode response body. Reason: %s>", e.getMessage());
        }
    }
}