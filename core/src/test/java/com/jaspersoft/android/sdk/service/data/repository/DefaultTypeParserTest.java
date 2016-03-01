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

package com.jaspersoft.android.sdk.service.data.repository;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.3
 */
@RunWith(JUnitParamsRunner.class)
public class DefaultTypeParserTest {
    @Test
    @Parameters({"folder", "reportUnit", "dashboard", "legacyDashboard", "file", "semanticLayerDataSource", "jndiJdbcDataSource"})
    public void shouldProvideTypeForKnownResourceTypes(String type) {
        ResourceType resourceType = ResourceType.valueOf(type);
        assertThat(DefaultTypeParser.INSTANCE.parse(type), is(resourceType));
    }

    @Test
    public void shouldReturnUnkownTypeForMissingMapping() {
        ResourceType resourceType = DefaultTypeParser.INSTANCE.parse("someStrangeType");
        assertThat(resourceType, is(notNullValue()));
        assertThat(resourceType.getRawValue(), is("someStrangeType"));
    }
}
