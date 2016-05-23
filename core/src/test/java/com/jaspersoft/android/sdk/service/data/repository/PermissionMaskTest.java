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

package com.jaspersoft.android.sdk.service.data.repository;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

@RunWith(JUnitParamsRunner.class)
public class PermissionMaskTest {

    @Rule
    public ExpectedException expected = none();

    @Test
    @Parameters({
            "NO_ACCESS|0",
            "ADMINISTER|1",
            "READ_ONLY|2",
            "READ_WRITE|6",
            "READ_DELETE|18",
            "READ_WRITE_DELETE|30",
            "EXECUTE_ONLY|32",
    })
    public void mapWith(String value, int mask) {
        PermissionMask actual = PermissionMask.valueOf(value);
        PermissionMask expected = PermissionMask.fromRawValue(mask);
        assertThat(expected, is(actual));
    }

    @Test
    public void should_throw_illegal_argument_exception() throws Exception {
        expected.expectMessage("Undefined type of mask: '100'");
        expected.expect(IllegalArgumentException.class);
        PermissionMask.fromRawValue(100);
    }
}