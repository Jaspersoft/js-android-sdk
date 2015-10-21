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
import com.jaspersoft.android.sdk.client.oxm.report.ExportExecution;
import com.jaspersoft.android.sdk.client.oxm.report.ExportsRequest;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class RunReportExportsRequest extends BaseRequest<ExportExecution>  {

    private ExportsRequest request;
    private String executionId;

    public RunReportExportsRequest(JsRestClient jsRestClient) {
        super(jsRestClient, ExportExecution.class);
    }

    public RunReportExportsRequest(JsRestClient jsRestClient,
                                   ExportsRequest request, String executionId) {
        super(jsRestClient, ExportExecution.class);
        this.request = request;
        this.executionId = executionId;
    }

    @Override
    public ExportExecution loadDataFromNetwork() throws Exception {
        return getJsRestClient().runExportForReport(executionId, request);
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public ExportsRequest getRequest() {
        return request;
    }

    public void setRequest(ExportsRequest request) {
        this.request = request;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
