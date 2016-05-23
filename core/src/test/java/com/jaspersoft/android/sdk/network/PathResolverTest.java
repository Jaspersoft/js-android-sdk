/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.network;

import com.squareup.okhttp.HttpUrl;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class PathResolverTest {
    @Test
    public void shouldAggregateSinglePath() throws Exception {
        HttpUrl base = HttpUrl.parse("http://host/");
        PathResolver resolver = new PathResolver.Builder()
                .addPath("path")
                .build();
        HttpUrl expected = resolver.resolve(base);
        assertThat(expected, is(HttpUrl.parse("http://host/path")));
    }

    @Test
    public void shouldAggregateMultiplePaths() throws Exception {
        HttpUrl base = HttpUrl.parse("http://host/");
        PathResolver resolver = new PathResolver.Builder()
                .addPaths("/path/a/b")
                .build();
        HttpUrl expected = resolver.resolve(base);
        assertThat(expected, is(HttpUrl.parse("http://host/path/a/b")));
    }

    @Test
    public void shouldAggregateSinglePathAsItIs() throws Exception {
        HttpUrl base = HttpUrl.parse("http://host/");
        PathResolver resolver = new PathResolver.Builder()
                .addPaths("path")
                .build();
        HttpUrl expected = resolver.resolve(base);
        assertThat(expected, is(HttpUrl.parse("http://host/path")));
    }

    @Test
    public void shouldNotAggregatePathIfEmptinessSupplied() throws Exception {

        HttpUrl base = HttpUrl.parse("http://host/");
        PathResolver resolver = new PathResolver.Builder()
                .addPath("")
                .build();
        HttpUrl expected = resolver.resolve(base);
        assertThat(expected, is(HttpUrl.parse("http://host/")));

        PathResolver resolver2 = new PathResolver.Builder()
                .addPaths("")
                .build();
        HttpUrl expected2 = resolver2.resolve(base);
        assertThat(expected2, is(HttpUrl.parse("http://host/")));
    }
}