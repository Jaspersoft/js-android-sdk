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
import com.jaspersoft.android.sdk.client.oxm.ReportAttachment;

import java.io.File;
import java.util.List;

/**
 * Request that downloads specified list of report attachments, once a report
 * has been generated and saves them in the specified directory.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class SaveReportAttachmentsRequest extends BaseRequest<File> {

    private String uuid;
    private List<ReportAttachment> reportAttachments;
    private File outputDir;

    /**
     * Creates a new instance of {@link SaveReportAttachmentsRequest}.
     *
     * @param uuid              Universally Unique Identifier.
     * @param reportAttachments List of the file names specified in the report descriptor.
     * @param outputDir         The directory in which these attachments will be saved.
     */
    public SaveReportAttachmentsRequest(JsRestClient jsRestClient, String uuid, List<ReportAttachment> reportAttachments, File outputDir) {
        super(jsRestClient, File.class);
        this.uuid = uuid;
        this.reportAttachments = reportAttachments;
        this.outputDir = outputDir;
    }

    @Override
    public File loadDataFromNetwork() throws Exception {
        for (ReportAttachment attachment : reportAttachments) {
            String attachmentName = attachment.getName();
            File outputFile = new File(outputDir, attachmentName);
            getJsRestClient().saveReportAttachmentToFile(uuid, attachmentName, outputFile);
        }
        return outputDir;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getUuid() {
        return uuid;
    }

    public List<ReportAttachment> getReportAttachments() {
        return reportAttachments;
    }

    public File getOutputDir() {
        return outputDir;
    }

}
