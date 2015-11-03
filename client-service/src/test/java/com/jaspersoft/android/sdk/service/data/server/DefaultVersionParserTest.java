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

package com.jaspersoft.android.sdk.service.data.server;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class DefaultVersionParserTest {
    @Test
    @Parameters({
            "5.0.0, 5",
            "5.1.0, 5.1",
            "5.2.0, 5.2",
            "5.5.0, 5.5",
            "5.6.0, 5.6",
            "5.6.1, 5.61",
            "6.0, 6",
            "6.0.1, 6.01",
            "6.1, 6.1",
            "6.1.1, 6.11",
    })
    public void shouldParseSemanticVersioning(String versionCode, String expected) {
        double expectedCode = Double.valueOf(expected);
        double resultCode = DefaultVersionParser.INSTANCE.convertToDouble(versionCode);
        assertThat(resultCode, is(expectedCode));
    }

    @Test
    @Parameters({
            "5.6.1.2, 5.612",
            "5.6.1.2.0, 5.612",
            "5.5.6.1.2, 5.5612",
            "5.5.6.1.2.0, 5.5612",
            "5.5.6.1.2.3, 5.56123",
            "5.5.6.1.2.3.0, 5.56123",
    })
    public void shouldParseLongSemanticVersioning(String versionCode, String expected) {
        double expectedCode = Double.valueOf(expected);
        double resultCode = DefaultVersionParser.INSTANCE.convertToDouble(versionCode);
        assertThat(resultCode, is(expectedCode));
    }
}