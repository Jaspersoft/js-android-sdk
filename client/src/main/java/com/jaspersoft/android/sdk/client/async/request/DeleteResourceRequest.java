/*
 * Copyright (C) 2012-2013 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;

/**
 * Request that deletes the resource with the specified URI.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class DeleteResourceRequest extends BaseRequest<Void> {

    private String uri;

    /**
     * Creates a new instance of {@link DeleteResourceRequest}.
     * @param uri resource URI (e.g. /reports/samples/)
     */
    public DeleteResourceRequest(JsRestClient jsRestClient, String uri) {
        super(jsRestClient, Void.class);
        this.uri = uri;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        getJsRestClient().deleteResource(uri);
        return null;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getUri() {
        return uri;
    }

}
