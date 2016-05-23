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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobOwner;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobSearchCriteriaMapper {

    @NotNull
    public Map<String, Object> transform(@NotNull JobSearchCriteria criteria) {
        Map<String, Object> searchParams = new HashMap<>(11);

        String reportUri = criteria.getReportUri();
        if (reportUri != null) {
            searchParams.put("reportUnitURI", reportUri);
        }

        JobOwner owner = criteria.getOwner();
        if (owner != null) {
            searchParams.put("owner", owner.toString());
        }

        String label = criteria.getLabel();
        if (label != null) {
            searchParams.put("label", label);
        }

        int offset = criteria.getOffset();
        searchParams.put("startIndex", String.valueOf(offset));

        int limit = criteria.getLimit();
        if (limit == JobSearchCriteria.UNLIMITED_ROW_NUMBER) {
            limit = -1;
        }
        searchParams.put("numberOfRows", String.valueOf(limit));

        JobSortType jobSortType = criteria.getSortType();
        if (jobSortType != null) {
            searchParams.put("sortType", jobSortType.toString());
        }

        Boolean ascending = criteria.getAscending();
        if (ascending != null) {
            searchParams.put("isAscending", String.valueOf(ascending));
        }

        return searchParams;
    }
}
