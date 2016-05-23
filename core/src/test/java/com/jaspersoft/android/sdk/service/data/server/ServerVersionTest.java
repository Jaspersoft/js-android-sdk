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

package com.jaspersoft.android.sdk.service.data.server;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class ServerVersionTest {

    @Test
    @Parameters({
            "v5_6, v5_5",
            "v5_6_1, v5_6",
            "v6, v5_6_1",
            "v6_0_1, v6",
            "v6_1, v6",
            "v6_1_1, v6_1",
            "v6_2, v6_1_1"
    })
    public void testGreaterThan(String greater, String lesser) throws Exception {
        ServerVersion v1 = (ServerVersion) ServerVersion.class.getField(greater).get(null);
        ServerVersion v2 = (ServerVersion) ServerVersion.class.getField(lesser).get(null);
        assertThat(String.format("%s should be greater than %s", greater, lesser), v1.greaterThan(v2));
    }

    @Test
    @Parameters({
            "v5_6, v5_5",
            "v5_6_1, v5_6",
            "v6, v5_6_1",
            "v6_0_1, v6",
            "v6_1, v6",
            "v6_1_1, v6_1",
            "v6_2, v6_1_1"
    })
    public void testGreaterThanOrEquals(String greater, String lesser) throws Exception {
        ServerVersion v1 = (ServerVersion) ServerVersion.class.getField(greater).get(null);
        ServerVersion v2 = (ServerVersion) ServerVersion.class.getField(lesser).get(null);
        assertThat(String.format("%s should be greater than %s", greater, lesser), v1.greaterThan(v2));
        assertThat(String.format("%s should be greater than or equal %s", greater, lesser), v1.greaterThanOrEquals(v2));
    }

    @Test
    @Parameters({
            "v5_6, v5_5",
            "v5_6_1, v5_6",
            "v6, v5_6_1",
            "v6_0_1, v6",
            "v6_1, v6",
            "v6_1_1, v6_1",
            "v6_2, v6_1_1"
    })
    public void testLessThan(String greater, String lesser) throws Exception {
        ServerVersion v1 = (ServerVersion) ServerVersion.class.getField(lesser).get(null);
        ServerVersion v2 = (ServerVersion) ServerVersion.class.getField(greater).get(null);
        assertThat(String.format("%s should be less than %s", lesser, greater), v1.lessThan(v2));
    }

    @Test
    @Parameters({
            "v5_6, v5_5",
            "v5_6_1, v5_6",
            "v6, v5_6_1",
            "v6_0_1, v6",
            "v6_1, v6",
            "v6_1_1, v6_1",
            "v6_2, v6_1_1"
    })
    public void testLessThanOrEquals(String greater, String lesser) throws Exception {
        ServerVersion v1 = (ServerVersion) ServerVersion.class.getField(lesser).get(null);
        ServerVersion v2 = (ServerVersion) ServerVersion.class.getField(greater).get(null);
        assertThat(String.format("%s should be less than %s", lesser, greater), v1.lessThan(v2));
        assertThat(String.format("%s should be less than or equals %s", lesser, greater), v1.lessThanOrEquals(v2));
    }

    @Test
    @Parameters({
            "v5_5, 5.5",
            "v5_6, 5.6",
            "v5_6_1, 5.6.1",
            "v6, 6.0",
            "v6_0_1, 6.0.1",
            "v6_1, 6.1",
            "v6_1_1, 6.1.1",
            "v6_2, 6.2",
    })
    public void testToString(String version, String expected) throws Exception {
        ServerVersion v1 = (ServerVersion) ServerVersion.class.getField(version).get(null);
        assertThat(String.format("Raw version should be %s", expected), expected, is(v1.toString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfVersionMakeNoSense() {
        ServerVersion.valueOf("make no sense");
    }

    @Test
    @Parameters({"0", "0.0"})
    public void shouldAcceptZeroAsArgument(String version) {
        ServerVersion.valueOf(version);
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(ServerVersion.class)
                .verify();
    }
}