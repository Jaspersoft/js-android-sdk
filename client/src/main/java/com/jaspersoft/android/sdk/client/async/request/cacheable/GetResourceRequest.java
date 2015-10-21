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

package com.jaspersoft.android.sdk.client.async.request.cacheable;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;

/**
 * Request that gets the resource descriptor for the resource with specified URI.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class GetResourceRequest extends CacheableRequest<ResourceDescriptor> {

    private String uri;

    /**
     * Creates a new instance of {@link GetResourceRequest}.
     * @param uri resource URI (e.g. /reports/samples/)
     */
    public GetResourceRequest(JsRestClient jsRestClient, String uri) {
        super(jsRestClient, ResourceDescriptor.class);
        this.uri = uri;
    }

    @Override
    public ResourceDescriptor loadDataFromNetwork() throws Exception {
        return getJsRestClient().getResource(uri);
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
