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

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class ExportOptionsMapper5_6and6_0 extends ExportOptionsMapper {
    ExportOptionsMapper5_6and6_0(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public ExecutionRequestOptions transform(ReportExportOptions criteria) {
        ExecutionRequestOptions options = super.transform(criteria);
        options.withIgnorePagination(null);
        options.withMarkupType(null);
        return options;
    }
}
