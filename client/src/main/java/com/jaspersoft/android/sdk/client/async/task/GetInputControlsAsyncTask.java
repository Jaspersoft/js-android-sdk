/*
 * Copyright (C) 2012 Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.client.async.task;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.control.InputControl;

import java.util.List;

/**
 * <p>Declaration of the <strong>GetInputControlsAsyncTask</strong> which is subclass of <strong>JsRestAsyncTask</strong>
 * abstract class and overrides <code>doInBackground(Object... arg0)</code> method from it.</p>
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.4
 */
public class GetInputControlsAsyncTask extends JsRestAsyncTask<Object, List<InputControl>> {

    private String reportUri;

    /**
     * Creates a new <strong>GetInputControlsAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetInputControlsAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param reportUri <strong>Report</strong> Uri.
     */
    public GetInputControlsAsyncTask(int id, JsRestClient jsRestClient, String reportUri) {
        super(id, jsRestClient);
        this.reportUri = reportUri;
    }

    /**
     * Creates a new <strong>GetInputControlsAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetInputControlsAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param reportUri <strong>Report</strong> Uri.
     */
    public GetInputControlsAsyncTask(int id, String progressMessage, JsRestClient jsRestClient, String reportUri) {
        super(id, progressMessage, jsRestClient);
        this.reportUri = reportUri;
    }

    /**
     *
     * @param id <strong>GetInputControlsAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param reportUri <strong>Report</strong> Uri.
     */
    public GetInputControlsAsyncTask(int id, String progressMessage,
                                     long showDialogTimeout, JsRestClient jsRestClient, String reportUri) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        this.reportUri = reportUri;
    }

    /**
     * Overrides the <code>doInBackground(Object... arg0)</code> method by calling <strong>JsRestClient</strong>
     * <code>getInputControlsForReport(String reportUri)</code> method.
     *
     * @param arg0 the parameters of the <strong>Asynchronous task</strong>. Current implementation does not use this params.
     * @return the list of input controls for the report with specified URI
     */
    @Override
    protected List<InputControl> doInBackground(Object... arg0) {
        super.doInBackground(arg0);
        try {
            return getJsRestClient().getInputControlsForReport(reportUri);
        } catch (Exception e) {
            setTaskException(e);
            return null;
        }
    }

    // Getters

    public String getReportUri() {
        return reportUri;
    }
}
