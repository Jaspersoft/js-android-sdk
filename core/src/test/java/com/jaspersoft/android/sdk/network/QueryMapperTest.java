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
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;
import java.util.Map;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class QueryMapperTest {

    private QueryMapper mQueryMapper;
    private final HttpUrl mLocalhost = HttpUrl.parse("http://localhost/");

    @Before
    public void setUp() throws Exception {
        mQueryMapper = QueryMapper.INSTANCE;
    }

    @Test
    public void should_ignore_null_params() throws Exception {
        HttpUrl expected = mQueryMapper.mapParams(null, mLocalhost);
        assertThat(expected, is(mLocalhost));
    }

    @Test
    @Parameters(method = "params")
    public void should_encode_any_param(String key, String value, String query) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(key, value);

        HttpUrl expected = mQueryMapper.mapParams(params, mLocalhost);
        assertThat(expected, is(HttpUrl.parse("http://localhost/?" + query)));
    }

    private Object[] params() {
        return $(
                $("reportUnitURI", "/some/report", "reportUnitURI=%2Fsome%2Freport"),
                $("owner", "jasperadmin|organization_1", "owner=jasperadmin%7Corganization_1"),
                $("label", "Sample Name", "label=Sample%20Name"),
                $("startIndex", "1", "startIndex=1"),
                $("numberOfRows", "-1", "numberOfRows=-1"),
                $("sortType", "NONE", "sortType=NONE"),
                $("isAscending", "true", "isAscending=true")
        );
    }
}