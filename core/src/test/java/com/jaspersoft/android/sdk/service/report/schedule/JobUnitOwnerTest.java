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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobOwner;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

public class JobUnitOwnerTest {
    @Rule
    public ExpectedException expected = none();

    @Test
    public void maps_username_organization_toString() throws Exception {
        JobOwner jobOwner = JobOwner.newOwner("jasperadmin", "organization_1");
        assertThat(jobOwner.toString(), is("jasperadmin|organization_1"));
    }

    @Test
    public void maps_username_toString() throws Exception {
        JobOwner jobOwner = JobOwner.newOwner("jasperadmin", null);
        assertThat(jobOwner.toString(), is("jasperadmin"));
    }

    @Test
    public void factory_fails_with_null_username() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Username should not be null");
        JobOwner.newOwner(null, null);
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(JobOwner.class).verify();
    }
}