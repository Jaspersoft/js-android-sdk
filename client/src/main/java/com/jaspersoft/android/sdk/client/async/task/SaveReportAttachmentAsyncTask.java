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

import java.io.File;

/**
 * <p>Declaration of the <strong>SaveReportAttachmentAsyncTask</strong> which is subclass of <strong>JsRestAsyncTask</strong>
 * abstract class and overrides <code>doInBackground(Object... arg0)</code> method from it.</p>
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 *
 * @deprecated Use {@link com.jaspersoft.android.sdk.client.async.request.SaveReportAttachmentRequest SaveReportAttachmentRequest}
 * instead.
 */

@Deprecated
public class SaveReportAttachmentAsyncTask extends JsRestAsyncTask<Object, Void> {

    private String uuid;
    private String attachmentName;
    private File outputFile;
    private String contentType;

    /**
     * Creates a new <strong>SaveReportAttachmentAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SaveReportAttachmentAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param uuid Universally Unique Identifier. As a side effect of storing the report output in the user session,
     *             the UUID in the URL is visible only to the currently logged user.
     * @param attachmentName One of the file names specified in the report xml. If the file parameter is not specified,
     *             the service returns the report descriptor.
     * @param outputFile The file in which the attachment will be saved.
     */
    public SaveReportAttachmentAsyncTask(int id, JsRestClient jsRestClient, String uuid, String attachmentName, File outputFile) {
        super(id, jsRestClient);
        init(uuid, attachmentName, outputFile);
    }

    /**
     * Creates a new <strong>SaveReportAttachmentAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SaveReportAttachmentAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param uuid Universally Unique Identifier. As a side effect of storing the report output in the user session,
     *             the UUID in the URL is visible only to the currently logged user.
     * @param attachmentName One of the file names specified in the report xml. If the file parameter is not specified,
     *             the service returns the report descriptor.
     * @param outputFile The file in which the attachment will be saved.
     */
    public SaveReportAttachmentAsyncTask(int id, String progressMessage, JsRestClient jsRestClient,
            String uuid, String attachmentName, File outputFile) {
        super(id, progressMessage, jsRestClient);
        init(uuid, attachmentName, outputFile);
    }

    /**
     * Creates a new <strong>SaveReportAttachmentAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SaveReportAttachmentAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param uuid Universally Unique Identifier. As a side effect of storing the report output in the user session,
     *             the UUID in the URL is visible only to the currently logged user.
     * @param attachmentName One of the file names specified in the report xml. If the file parameter is not specified,
     *             the service returns the report descriptor.
     * @param outputFile The file in which the attachment will be saved.
     */
    public SaveReportAttachmentAsyncTask(int id, String progressMessage, long showDialogTimeout,
            JsRestClient jsRestClient, String uuid, String attachmentName, File outputFile) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        init(uuid, attachmentName, outputFile);
    }

    private void init(String uuid, String attachmentName, File outputFile) {
        this.uuid = uuid;
        this.attachmentName = attachmentName;
        this.outputFile = outputFile;
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
            getJsRestClient().saveReportAttachmentToFile(uuid, attachmentName, outputFile);
        } catch (Exception e) {
            setTaskException(e);
        }
        return null;
    }

    // Getters

    public String getUuid() {
        return uuid;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
