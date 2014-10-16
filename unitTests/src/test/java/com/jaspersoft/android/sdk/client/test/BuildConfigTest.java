/*
 * Copyright © 2014 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.client.test;

import com.jaspersoft.android.sdk.client.BuildConfig;
import com.jaspersoft.android.sdk.client.test.support.UnitTestSpecification;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class BuildConfigTest extends UnitTestSpecification {

    @Test
    public void shouldHaveCorrectConfiguration() {
        if ("debug".equals(BuildConfig.BUILD_TYPE)) {
            assertThat(BuildConfig.DEBUG).isTrue();
        } else if ("release".equals(BuildConfig.BUILD_TYPE)) {
            assertThat(BuildConfig.DEBUG).isFalse();
        } else {
            fail("build type configuration not tested or supported?");
        }
        new BuildConfig(); // dummy coverage, should be an interface or something else
    }

}
