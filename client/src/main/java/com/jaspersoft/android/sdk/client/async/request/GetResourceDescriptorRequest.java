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

package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.cacheable.CacheableRequest;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookup;

/**
 * Request that gets the report resource lookup for the specified URI.
 *
 * @author Andrew Tivodar
 * @since 1.12
 */
public class GetResourceDescriptorRequest extends CacheableRequest<ResourceLookup> {

    private String uri;
    private ResourceLookup.ResourceType resourceType;

    /**
     * Creates a new instance of {@link GetResourceDescriptorRequest}.
     * @param uri resource URI (e.g. /reports/samples/)
     */
    public GetResourceDescriptorRequest(JsRestClient jsRestClient, String uri, ResourceLookup.ResourceType resourceType) {
        super(jsRestClient, ResourceLookup.class);
        this.uri = uri;
        this.resourceType = resourceType;
    }

    @Override
    public ResourceLookup loadDataFromNetwork() throws Exception {
        return getJsRestClient().getResourceDescriptor(uri, resourceType);
    }

    @Override
    protected String createCacheKeyString() {
        return super.createCacheKeyString() + uri;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getUri() {
        return uri;
    }

}
