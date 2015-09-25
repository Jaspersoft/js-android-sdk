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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class SearchCriteria {
    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 100;

    public static int ALL = 1;
    public static int REPORT = (1 << 1);
    public static int DASHBOARD = (1 << 2);
    public static int LEGACY_DASHBOARD = (1 << 3);

    private final int mLimit;
    private final int mOffset;
    private final int mResourceMask;
    private final Boolean mRecursive;
    private final Boolean mForceFullPage;
    private final Boolean mForceTotalCount;
    private final String mQuery;
    private final String mSortBy;
    private final String mFolderUri;

    private SearchCriteria(Builder builder) {
        mLimit = builder.limit;
        mOffset = builder.offset;
        mResourceMask = builder.resourceMask;
        mRecursive = builder.recursive;
        mForceFullPage = builder.forceFullPage;
        mForceTotalCount = builder.forceTotalCount;
        mQuery = builder.query;
        mSortBy = builder.sort;
        mFolderUri = builder.folderUri;
    }

    @NonNull
    public static SearchCriteria.Builder builder() {
        return new SearchCriteria.Builder();
    }

    @NonNull
    public static SearchCriteria none() {
        return builder().create();
    }

    public int getLimit() {
        return mLimit;
    }

    public int getOffset() {
        return mOffset;
    }

    @Nullable
    public Boolean getForceFullPage() {
        return mForceFullPage;
    }

    @Nullable
    public Boolean getForceTotalCount() {
        return mForceTotalCount;
    }

    @Nullable
    public String getQuery() {
        return mQuery;
    }

    @Nullable
    public Boolean getRecursive() {
        return mRecursive;
    }

    @Nullable
    public String getSortBy() {
        return mSortBy;
    }

    public int getResourceMask() {
        return mResourceMask;
    }

    @Nullable
    public String getFolderUri() {
        return mFolderUri;
    }

    @NonNull
    public SearchCriteria.Builder newBuilder() {
        SearchCriteria.Builder builder = builder();

        if (mRecursive != null) {
            builder.recursive(mRecursive);
        }
        if (mForceFullPage != null) {
            builder.forceFullPage(mForceFullPage);
        }
        if (mForceTotalCount != null) {
            builder.forceTotalCount(mForceTotalCount);
        }
        if (mQuery != null) {
            builder.query(mQuery);
        }
        if (mSortBy != null) {
            builder.sortBy(mSortBy);
        }
        if (mFolderUri != null) {
            builder.folderUri(mFolderUri);
        }

        builder.resourceMask(mResourceMask);
        builder.limit(mLimit);
        builder.offset(mOffset);

        return builder;
    }

    @NonNull
    public Map<String, Object> toMap() {
        Map<String, Object> params = new HashMap<>();

        if (mLimit != DEFAULT_LIMIT) {
            params.put("limit", String.valueOf(mLimit));
        }
        if (mOffset != DEFAULT_OFFSET) {
            params.put("offset", String.valueOf(mOffset));
        }
        if (mRecursive != null) {
            params.put("recursive", String.valueOf(mRecursive));
        }
        if (mForceFullPage != null) {
            params.put("forceFullPage", String.valueOf(mForceFullPage));
        }
        if (mForceTotalCount != null) {
            params.put("forceTotalCount", String.valueOf(mForceTotalCount));
        }
        if (mQuery != null && mQuery.length() > 0) {
            params.put("q", mQuery);
        }
        if (mSortBy != null) {
            params.put("sortBy", mSortBy);
        }
        if (mFolderUri != null) {
            params.put("folderUri", mFolderUri);
        }

        populateTypes(params);
        return params;
    }

    private void populateTypes(Map<String, Object> params) {
        Set<String> types = new HashSet<>();

        boolean includeReport =
                (mResourceMask & REPORT) == REPORT || (mResourceMask & ALL) == ALL;
        if (includeReport) {
            types.add("reportUnit");
        }
        boolean includeDashboard =
                (mResourceMask & DASHBOARD) == DASHBOARD || (mResourceMask & ALL) == ALL;
        if (includeDashboard) {
            types.add("dashboard");
        }
        boolean includeLegacyDashboard =
                (mResourceMask & LEGACY_DASHBOARD) == LEGACY_DASHBOARD || (mResourceMask & ALL) == ALL;
        if (includeLegacyDashboard) {
            types.add("legacyDashboard");
        }

        if (!types.isEmpty()) {
            params.put("type", types);
        }
    }

    public static class Builder {
        private int limit = DEFAULT_LIMIT;
        private int offset = DEFAULT_OFFSET;
        private int resourceMask = Integer.MIN_VALUE;

        @Nullable
        private Boolean recursive;
        @Nullable
        private Boolean forceFullPage;
        @Nullable
        public Boolean forceTotalCount;
        @Nullable
        private String query;
        @Nullable
        private String sort;
        @Nullable
        private String folderUri;

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
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

        public Builder folderUri(@Nullable String folderUri) {
            this.folderUri = folderUri;
            return this;
        }

        /**
         * Internal use. Mutating sortBy value.
         * @param sort either 'label' or 'creationDate'
         * @return chain builder instance
         */
        Builder sortBy(String sort) {
            this.sort = sort;
            return this;
        }

        /**
         * Internal use. Mutating forceFullPage value.
         * @param forceFullPage either true or false
         * @return chain builder instance
         */
        Builder forceFullPage(boolean forceFullPage) {
            this.forceFullPage = forceFullPage;
            return this;
        }

        /**
         * Internal use. Mutating forceTotalCount value.
         * @param forceTotalCount either true or false
         * @return chain builder instance
         */
        Builder forceTotalCount(@Nullable Boolean forceTotalCount) {
            this.forceTotalCount = forceTotalCount;
            return this;
        }

        public SearchCriteria create() {
            return new SearchCriteria(this);
        }
    }
}
