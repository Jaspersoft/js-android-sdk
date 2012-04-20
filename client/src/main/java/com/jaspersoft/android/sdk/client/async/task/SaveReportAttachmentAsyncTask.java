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

import java.io.File;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class SaveReportAttachmentAsyncTask extends JsRestAsyncTask<Object, Void> {

    private String uuid;
    private String attachmentName;
    private File outputFile;
    private String contentType;

    public SaveReportAttachmentAsyncTask(int id, JsRestClient jsRestClient, String uuid, String attachmentName, File outputFile) {
        super(id, jsRestClient);
        init(uuid, attachmentName, outputFile);
    }

    public SaveReportAttachmentAsyncTask(int id, String progressMessage, JsRestClient jsRestClient,
            String uuid, String attachmentName, File outputFile) {
        super(id, progressMessage, jsRestClient);
        init(uuid, attachmentName, outputFile);
    }

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
