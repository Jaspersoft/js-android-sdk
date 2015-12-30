/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.execution.AttachmentDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ExportDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class AttachmentsFactory {
    private final ExportExecutionApi mExportExecutionApi;

    AttachmentsFactory(ExportExecutionApi exportExecutionApi) {
        mExportExecutionApi = exportExecutionApi;
    }

    public List<ReportAttachment> create(ExportDescriptor export, String execId) {
        String exportId = export.getId();
        Set<AttachmentDescriptor> rawAttachments = export.getAttachments();
        List<ReportAttachment> attachments = new ArrayList<>(rawAttachments.size());
        for (AttachmentDescriptor attachment : rawAttachments) {
            String fileName = attachment.getFileName();
            ReportAttachment reportAttachment = new ReportAttachmentImpl(
                    mExportExecutionApi,
                    execId,
                    exportId,
                    fileName
            );
            attachments.add(reportAttachment);
        }
        return attachments;
    }
}
