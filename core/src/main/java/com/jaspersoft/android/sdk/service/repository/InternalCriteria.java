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

package com.jaspersoft.android.sdk.service.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jaspersoft.android.sdk.service.repository.SearchCriteria.DEFAULT_LIMIT;
import static com.jaspersoft.android.sdk.service.repository.SearchCriteria.DEFAULT_OFFSET;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class InternalCriteria {
    private final int mLimit;
    private final int mOffset;
    private final int mResourceMask;
    private final Boolean mRecursive;
    private final Boolean mForceFullPage;
    private final Boolean mForceTotalCount;
    private final String mQuery;
    private final SortType mSortBy;
    private final String mFolderUri;

    private InternalCriteria(Builder builder) {
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

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public static InternalCriteria from(SearchCriteria criteria) {
        return InternalCriteria.builder()
                .limit(criteria.getLimit())
                .offset(criteria.getOffset())
                .resourceMask(criteria.getResourceMask())
                .recursive(criteria.getRecursive())
                .folderUri(criteria.getFolderUri())
                .query(criteria.getQuery())
                .sortBy(criteria.getSortBy())
                .create();
    }

    public int getLimit() {
        return mLimit;
    }

    public int getOffset() {
        return mOffset;
    }

    @Nullable
    public String getFolderUri() {
        return mFolderUri;
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

    public int getResourceMask() {
        return mResourceMask;
    }

    @Nullable
    public SortType getSortBy() {
        return mSortBy;
    }

    @NotNull
    public Builder newBuilder() {
        Builder builder = builder();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InternalCriteria criteria = (InternalCriteria) o;

        if (mLimit != criteria.mLimit) return false;
        if (mOffset != criteria.mOffset) return false;
        if (mResourceMask != criteria.mResourceMask) return false;
        if (mRecursive != null ? !mRecursive.equals(criteria.mRecursive) : criteria.mRecursive != null)
            return false;
        if (mForceFullPage != null ? !mForceFullPage.equals(criteria.mForceFullPage) : criteria.mForceFullPage != null)
            return false;
        if (mForceTotalCount != null ? !mForceTotalCount.equals(criteria.mForceTotalCount) : criteria.mForceTotalCount != null)
            return false;
        if (mQuery != null ? !mQuery.equals(criteria.mQuery) : criteria.mQuery != null)
            return false;
        if (mSortBy != null ? !mSortBy.equals(criteria.mSortBy) : criteria.mSortBy != null)
            return false;
        return !(mFolderUri != null ? !mFolderUri.equals(criteria.mFolderUri) : criteria.mFolderUri != null);
    }

    @Override
    public int hashCode() {
        int result = mLimit;
        result = 31 * result + mOffset;
        result = 31 * result + mResourceMask;
        result = 31 * result + (mRecursive != null ? mRecursive.hashCode() : 0);
        result = 31 * result + (mForceFullPage != null ? mForceFullPage.hashCode() : 0);
        result = 31 * result + (mForceTotalCount != null ? mForceTotalCount.hashCode() : 0);
        result = 31 * result + (mQuery != null ? mQuery.hashCode() : 0);
        result = 31 * result + (mSortBy != null ? mSortBy.hashCode() : 0);
        result = 31 * result + (mFolderUri != null ? mFolderUri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InternalCriteria{" +
                "mFolderUri='" + mFolderUri + '\'' +
                ", mLimit=" + mLimit +
                ", mOffset=" + mOffset +
                ", mResourceMask=" + mResourceMask +
                ", mRecursive=" + mRecursive +
                ", mForceFullPage=" + mForceFullPage +
                ", mForceTotalCount=" + mForceTotalCount +
                ", mQuery='" + mQuery + '\'' +
                ", mSortBy='" + mSortBy + '\'' +
                '}';
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
        private SortType sort;
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

        public Builder recursive(Boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        public Builder query(@Nullable String query) {
            this.query = query;
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
        public Builder sortBy(SortType sort) {
            this.sort = sort;
            return this;
        }

        /**
         * Internal use. Mutating forceFullPage value.
         * @param forceFullPage either true or false
         * @return chain builder instance
         */
        public Builder forceFullPage(Boolean forceFullPage) {
            this.forceFullPage = forceFullPage;
            return this;
        }

        /**
         * Internal use. Mutating forceTotalCount value.
         * @param forceTotalCount either true or false
         * @return chain builder instance
         */
        public Builder forceTotalCount(Boolean forceTotalCount) {
            this.forceTotalCount = forceTotalCount;
            return this;
        }

        public InternalCriteria create() {
            return new InternalCriteria(this);
        }
    }
}
