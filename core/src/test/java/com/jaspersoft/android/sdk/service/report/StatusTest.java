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
package com.jaspersoft.android.sdk.service.report;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * @author Tom Koptel
 * @since 2.0
 */
public class StatusTest {
    @Test
    public void testStatusShouldBeQueued() {
        Status status = Status.wrap("queued");
        assertThat(status.isQueued(), is(true));
    }

    @Test
    public void testStatusShouldBeExecution() {
        Status status = Status.wrap("execution");
        assertThat(status.isExecution(), is(true));
    }

    @Test
    public void testStatusShouldBeCancelled() {
        Status status = Status.wrap("cancelled");
        assertThat(status.isCancelled(), is(true));
    }

    @Test
    public void testStatusShouldBeFailed() {
        Status status = Status.wrap("failed");
        assertThat(status.isFailed(), is(true));
    }

    @Test
    public void testStatusShouldBeReady() {
        Status status = Status.wrap("ready");
        assertThat(status.isReady(), is(true));
    }
}
