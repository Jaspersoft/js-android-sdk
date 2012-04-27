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
import com.jaspersoft.android.sdk.client.oxm.ReportAttachment;

import java.io.File;
import java.util.List;

/**
 * <p>Declaration of the <strong>SaveReportAttachmentsAsyncTask</strong> which is subclass of <strong>JsRestAsyncTask</strong>
 * abstract class and overrides <code>doInBackground(Object... arg0)</code> method from it.</p>
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class SaveReportAttachmentsAsyncTask extends JsRestAsyncTask<Object, Void> {

    private String uuid;
    private List<ReportAttachment> reportAttachments;
    private File outputDir;

    /**
     * Creates a new <strong>SaveReportAttachmentsAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SaveReportAttachmentsAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param uuid Universally Unique Identifier. As a side effect of storing the report output in the user session,
     *             the UUID in the URL is visible only to the currently logged user.
     * @param reportAttachments One of the file names specified in the report xml. If the file parameter is not specified,
     *             the service returns the report descriptor.
     * @param outputDir The file in which the attachment will be saved.
     */
    public SaveReportAttachmentsAsyncTask(int id, JsRestClient jsRestClient,
            String uuid, List<ReportAttachment> reportAttachments, File outputDir) {
        super(id, jsRestClient);
        init(uuid, reportAttachments, outputDir);
    }

    /**
     * Creates a new <strong>SaveReportAttachmentsAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SaveReportAttachmentsAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param uuid Universally Unique Identifier. As a side effect of storing the report output in the user session,
     *             the UUID in the URL is visible only to the currently logged user.
     * @param reportAttachments One of the file names specified in the report xml. If the file parameter is not specified,
     *             the service returns the report descriptor.
     * @param outputDir The file in which the attachment will be saved.
     */
    public SaveReportAttachmentsAsyncTask(int id, String progressMessage, JsRestClient jsRestClient,
            String uuid, List<ReportAttachment> reportAttachments, File outputDir) {
        super(id, progressMessage, jsRestClient);
        init(uuid, reportAttachments, outputDir);
    }

    /**
     * Creates a new <strong>SaveReportAttachmentsAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SaveReportAttachmentsAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param uuid Universally Unique Identifier. As a side effect of storing the report output in the user session,
     *             the UUID in the URL is visible only to the currently logged user.
     * @param reportAttachments One of the file names specified in the report xml. If the file parameter is not specified,
     *             the service returns the report descriptor.
     * @param outputDir The file in which the attachment will be saved.
     */
    public SaveReportAttachmentsAsyncTask(int id, String progressMessage, long showDialogTimeout,
            JsRestClient jsRestClient, String uuid, List<ReportAttachment> reportAttachments, File outputDir) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        init(uuid, reportAttachments, outputDir);
    }

    private void init(String uuid, List<ReportAttachment> reportAttachments, File outputDir) {
        this.uuid = uuid;
        this.reportAttachments = reportAttachments;
        this.outputDir = outputDir;
    }

    /**
     * Overrides the <code>doInBackground(Object... arg0)</code> method by calling <strong>JsRestClient</strong>
     * <code>saveReportAttachmentToFile(...)</code> method.
     *
     * @param arg0 the parameters of the <strong>Asynchronous task</strong>. Current implementation does not use this params.
     * @return nothing.
     */
    @Override
    protected Void doInBackground(Object... arg0) {
        super.doInBackground(arg0);
        try {
            for (ReportAttachment attachment : reportAttachments) {
                String attachmentName = attachment.getName();
                File outputFile = new File(outputDir, attachmentName);
                getJsRestClient().saveReportAttachmentToFile(uuid, attachmentName, outputFile);
            }
        } catch (Exception e) {
            setTaskException(e);
        }
        return null;
    }

    // Getters

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
