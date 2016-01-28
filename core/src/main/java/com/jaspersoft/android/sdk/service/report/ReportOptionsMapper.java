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

import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class ReportOptionsMapper {
    private final String mBaseUrl;

    protected ReportOptionsMapper(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public static ReportOptionsMapper create(ServerVersion serverVersion, String baseUrl) {
        if (serverVersion.lessThanOrEquals(ServerVersion.v5_5)) {
            return new ReportOptionsMapper5_5(baseUrl);
        }
        if (serverVersion.equals(ServerVersion.v5_6)) {
            return new ReportOptionsMapper5_6(baseUrl);
        }
        return new ReportOptionsMapper5_6Plus(baseUrl);
    }

    public ReportExecutionRequestOptions transform(String reportUri, ReportExecutionOptions criteria) {
        ReportExecutionRequestOptions options = ReportExecutionRequestOptions.newRequest(reportUri);
        options.withAsync(true);
        options.withBaseUrl(mBaseUrl);
        options.withFreshData(criteria.getFreshData());
        options.withSaveDataSnapshot(criteria.getSaveSnapshot());
        options.withInteractive(criteria.getInteractive());
        options.withIgnorePagination(criteria.getIgnorePagination());
        options.withAllowInlineScripts(criteria.getAllowInlineScripts());
        options.withTransformerKey(criteria.getTransformerKey());
        options.withAnchor(criteria.getAnchor());
        options.withParameters(criteria.getParams());

        String attachmentPrefix = criteria.getAttachmentPrefix();
        if (attachmentPrefix != null) {
            options.withAttachmentsPrefix(escapeAttachmentPrefix(attachmentPrefix));
        }

        ReportMarkup markupType = criteria.getMarkupType();
        if (markupType != null) {
            options.withMarkupType(markupType.toString().toLowerCase());
        }

        ReportFormat format = criteria.getFormat();
        if (format != null) {
            options.withOutputFormat(format.toString().toLowerCase());
        }

        PageRange pageRange = criteria.getPageRange();
        if (pageRange != null) {
            options.withPages(pageRange.toString());
        }

        return options;
    }

    private String escapeAttachmentPrefix(String prefix) {
        try {
            return URLEncoder.encode(prefix, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            return prefix;
        }
    }
}
