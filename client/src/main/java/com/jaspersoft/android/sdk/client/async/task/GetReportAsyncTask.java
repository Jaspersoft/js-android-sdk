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

package com.jaspersoft.android.sdk.client.async.task;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.ReportDescriptor;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;

/**
 * <p>Declaration of the <strong>GetReportAsyncTask</strong> which is subclass of <strong>JsRestAsyncTask</strong>
 * abstract class and overrides <code>doInBackground(Object... arg0)</code> method.</p>
 *
 * @author Ivan Gadzhega
 * @since 1.0
 *
 * @deprecated Use {@link com.jaspersoft.android.sdk.client.async.request.cacheable.GetReportRequest GetReportRequest}
 * instead.
 */

@Deprecated
public class GetReportAsyncTask extends JsRestAsyncTask<Object, ReportDescriptor> {

    private ResourceDescriptor resourceDescriptor;
    private String outputFormat;

    /**
     * Creates a new <strong>GetReportAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetReportAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     */
    public GetReportAsyncTask(int id, JsRestClient jsRestClient,
            ResourceDescriptor resourceDescriptor, String outputFormat) {
        super(id, jsRestClient);
        this.resourceDescriptor = resourceDescriptor;
        this.outputFormat = outputFormat;
    }

    /**
     * Creates a new <strong>GetReportAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetReportAsyncTask</strong> identifier.
     * @param progressMessage <strong>Progress dialog</strong> message.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceDescriptor resource descriptor of this report
     * @param outputFormat The format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV,
     * XML, JRPRINT. The Default is PDF.
     */
    public GetReportAsyncTask(int id, String progressMessage, JsRestClient jsRestClient,
            ResourceDescriptor resourceDescriptor, String outputFormat) {
        super(id, progressMessage, jsRestClient);
        this.resourceDescriptor = resourceDescriptor;
        this.outputFormat = outputFormat;
    }

    /**
     * Creates a new <strong>GetReportAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetReportAsyncTask</strong> identifier.
     * @param progressMessage <strong>Progress dialog</strong> message.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceDescriptor resource descriptor of this report
     * @param outputFormat The format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV,
     * XML, JRPRINT. The Default is PDF.
     */
    public GetReportAsyncTask(int id, String progressMessage, long showDialogTimeout, JsRestClient jsRestClient,
            ResourceDescriptor resourceDescriptor, String outputFormat) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        this.resourceDescriptor = resourceDescriptor;
        this.outputFormat = outputFormat;
    }

    /**
     * Overrides the <code>doInBackground(Object... arg0)</code> method by calling <strong>JsRestClient</strong>
     * <code>getReportDescriptor(String resourceUri, String outputFormat)</code> method.
     *
     * @param arg0 the parameters of the <strong>Asynchronous task</strong>. Current implementation does not use this params.
     * @return <strong>Resource descriptor</strong> by resource URI.
     */
    @Override
    protected ReportDescriptor doInBackground(Object... arg0) {
        super.doInBackground(arg0);
        try {
            return getJsRestClient().getReportDescriptor(this.resourceDescriptor, outputFormat);
        } catch (Exception e) {
            setTaskException(e);
            return null;
        }
    }

    // Getters

    public ResourceDescriptor getResourceDescriptor() {
        return resourceDescriptor;
    }

    public String getOutputFormat() {
        return outputFormat;
    }
}
