/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jaspersoft.android.sdk.service.Preconditions.checkArgument;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class SearchCriteria {
    static final int DEFAULT_OFFSET = 0;
    static final int DEFAULT_LIMIT = 100;

    public static int ALL = 1;
    public static int REPORT = (1 << 1);
    public static int DASHBOARD = (1 << 2);
    public static int LEGACY_DASHBOARD = (1 << 3);

    private final int mLimit;
    private final int mOffset;
    private final int mResourceMask;
    private final Boolean mRecursive;

    private final String mQuery;
    private final String mSortBy;
    private final String mFolderUri;

    private SearchCriteria(Builder builder) {
        mLimit = builder.limit;
        mOffset = builder.offset;
        mResourceMask = builder.resourceMask;
        mRecursive = builder.recursive;
        mQuery = builder.query;
        mSortBy = builder.sort;
        mFolderUri = builder.folderUri;
    }

    @NotNull
    public static SearchCriteria.Builder builder() {
        return new SearchCriteria.Builder();
    }

    @NotNull
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

    public static class Builder {
        private int limit = DEFAULT_LIMIT;
        private int offset = DEFAULT_OFFSET;
        private int resourceMask = Integer.MIN_VALUE;

        @Nullable
        private Boolean recursive;
        @Nullable
        private String query;
        @Nullable
        private String sort;
        @Nullable
        private String folderUri;

        public Builder limit(int limit) {
            checkArgument(limit >= 0, "Limit should be positive");
            this.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            checkArgument(offset >= 0, "Offset should be positive");
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

        public SearchCriteria create() {
            return new SearchCriteria(this);
        }
    }
}
