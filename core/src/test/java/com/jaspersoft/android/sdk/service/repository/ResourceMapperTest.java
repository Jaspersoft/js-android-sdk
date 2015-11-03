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

import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ResourceMapperTest {
    public static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());
    @Mock
    ServerInfo mServerInfo;
    @Mock
    ResourceLookup mResourceLookup;

    private ResourceMapper objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new ResourceMapper();

        when(mResourceLookup.getCreationDate()).thenReturn("2013-10-03 16:32:05");
        when(mResourceLookup.getUpdateDate()).thenReturn("2013-11-03 16:32:05");
        when(mResourceLookup.getResourceType()).thenReturn("reportUnit");
        when(mResourceLookup.getDescription()).thenReturn("description");
        when(mResourceLookup.getLabel()).thenReturn("label");
    }

    @Test
    public void testTransform() throws Exception {
        long creationTime = DATE_FORMAT.parse("2013-10-03 16:32:05").getTime();
        long updateTime = DATE_FORMAT.parse("2013-11-03 16:32:05").getTime();

        Resource resource = objectUnderTest.transform(mResourceLookup, DATE_FORMAT);
        assertThat(resource.getCreationDate().getTime(), is(creationTime));
        assertThat(resource.getUpdateDate().getTime(), is(updateTime));
        assertThat(resource.getDescription(), is("description"));
        assertThat(resource.getLabel(), is("label"));
        assertThat(resource.getResourceType(), is(ResourceType.reportUnit));
    }

    @Test
    public void testTransformResourceLookupCollection() {
        ResourceLookup mockResourceLookupOne = mock(ResourceLookup.class);
        ResourceLookup mockResourceLookupTwo = mock(ResourceLookup.class);

        List<ResourceLookup> lookups = new ArrayList<ResourceLookup>(5);
        lookups.add(mockResourceLookupOne);
        lookups.add(mockResourceLookupTwo);

        Collection<Resource> resourcesCollection = objectUnderTest.transform(lookups, DATE_FORMAT);

        assertThat(resourcesCollection.toArray()[0], is(instanceOf(Resource.class)));
        assertThat(resourcesCollection.toArray()[1], is(instanceOf(Resource.class)));
        assertThat(resourcesCollection.size(), is(2));
    }
}