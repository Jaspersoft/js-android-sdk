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

package com.jaspersoft.android.sdk.client.async.request.cacheable;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.ReportDescriptor;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;
import com.jaspersoft.android.sdk.client.oxm.ResourceParameter;

/**
 * Request that runs the report and generates the specified output. The response contains report descriptor
 * with the ID of the saved output for downloading later with another GET request.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class GetReportRequest extends CacheableRequest<ReportDescriptor> {

    private ResourceDescriptor resourceDescriptor;
    private String outputFormat;

    /**
     * Creates a new instance of {@link GetReportRequest}.
     *
     * @param resourceDescriptor resource descriptor of this report
     * @param outputFormat The format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV,
     * XML, JRPRINT. The Default is PDF.
     */
    public GetReportRequest(JsRestClient jsRestClient, ResourceDescriptor resourceDescriptor, String outputFormat) {
        super(jsRestClient, ReportDescriptor.class);
        this.resourceDescriptor = resourceDescriptor;
        this.outputFormat = outputFormat;
    }

    @Override
    public ReportDescriptor loadDataFromNetwork() throws Exception {
        return getJsRestClient().getReportDescriptor(resourceDescriptor, outputFormat);
    }

    @Override
    protected String createCacheKeyString() {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(super.createCacheKeyString());
        keyBuilder.append(resourceDescriptor.getUriString());
        keyBuilder.append(outputFormat);
        for (ResourceParameter parameter : resourceDescriptor.getParameters()) {
            keyBuilder.append(parameter.getName());
            keyBuilder.append(parameter.getValue());
        }
        return keyBuilder.toString();
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public ResourceDescriptor getResourceDescriptor() {
        return resourceDescriptor;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

}
