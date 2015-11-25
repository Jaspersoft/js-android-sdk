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
public class ServerVersionTest {
    @Test
    @Parameters({
            "5.0.0, v5",
            "5.2.0, v5_2",
            "5.5.0, v5_5",
            "5.6.0, v5_6",
            "5.6.1, v5_6_1",
            "6.0, v6",
            "6.0.1, v6_0_1",
            "6.1, v6_1",
    })
    public void shouldParseSemanticVersioning(String versionCode, String enumName) {
        ServerVersion expectedRelease = ServerVersion.valueOf(enumName);
        ServerVersion resultRelease = ServerVersion.parse(versionCode);
        assertThat(resultRelease, is(expectedRelease));
    }

    @Test
    @Parameters({
            "5.0, v5",
            "5.2, v5_2",
            "5.5, v5_5",
            "5.6, v5_6",
            "5.6.1, v5_6_1",
            "6.0, v6",
            "6.0.1, v6_0_1",
            "6.1, v6_1",
    })
    public void shouldParseCode(String versionCode, String enumName) {
        ServerVersion expectedRelease = ServerVersion.valueOf(enumName);
        ServerVersion resultRelease = ServerVersion.parse(versionCode);
        assertThat(resultRelease, is(expectedRelease));
    }

    @Test
    @Parameters({
            "5.6.0 Preview",
            "5.6.0-BETA",
    })
    public void shouldParseNonSemanticVersioning(String nonSemanticVersion) {
        ServerVersion resultRelease = ServerVersion.parse(nonSemanticVersion);
        assertThat(resultRelease, is(ServerVersion.v5_6));
    }
}