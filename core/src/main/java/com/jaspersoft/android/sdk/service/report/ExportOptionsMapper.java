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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class ExportOptionsMapper {
    private final String mBaseUrl;

    protected ExportOptionsMapper(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public static ExportOptionsMapper create(ServerVersion serverVersion, String baseUrl) {
        if (serverVersion.greaterThanOrEquals(ServerVersion.v6_2)) {
            return new ExportOptionsMapper6_2(baseUrl);
        }
        return new ExportOptionsMapper5_5and6_1(baseUrl);
    }

    public ExecutionRequestOptions transform(ReportExportOptions options) {
        ExecutionRequestOptions resultOptions = ExecutionRequestOptions.create();

        ReportFormat format = options.getFormat();
        resultOptions.withOutputFormat(format.toString().toLowerCase());

        PageRange pageRange = options.getPageRange();
        if (pageRange != null) {
            resultOptions.withPages(pageRange.toString());
        }

        String prefix = options.getAttachmentPrefix();
        if (prefix != null) {
            resultOptions.withAttachmentsPrefix(escapeAttachmentPrefix(prefix));
        }

        resultOptions.withIgnorePagination(options.getIgnorePagination());
        resultOptions.withAnchor(options.getAnchor());
        resultOptions.withAllowInlineScripts(options.getAllowInlineScripts());
        resultOptions.withBaseUrl(mBaseUrl);
        return resultOptions;
    }

    private String escapeAttachmentPrefix(String prefix) {
        try {
            return URLEncoder.encode(prefix, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            return prefix;
        }
    }
}
