/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */
package com.jaspersoft.android.sdk.service.report;

import android.support.annotation.NonNull;

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ExecutionOptionsDataMapper {

    private static class InstanceHolder {
        private static ExecutionOptionsDataMapper INSTANCE = new ExecutionOptionsDataMapper();
    }

    public static ExecutionOptionsDataMapper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ReportExecutionRequestOptions transformReportOptions(@NonNull String reportUri, @NonNull String serverUrl, @NonNull ExecutionCriteria configuration) {
        ReportExecutionRequestOptions options = ReportExecutionRequestOptions.newRequest(reportUri);
        adaptFields(serverUrl, configuration, options);
        return options;
    }

    public ExecutionRequestOptions transformExportOptions(@NonNull String serverUrl, @NonNull ExecutionCriteria configuration) {
        ExecutionRequestOptions options = ExecutionRequestOptions.create();
        adaptFields(serverUrl, configuration, options);
        return options;
    }

    private void adaptFields(@NonNull String serverUrl, @NonNull ExecutionCriteria criteria, ExecutionRequestOptions options) {
        options.withOutputFormat(Helper.adaptFormat(criteria.getFormat()));
        options.withAttachmentsPrefix(Helper.adaptAttachmentPrefix(criteria.getAttachmentPrefix()));

        options.withFreshData(criteria.isFreshData());
        options.withSaveDataSnapshot(criteria.isSaveSnapshot());
        options.withInteractive(criteria.isInteractive());
        options.withPages(criteria.getPages());
        options.withParameters(criteria.getParams());

        options.withAsync(true);
        options.withBaseUrl(serverUrl);
    }

    static class Helper {
        static String adaptFormat(ExecutionCriteria.Format format) {
            if (format == null) {
                return null;
            }
            return format.toString();
        }

        static String adaptAttachmentPrefix(String attachmentsPrefix) {
            if (attachmentsPrefix == null) {
                return null;
            }
            try {
                return URLEncoder.encode(attachmentsPrefix, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException("This should not be possible", e);
            }
        }
    }
}
