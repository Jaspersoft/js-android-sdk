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
 * Request that downloads specified report attachment, once a report
 * has been generated and saves it in the specified file.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class SaveReportAttachmentRequest extends BaseRequest<File> {

    private String uuid;
    private String attachmentName;
    private File outputFile;

    /**
     * Creates a new instance of {@link SaveReportAttachmentRequest}.
     *
     * @param uuid           Universally Unique Identifier.
     * @param attachmentName One of the file names specified in the report descriptor.
     * @param outputFile     The file in which the attachment will be saved.
     */
    public SaveReportAttachmentRequest(JsRestClient jsRestClient, String uuid, String attachmentName, File outputFile) {
        super(jsRestClient, File.class);
        this.uuid = uuid;
        this.attachmentName = attachmentName;
        this.outputFile = outputFile;
    }

    @Override
    public File loadDataFromNetwork() throws Exception {
        getJsRestClient().saveReportAttachmentToFile(uuid, attachmentName, outputFile);
        return outputFile;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getUuid() {
        return uuid;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public File getOutputFile() {
        return outputFile;
    }

}
