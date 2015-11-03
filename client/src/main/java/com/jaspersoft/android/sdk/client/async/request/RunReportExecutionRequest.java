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
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;

import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 1.8
 */
public class RunReportExecutionRequest extends BaseRequest<ReportExecutionResponse> {

    private ReportExecutionRequest request;

    public RunReportExecutionRequest(JsRestClient jsRestClient, String reportUri,
                                     String outputFormat, List<ReportParameter> parameters) {
        this(jsRestClient, reportUri, outputFormat, parameters, true);
    }

    public RunReportExecutionRequest(JsRestClient jsRestClient, String reportUri, String outputFormat,
                                     List<ReportParameter> parameters, boolean interactive) {
        this(jsRestClient, reportUri, outputFormat, parameters, interactive, null);
    }

    public RunReportExecutionRequest(JsRestClient jsRestClient, String reportUri, String outputFormat,
                                     List<ReportParameter> parameters, boolean interactive, String attachmentsPrefix) {
        super(jsRestClient, ReportExecutionResponse.class);
        request = new ReportExecutionRequest();
        request.setReportUnitUri(reportUri);
        request.setOutputFormat(outputFormat);
        request.setParameters(parameters);
        request.setInteractive(interactive);
        request.setAttachmentsPrefix(attachmentsPrefix);
    }

    public RunReportExecutionRequest(JsRestClient jsRestClient, ReportExecutionRequest request) {
        super(jsRestClient, ReportExecutionResponse.class);
        this.request = request;
    }

    @Override
    public ReportExecutionResponse loadDataFromNetwork() throws Exception {
        return getJsRestClient().runReportExecution(request);
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public ReportExecutionRequest getRequest() {
        return request;
    }

}
