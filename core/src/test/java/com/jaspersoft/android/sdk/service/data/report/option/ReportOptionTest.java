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

package com.jaspersoft.android.sdk.service.data.report.option;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.rules.ExpectedException.none;

public class ReportOptionTest {

    private ReportOption.Builder mBuilder;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        mBuilder = new ReportOption.Builder();
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(ReportOption.class).verify();
    }

    @Test
    public void builder_should_not_allow_null_id() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Id should not be null");
        new ReportOption.Builder().withId(null);
    }

    @Test
    public void builder_should_not_allow_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Uri should not be null");
        new ReportOption.Builder().withUri(null);
    }

    @Test
    public void builder_should_not_allow_null_label() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Label should not be null");
        new ReportOption.Builder().withLabel(null);
    }

    @Test
    public void build_should_not_allow_null_id() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report option can not be created without id");
        new ReportOption.Builder()
                .withUri("/my/uri")
                .withLabel("label")
                .build();
    }

    @Test
    public void build_should_not_allow_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report option can not be created without uri");
        new ReportOption.Builder()
                .withId("id")
                .withLabel("label")
                .build();
    }

    @Test
    public void build_should_not_allow_null_label() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report option can not be created without label");
        new ReportOption.Builder()
                .withId("id")
                .withUri("/my/uri")
                .build();
    }
}