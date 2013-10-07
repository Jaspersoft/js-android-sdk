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
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;

/**
 * Request that modifies the resource with specified ResourceDescriptor.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class ModifyResourceRequest extends BaseRequest<Void> {

    private ResourceDescriptor resourceDescriptor;

    /**
     * Creates a new instance of {@link ModifyResourceRequest}.
     * @param resourceDescriptor descriptor of resource being modified
     */
    public ModifyResourceRequest(JsRestClient jsRestClient, ResourceDescriptor resourceDescriptor) {
        super(jsRestClient, Void.class);
        this.resourceDescriptor = resourceDescriptor;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        getJsRestClient().modifyResource(resourceDescriptor);
        return null;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public ResourceDescriptor getResourceDescriptor() {
        return resourceDescriptor;
    }

}
