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

package com.jaspersoft.android.sdk.test;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.rules.ExternalResource;
import org.robolectric.shadows.httpclient.FakeHttp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class WebMockRule extends ExternalResource {
    private MockWebServer server;

    private Logger logger = Logger.getLogger(WebMockRule.class.getName());

    @Before
    public void before() throws Throwable {
        super.before();
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);

        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        server = new MockWebServer();
        try {
            server.start();
        } catch (IOException e) {
            logger.log(Level.INFO, "Failed to start MockWebServer");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void after() {
        super.after();
        try {
            server.shutdown();
        } catch (IOException e) {
            logger.log(Level.INFO, "Failed to shutdown MockWebServer", e);
        }
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(true);
    }

    public MockWebServer get() {
        return server;
    }

    public String getRootUrl() {
        return server.url("/").url().toExternalForm();
    }

    public void enqueue(MockResponse mockResponse) {
        server.enqueue(mockResponse);
    }
}
