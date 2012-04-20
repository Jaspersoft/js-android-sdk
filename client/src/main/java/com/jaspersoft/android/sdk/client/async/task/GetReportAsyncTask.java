/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.android.sdk.client.async.task;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.ReportDescriptor;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class GetReportAsyncTask extends JsRestAsyncTask<Object, ReportDescriptor> {

    private ResourceDescriptor resourceDescriptor;
    private String outputFormat;

    public GetReportAsyncTask(int id, JsRestClient jsRestClient,
            ResourceDescriptor resourceDescriptor, String outputFormat) {
        super(id, jsRestClient);
        this.resourceDescriptor = resourceDescriptor;
        this.outputFormat = outputFormat;
    }

    public GetReportAsyncTask(int id, String progressMessage, JsRestClient jsRestClient,
            ResourceDescriptor resourceDescriptor, String outputFormat) {
        super(id, progressMessage, jsRestClient);
        this.resourceDescriptor = resourceDescriptor;
        this.outputFormat = outputFormat;
    }

    public GetReportAsyncTask(int id, String progressMessage, long showDialogTimeout, JsRestClient jsRestClient,
            ResourceDescriptor resourceDescriptor, String outputFormat) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        this.resourceDescriptor = resourceDescriptor;
        this.outputFormat = outputFormat;
    }


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
