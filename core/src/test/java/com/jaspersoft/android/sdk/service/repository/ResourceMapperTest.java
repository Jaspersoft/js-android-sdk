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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.TestConstants;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.service.data.repository.PermissionMask;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class ResourceMapperTest {
    @Mock
    ResourceLookup mResourceLookup;
    private ResourceMapper mMapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mMapper = new ResourceMapper(TestConstants.DATE_TIME_FORMAT);
        mResourceLookup = MockResourceFactory.mockCommonFields(mResourceLookup);
    }

    @Test
    public void should_map_lookup_to_resource() throws Exception {
        long creationTime = TestConstants.DATE_TIME_FORMAT.parse("2013-10-03 16:32:05").getTime();
        long updateTime = TestConstants.DATE_TIME_FORMAT.parse("2013-11-03 16:32:05").getTime();

        Resource resource = mMapper.transform(mResourceLookup);

        assertThat(resource.getCreationDate().getTime(), is(creationTime));
        assertThat(resource.getUpdateDate().getTime(), is(updateTime));
        assertThat(resource.getDescription(), is("description"));
        assertThat(resource.getLabel(), is("label"));
        assertThat(resource.getUri(), is("/my/uri"));
        assertThat(resource.getResourceType(), is(ResourceType.reportUnit));
        assertThat(resource.getVersion(), is(100));
        assertThat(resource.getPermissionMask(), is(PermissionMask.NO_ACCESS));
    }
}