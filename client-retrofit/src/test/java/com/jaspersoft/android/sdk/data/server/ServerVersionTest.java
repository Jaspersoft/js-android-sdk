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
            "5.0.0, EMERALD",
            "5.2.0, EMERALD_MR1",
            "5.5.0, EMERALD_MR2",
            "5.6.0, EMERALD_MR3",
            "5.6.1, EMERALD_MR4",
            "6.0, AMBER",
            "6.0.1, AMBER_MR1",
            "6.1, AMBER_MR2",
    })
    public void shouldParseSemanticVersioning(String versionCode, String enumName) {
        ServerVersion expectedRelease = ServerVersion.valueOf(enumName);
        ServerVersion resultRelease = ServerVersion.defaultParser().parse(versionCode);
        assertThat(resultRelease, is(expectedRelease));
    }

    @Test
    @Parameters({
            "5.0, EMERALD",
            "5.2, EMERALD_MR1",
            "5.5, EMERALD_MR2",
            "5.6, EMERALD_MR3",
            "5.6.1, EMERALD_MR4",
            "6.0, AMBER",
            "6.0.1, AMBER_MR1",
            "6.1, AMBER_MR2",
    })
    public void shouldParseCode(String versionCode, String enumName) {
        ServerVersion expectedRelease = ServerVersion.valueOf(enumName);
        ServerVersion resultRelease = ServerVersion.defaultParser().parse(versionCode);
        assertThat(resultRelease, is(expectedRelease));
    }

    @Test
    @Parameters({
            "5.6.0 Preview",
            "5.6.0-BETA",
    })
    public void shouldParseNonSemanticVersioning(String nonSemanticVersion) {
        ServerVersion resultRelease = ServerVersion.defaultParser().parse(nonSemanticVersion);
        assertThat(resultRelease, is(ServerVersion.EMERALD_MR3));
    }
}