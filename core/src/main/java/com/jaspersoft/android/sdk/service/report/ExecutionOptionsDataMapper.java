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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ExecutionOptionsDataMapper {
    private final String mBaseUrl;

    ExecutionOptionsDataMapper(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public ReportExecutionRequestOptions transformRunReportOptions(@NotNull String reportUri,
                                                                   @NotNull ServerVersion serverVersion,
                                                                   @NotNull RunReportCriteria criteria) {
        ReportExecutionRequestOptions options = ReportExecutionRequestOptions.newRequest(reportUri);
        mapCommonCriterion(criteria, serverVersion, options);
        options.withAsync(true);
        options.withParameters(criteria.getParams());
        return options;
    }

    public ExecutionRequestOptions transformExportOptions(@NotNull RunExportCriteria configuration,
                                                          @NotNull ServerVersion serverVersion) {
        ExecutionRequestOptions options = ExecutionRequestOptions.create();
        mapCommonCriterion(configuration, serverVersion, options);
        if (serverVersion.lessThanOrEquals(ServerVersion.v5_5)) {
            options.withSaveDataSnapshot(null);
            options.withFreshData(null);
            options.withInteractive(null);
        }
        return options;
    }

    private void mapCommonCriterion(@NotNull ExecutionCriteria criteria,
                                    @NotNull ServerVersion version,
                                    @NotNull ExecutionRequestOptions options) {
        options.withOutputFormat(Helper.adaptFormat(criteria.getFormat()));
        options.withAttachmentsPrefix(Helper.adaptAttachmentPrefix(criteria.getAttachmentPrefix()));

        options.withFreshData(criteria.isFreshData());
        options.withSaveDataSnapshot(criteria.isSaveSnapshot());
        options.withInteractive(criteria.isInteractive());
        options.withPages(criteria.getPages());

        if (version.lessThanOrEquals(ServerVersion.v5_5)) {
            options.withBaseUrl(null);
            options.withAllowInlineScripts(null);
            if (options.getOutputFormat() == null) {
                options.withOutputFormat("HTML");
            }
        } else if (version.equals(ServerVersion.v5_6)) {
            options.withInteractive(false);
        } else {
            options.withBaseUrl(mBaseUrl);
        }
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
