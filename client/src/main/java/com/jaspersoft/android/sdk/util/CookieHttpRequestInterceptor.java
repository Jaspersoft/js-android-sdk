/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.util;

import android.util.Base64;
import android.util.Log;

import com.jaspersoft.android.sdk.client.JsServerProfile;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 1.9
 */
@Deprecated
public class CookieHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final String SET_COOKIE = "set-cookie";
    private static final String COOKIE = "Cookie";
    private static final String COOKIE_STORE = "cookieStore";
    private final JsServerProfile jsServerProfile;

    public CookieHttpRequestInterceptor(JsServerProfile jsServerProfile) {
        this.jsServerProfile = jsServerProfile;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] byteArray,
                                        ClientHttpRequestExecution execution) throws IOException {

        Log.d(getClass().getSimpleName(), ">>> entering intercept");
        List<String> cookies = request.getHeaders().get(COOKIE);
        // if the header doesn't exist, add any existing, saved cookies
        if (cookies == null) {
            List<String> cookieStore = (List<String>) StaticCacheHelper.retrieveObjectFromCache(COOKIE_STORE);
            // if we have stored cookies, add them to the headers
            if (cookieStore != null) {
                request.getHeaders().add(COOKIE, StringUtils.join(cookieStore, ";"));
            } else {
                Log.d(getClass().getSimpleName(), "Setting basic auth");
                // Basic Authentication
                String authorisation = jsServerProfile.getUsernameWithOrgId() + ":" + jsServerProfile.getPassword();
                byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes(), Base64.NO_WRAP);
                request.getHeaders().set("Authorization", "Basic " + new String(encodedAuthorisation));
                // disable buggy keep-alive
                request.getHeaders().set("Connection", "close");
            }
        }

        // execute the request
        ClientHttpResponse response = execution.execute(request, byteArray);
        // pull any cookies off and store them
        cookies = response.getHeaders().get(SET_COOKIE);
        if (cookies != null) {
            for (String cookie : cookies) {
                Log.d(getClass().getSimpleName(), ">>> response cookie = " + cookie);
            }
            StaticCacheHelper.storeObjectInCache(COOKIE_STORE, cookies);
        }
        Log.d(getClass().getSimpleName(), ">>> leaving intercept");
        return response;
    }
}
