/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

/**
 * @author Tom Koptel
 * @since 2.3
 */
abstract class ExportOptionsMapper {
    private final String mBaseUrl;

    protected ExportOptionsMapper(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public static ExportOptionsMapper create(ServerVersion serverVersion, String baseUrl) {
        if (serverVersion.lessThanOrEquals(ServerVersion.v5_5)) {
            return new ExportOptionsMapper5_5(baseUrl);
        }
        if (serverVersion.greaterThanOrEquals(ServerVersion.v5_6) &&
                serverVersion.lessThan(ServerVersion.v6)) {
            return new ExportOptionsMapper5_6and6_0(baseUrl);
        }
        if (serverVersion.greaterThanOrEquals(ServerVersion.v6) &&
                serverVersion.lessThan(ServerVersion.v6_2)) {
            return new ExportOptionsMapper6_06and6_1(baseUrl);
        }
        return new ExportOptionsMapper6_2(baseUrl);
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
            resultOptions.withAttachmentsPrefix(prefix);
        }

        ReportMarkup markup = options.getMarkupType();
        if (markup != null) {
            resultOptions.withMarkupType(markup.toString().toLowerCase());
        }

        resultOptions.withIgnorePagination(options.getIgnorePagination());
        resultOptions.withAnchor(options.getAnchor());
        resultOptions.withAllowInlineScripts(options.getAllowInlineScripts());
        resultOptions.withBaseUrl(mBaseUrl);
        return resultOptions;
    }
}
