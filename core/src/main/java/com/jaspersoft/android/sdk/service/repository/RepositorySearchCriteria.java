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

package com.jaspersoft.android.sdk.service.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jaspersoft.android.sdk.service.internal.Preconditions.checkArgument;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class RepositorySearchCriteria {
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_LIMIT = 100;

    public static int ALL = 1;
    public static int REPORT = (1 << 1);
    public static int DASHBOARD = (1 << 2);
    public static int LEGACY_DASHBOARD = (1 << 3);
    public static int FOLDER = (1 << 4);
    public static int REPORT_OPTION = (1 << 5);
    public static int FILE = (1 << 6);

    private final int mLimit;
    private final int mOffset;
    private final int mResourceMask;
    private final Boolean mRecursive;

    private final String mQuery;
    private final SortType mSort;
    private final AccessType mAccessType;
    private final String mFolderUri;

    private RepositorySearchCriteria(Builder builder) {
        mLimit = builder.limit;
        mOffset = builder.offset;
        mResourceMask = builder.resourceMask;
        mRecursive = builder.recursive;
        mQuery = builder.query;
        mSort = builder.sortType;
        mAccessType = builder.accessType;
        mFolderUri = builder.folderUri;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public static RepositorySearchCriteria empty() {
        return builder().build();
    }

    public int getLimit() {
        return mLimit;
    }

    public int getOffset() {
        return mOffset;
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
    public SortType getSortBy() {
        return mSort;
    }

    public int getResourceMask() {
        return mResourceMask;
    }

    @Nullable
    public String getFolderUri() {
        return mFolderUri;
    }

    public AccessType getAccessType() {
        return mAccessType;
    }

    public static class Builder {
        private int limit = DEFAULT_LIMIT;
        private int offset = DEFAULT_OFFSET;
        private int resourceMask = Integer.MIN_VALUE;

        @Nullable
        private Boolean recursive;
        @Nullable
        private String query;
        @Nullable
        private String folderUri;
        @Nullable
        private SortType sortType;
        @Nullable
        private AccessType accessType;

        public Builder withLimit(int limit) {
            checkArgument(limit >= 0, "Limit should be positive");
            this.limit = limit;
            return this;
        }

        public Builder withOffset(int offset) {
            checkArgument(offset >= 0, "Offset should be positive");
            this.offset = offset;
            return this;
        }

        public Builder withResourceMask(int resourceMask) {
            this.resourceMask = resourceMask;
            return this;
        }

        public Builder withRecursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        public Builder withQuery(@Nullable String query) {
            this.query = query;
            return this;
        }

        public Builder withSortType(@Nullable SortType sortType) {
            this.sortType = sortType;
            return this;
        }

        public Builder withAccessType(@Nullable AccessType accessType) {
            this.accessType = accessType;
            return this;
        }

        public Builder withFolderUri(@Nullable String folderUri) {
            this.folderUri = folderUri;
            return this;
        }

        public RepositorySearchCriteria build() {
            return new RepositorySearchCriteria(this);
        }
    }
}
