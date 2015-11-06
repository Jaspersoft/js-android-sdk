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
import com.jaspersoft.android.sdk.client.oxm.resource.FileLookup;

/**
 * Request that gets the file resource lookup for the specified URI.
 *
 * @author Andrew Tivodar
 * @since 1.12
 */
public class GetFileResourceRequest extends CacheableRequest<FileLookup> {

    private String uri;

    /**
     * Creates a new instance of {@link GetFileResourceRequest}.
     * @param uri file URI (e.g. /reports/samples/)
     */
    public GetFileResourceRequest(JsRestClient jsRestClient, String uri) {
        super(jsRestClient, FileLookup.class);
        this.uri = uri;
    }

    @Override
    public FileLookup loadDataFromNetwork() throws Exception {
        return getJsRestClient().getFileDescriptor(uri);
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
