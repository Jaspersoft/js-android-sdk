/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.network.rest.v2.api;

import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ResourceSearchResponse;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;

import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedString;

import static com.jaspersoft.android.sdk.test.matcher.HasSerializedName.hasSerializedName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class})
public class ResourceSearchResponseAdapterTest {

    @ResourceFile("json/all_resources.json")
    TestResource searchResponse;
    @Mock
    Response mockResponse;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        MockitoAnnotations.initMocks(this);

        TypedInput emptyJsonInput = new TypedString("{}");
        when(mockResponse.getBody()).thenReturn(emptyJsonInput);
    }

    @Test
    public void shouldAssignDefaultValueToResultCountIfHeaderMissing() {
        ResourceSearchResponse response = ResourceSearchResponseAdapter.adapt(mockResponse);
        assertThat(response.getResultCount(), is(0));
    }

    @Test
    public void shouldAssignDefaultValueToTotalCountIfHeaderMissing() {
        ResourceSearchResponse response = ResourceSearchResponseAdapter.adapt(mockResponse);
        assertThat(response.getTotalCount(), is(0));
    }

    @Test
    public void shouldAssignDefaultValueToValueStartIndexIfHeaderMissing() {
        ResourceSearchResponse response = ResourceSearchResponseAdapter.adapt(mockResponse);
        assertThat(response.getStartIndex(), is(0));
    }

    @Test
    public void shouldAssignDefaultValueToValueNextOffsetIfHeaderMissing() {
        ResourceSearchResponse response = ResourceSearchResponseAdapter.adapt(mockResponse);
        assertThat(response.getNextOffset(), is(0));
    }

    @Test
    public void shouldExtractResultCountFromHeaders() {
        when(mockResponse.getHeaders()).thenReturn(new ArrayList<Header>() {{
            add(new Header("Result-Count", String.valueOf(2)));
        }});
        ResourceSearchResponse response = ResourceSearchResponseAdapter.adapt(mockResponse);
        assertThat(response.getResultCount(), is(2));
    }

    @Test
    public void shouldExtractTotalCountFromHeaders() {
        when(mockResponse.getHeaders()).thenReturn(new ArrayList<Header>(){{
            add(new Header("Total-Count", String.valueOf(3)));
        }});
        ResourceSearchResponse response = ResourceSearchResponseAdapter.adapt(mockResponse);
        assertThat(response.getTotalCount(), is(3));
    }

    @Test
    public void shouldExtractStartIndexFromHeaders() {
        when(mockResponse.getHeaders()).thenReturn(new ArrayList<Header>(){{
            add(new Header("Start-Index", String.valueOf(3)));
        }});
        ResourceSearchResponse response = ResourceSearchResponseAdapter.adapt(mockResponse);
        assertThat(response.getStartIndex(), is(3));
    }

    @Test
    public void shouldExtractNextOffsetFromHeaders() {
        when(mockResponse.getHeaders()).thenReturn(new ArrayList<Header>(){{
            add(new Header("Next-Offset", String.valueOf(4)));
        }});
        ResourceSearchResponse response = ResourceSearchResponseAdapter.adapt(mockResponse);
        assertThat(response.getNextOffset(), is(4));
    }

    @Test
    public void shouldParseJsonResponseFromBody() {
        TypedInput typedInput = new TypedString(searchResponse.asString());
        when(mockResponse.getBody()).thenReturn(typedInput);

        ResourceSearchResponse response = ResourceSearchResponseAdapter.adapt(mockResponse);
        assertThat(response.getResources(), is(not(empty())));
    }

    @Test
    public void mResourcesFieldShouldHaveSerializedNameAnnotationForField() throws NoSuchFieldException {
        Field field = ResourceSearchResponse.class.getDeclaredField("mResources");
        assertThat(field, hasSerializedName("resourceLookup"));
    }
}
