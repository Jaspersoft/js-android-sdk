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

package com.jaspersoft.android.sdk.service.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class SearchCriteria {

    public static int ALL = 1;
    public static int REPORT = (1 << 1);
    public static int DASHBOARD = (1 << 2);
    public static int LEGACY_DASHBOARD = (1 << 3);

    private final Integer mCount;
    private final int mResourceMask;
    private final Boolean mRecursive;
    private final Boolean mForceFullPage;
    private final String mQuery;
    private final String mSortBy;

    private SearchCriteria(Builder builder) {
        mCount = builder.count;
        mResourceMask = builder.resourceMask;
        mRecursive = builder.recursive;
        mForceFullPage = builder.forceFullPage;
        mQuery = builder.query;
        mSortBy = builder.sort;
    }

    @NonNull
    public static SearchCriteria.Builder builder() {
        return new SearchCriteria.Builder();
    }

    @NonNull
    public Map<String, String> toMap() {
        Map<String, String> params = new HashMap<>();

        if (mCount != null) {
            params.put("limit", String.valueOf(mCount));
        }
        if (mRecursive != null) {
            params.put("recursive", String.valueOf(mRecursive));
        }
        if (mForceFullPage != null) {
            params.put("forceFullPage", String.valueOf(mForceFullPage));
        }
        if (mQuery != null && mQuery.length() > 0) {
            params.put("q", mQuery);
        }
        if (mSortBy != null) {
            params.put("sortBy", mSortBy);
        }

        populateTypes(params);
        return params;
    }

    private void populateTypes(Map<String, String> params) {
        boolean includeReport =
                (mResourceMask & REPORT) == REPORT || (mResourceMask & ALL) == ALL;
        if (includeReport) {
            params.put("type", "reportUnit");
        }
        boolean includeDashboard =
                (mResourceMask & DASHBOARD) == DASHBOARD || (mResourceMask & ALL) == ALL;
        if (includeDashboard) {
            params.put("type", "dashboard");
        }
        boolean includeLegacyDashboard =
                (mResourceMask & LEGACY_DASHBOARD) == LEGACY_DASHBOARD || (mResourceMask & ALL) == ALL;
        if (includeLegacyDashboard) {
            params.put("type", "legacyDashboard");
        }
    }

    public static class Builder {
        @Nullable
        private Integer count;
        private int resourceMask;
        @Nullable
        private Boolean recursive;
        @Nullable
        private Boolean forceFullPage;
        @Nullable
        private String query;
        @Nullable
        private String sort;

        public Builder limitCount(int count) {
            this.count = count;
            return this;
        }

        public Builder resourceMask(int resourceMask) {
            this.resourceMask = resourceMask;
            return this;
        }

        public Builder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        public Builder forceFullPage(boolean forceFullPage) {
            this.forceFullPage = forceFullPage;
            return this;
        }

        public Builder query(@Nullable String query) {
            this.query = query;
            return this;
        }

        public Builder sortByLabel() {
            this.sort = "label";
            return this;
        }

        public Builder sortByCreationDate() {
            this.sort = "creationDate";
            return this;
        }

        public SearchCriteria create() {
            return new SearchCriteria(this);
        }
    }
}
