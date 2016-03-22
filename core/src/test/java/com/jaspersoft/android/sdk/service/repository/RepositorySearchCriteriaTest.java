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

package com.jaspersoft.android.sdk.service.repository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class RepositorySearchCriteriaTest {
    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();

    @Test
    public void shouldNotAcceptNegativeOffset() {
        mExpectedException.expect(IllegalArgumentException.class);
        mExpectedException.expectMessage("Offset should be positive");
        RepositorySearchCriteria.builder().withOffset(-1).build();
    }

    @Test
    public void shouldNotAcceptNegativeLimit() {
        mExpectedException.expect(IllegalArgumentException.class);
        mExpectedException.expectMessage("Limit should be positive");
        RepositorySearchCriteria.builder().withLimit(-1).build();
    }
}
