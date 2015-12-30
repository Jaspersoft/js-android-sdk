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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportExecutionOptions {
    private final Boolean mFreshData;
    private final Boolean mSaveSnapshot;
    private final Boolean mInteractive;
    private final Boolean mIgnorePagination;
    private final Boolean mAllowInlineScripts;
    private final String mTransformerKey;
    private final String mAttachmentPrefix;
    private final String mAnchor;
    private final ReportMarkup mMarkupType;
    private final ReportFormat mFormat;
    private final PageRange mPageRange;
    private final List<ReportParameter> mParams;

    @TestOnly
    ReportExecutionOptions(Boolean freshData,
                           Boolean saveSnapshot,
                           Boolean interactive,
                           Boolean ignorePagination,
                           Boolean allowInlineScripts,
                           String transformerKey,
                           String attachmentPrefix,
                           String anchor,
                           ReportMarkup markupType,
                           ReportFormat format,
                           PageRange pageRange,
                           List<ReportParameter> params) {
        mFreshData = freshData;
        mSaveSnapshot = saveSnapshot;
        mInteractive = interactive;
        mIgnorePagination = ignorePagination;
        mAllowInlineScripts = allowInlineScripts;
        mTransformerKey = transformerKey;
        mAttachmentPrefix = attachmentPrefix;
        mAnchor = anchor;
        mMarkupType = markupType;
        mFormat = format;
        mPageRange = pageRange;
        mParams = params;
    }


    @NotNull
    public Builder newBuilder() {
        return new Builder()
                .withFreshData(mFreshData)
                .withSaveSnapshot(mSaveSnapshot)
                .withInteractive(mInteractive)
                .withIgnorePagination(mIgnorePagination)
                .withAllowInlineScripts(mAllowInlineScripts)
                .withTransformerKey(mTransformerKey)
                .withAttachmentPrefix(mAttachmentPrefix)
                .withAnchor(mAnchor)
                .withMarkupType(mMarkupType)
                .withFormat(mFormat)
                .withPageRange(mPageRange)
                .withParams(mParams);
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public Boolean getFreshData() {
        return mFreshData;
    }

    @Nullable
    public Boolean getSaveSnapshot() {
        return mSaveSnapshot;
    }

    @Nullable
    public Boolean getInteractive() {
        return mInteractive;
    }

    @Nullable
    public Boolean getIgnorePagination() {
        return mIgnorePagination;
    }

    @Nullable
    public Boolean getAllowInlineScripts() {
        return mAllowInlineScripts;
    }

    @Nullable
    public String getTransformerKey() {
        return mTransformerKey;
    }

    @Nullable
    public String getAttachmentPrefix() {
        return mAttachmentPrefix;
    }

    @Nullable
    public String getAnchor() {
        return mAnchor;
    }

    @Nullable
    public ReportMarkup getMarkupType() {
        return mMarkupType;
    }

    @Nullable
    public ReportFormat getFormat() {
        return mFormat;
    }

    @Nullable
    public PageRange getPageRange() {
        return mPageRange;
    }

    @Nullable
    public List<ReportParameter> getParams() {
        return mParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportExecutionOptions criteria = (ReportExecutionOptions) o;

        if (mAllowInlineScripts != null ? !mAllowInlineScripts.equals(criteria.mAllowInlineScripts) : criteria.mAllowInlineScripts != null)
            return false;
        if (mAnchor != null ? !mAnchor.equals(criteria.mAnchor) : criteria.mAnchor != null) return false;
        if (mAttachmentPrefix != null ? !mAttachmentPrefix.equals(criteria.mAttachmentPrefix) : criteria.mAttachmentPrefix != null)
            return false;
        if (mFormat != criteria.mFormat) return false;
        if (mFreshData != null ? !mFreshData.equals(criteria.mFreshData) : criteria.mFreshData != null) return false;
        if (mIgnorePagination != null ? !mIgnorePagination.equals(criteria.mIgnorePagination) : criteria.mIgnorePagination != null)
            return false;
        if (mInteractive != null ? !mInteractive.equals(criteria.mInteractive) : criteria.mInteractive != null)
            return false;
        if (mMarkupType != criteria.mMarkupType) return false;
        if (mPageRange != null ? !mPageRange.equals(criteria.mPageRange) : criteria.mPageRange != null) return false;
        if (mParams != null ? !mParams.equals(criteria.mParams) : criteria.mParams != null) return false;
        if (mSaveSnapshot != null ? !mSaveSnapshot.equals(criteria.mSaveSnapshot) : criteria.mSaveSnapshot != null)
            return false;
        if (mTransformerKey != null ? !mTransformerKey.equals(criteria.mTransformerKey) : criteria.mTransformerKey != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mFreshData != null ? mFreshData.hashCode() : 0;
        result = 31 * result + (mSaveSnapshot != null ? mSaveSnapshot.hashCode() : 0);
        result = 31 * result + (mInteractive != null ? mInteractive.hashCode() : 0);
        result = 31 * result + (mIgnorePagination != null ? mIgnorePagination.hashCode() : 0);
        result = 31 * result + (mAllowInlineScripts != null ? mAllowInlineScripts.hashCode() : 0);
        result = 31 * result + (mTransformerKey != null ? mTransformerKey.hashCode() : 0);
        result = 31 * result + (mAttachmentPrefix != null ? mAttachmentPrefix.hashCode() : 0);
        result = 31 * result + (mAnchor != null ? mAnchor.hashCode() : 0);
        result = 31 * result + (mMarkupType != null ? mMarkupType.hashCode() : 0);
        result = 31 * result + (mFormat != null ? mFormat.hashCode() : 0);
        result = 31 * result + (mPageRange != null ? mPageRange.hashCode() : 0);
        result = 31 * result + (mParams != null ? mParams.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RunReportCriteria{" +
                "freshData=" + mFreshData +
                ", saveSnapshot=" + mSaveSnapshot +
                ", interactive=" + mInteractive +
                ", ignorePagination=" + mIgnorePagination +
                ", allowInlineScripts=" + mAllowInlineScripts +
                ", transformerKey='" + mTransformerKey + '\'' +
                ", attachmentPrefix='" + mAttachmentPrefix + '\'' +
                ", anchor='" + mAnchor + '\'' +
                ", markupType=" + mMarkupType +
                ", format=" + mFormat +
                ", pageRange=" + mPageRange +
                '}';
    }

    public static class Builder {
        private Boolean mFreshData;
        private Boolean mSaveSnapshot;
        private Boolean mInteractive;
        private Boolean mIgnorePagination;
        private Boolean mAllowInlineScripts;
        private String mTransformerKey;
        private String mAttachmentPrefix;
        private String mAnchor;
        private ReportMarkup mMarkupType;
        private ReportFormat mFormat;
        private PageRange mPageRange;
        private List<ReportParameter> mParams;

        public Builder() {
            mInteractive = true;
        }

        public Builder withFreshData(@Nullable Boolean freshData) {
            this.mFreshData = freshData;
            return this;
        }

        public Builder withSaveSnapshot(@Nullable Boolean saveSnapshot) {
            mSaveSnapshot = saveSnapshot;
            return this;
        }

        /**
         * Configuration for report interactiveness
         * <p/>
         * NOTICE: This flag ignored for JRS 5.6 where we are forcing disable state
         *
         * @param interactive weather report should be mInteractive or not
         * @return builder instance
         */
        public Builder withInteractive(@Nullable Boolean interactive) {
            mInteractive = interactive;
            return this;
        }

        public Builder withIgnorePagination(@Nullable Boolean ignorePagination) {
            mIgnorePagination = ignorePagination;
            return this;
        }

        public Builder withAllowInlineScripts(@Nullable Boolean allowInlineScripts) {
            mAllowInlineScripts = allowInlineScripts;
            return this;
        }

        public Builder withTransformerKey(@Nullable String transformerKey) {
            mTransformerKey = transformerKey;
            return this;
        }

        public Builder withAttachmentPrefix(@Nullable String prefix) {
            mAttachmentPrefix = prefix;
            return this;
        }

        public Builder withAnchor(@Nullable String anchor) {
            mAnchor = anchor;
            return this;
        }

        public Builder withMarkupType(@Nullable ReportMarkup markup) {
            mMarkupType = markup;
            return this;
        }

        public Builder withFormat(@Nullable ReportFormat format) {
            mFormat = format;
            return this;
        }

        public Builder withPageRange(@Nullable PageRange pageRange) {
            mPageRange = pageRange;
            return this;
        }

        public Builder withParams(@Nullable List<ReportParameter> params) {
            mParams = params;
            return this;
        }

        public ReportExecutionOptions build() {
            return new ReportExecutionOptions(
                    mFreshData,
                    mSaveSnapshot,
                    mInteractive,
                    mIgnorePagination,
                    mAllowInlineScripts,
                    mTransformerKey,
                    mAttachmentPrefix,
                    mAnchor,
                    mMarkupType,
                    mFormat,
                    mPageRange,
                    mParams);
        }
    }
}
