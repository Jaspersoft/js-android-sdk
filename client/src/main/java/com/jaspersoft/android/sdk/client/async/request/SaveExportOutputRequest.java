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

import java.io.File;

/**
 * @author Ivan Gadzhega
 * @since 1.8
 */
public class SaveExportOutputRequest extends BaseRequest<File> {

    private String executionId;
    private String exportOutput;
    private File outputFile;

    public SaveExportOutputRequest(JsRestClient jsRestClient, String executionId, String exportOutput, File outputFile) {
        super(jsRestClient, File.class);
        this.executionId = executionId;
        this.exportOutput = exportOutput;
        this.outputFile = outputFile;
    }

    @Override
    public File loadDataFromNetwork() throws Exception {
        getJsRestClient().saveExportOutputToFile(executionId, exportOutput, outputFile);
        return outputFile;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getExecutionId() {
        return executionId;
    }

    public String getExportOutput() {
        return exportOutput;
    }

    public File getOutputFile() {
        return outputFile;
    }

}
