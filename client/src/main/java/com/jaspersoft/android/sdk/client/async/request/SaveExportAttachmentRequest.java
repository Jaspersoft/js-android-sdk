/*
 * Copyright (C) 2012-2014 Jaspersoft Corporation. All rights reserved.
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

import java.io.File;

/**
 * @author Ivan Gadzhega
 * @since 1.8
 */
public class SaveExportAttachmentRequest extends BaseRequest<File> {

    private String executionId;
    private String exportOutput;
    private String attachmentName;
    private File outputFile;

    public SaveExportAttachmentRequest(JsRestClient jsRestClient, String executionId,
                                       String exportOutput, String attachmentName, File outputFile) {
        super(jsRestClient, File.class);
        this.executionId = executionId;
        this.exportOutput = exportOutput;
        this.attachmentName = attachmentName;
        this.outputFile = outputFile;
    }

    @Override
    public File loadDataFromNetwork() throws Exception {
        getJsRestClient().saveExportAttachmentToFile(executionId, exportOutput, attachmentName, outputFile);
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

    public String getAttachmentName() {
        return attachmentName;
    }

    public File getOutputFile() {
        return outputFile;
    }

}
