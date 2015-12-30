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

import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportExportOptions {
    private final ReportFormat mFormat;

    private final PageRange mPageRange;
    private final String mAttachmentPrefix;
    private final String mAnchor;

    private final Boolean mIgnorePagination;
    private final Boolean mAllowInlineScripts;

    @TestOnly
    ReportExportOptions(ReportFormat format,
                                PageRange pageRange,
                                String attachmentPrefix,
                                String anchor,
                                Boolean ignorePagination,
                                Boolean allowInlineScripts) {
        mFormat = format;
        mPageRange = pageRange;
        mAttachmentPrefix = attachmentPrefix;
        mAnchor = anchor;
        mIgnorePagination = ignorePagination;
        mAllowInlineScripts = allowInlineScripts;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public ReportFormat getFormat() {
        return mFormat;
    }

    @Nullable
    public PageRange getPageRange() {
        return mPageRange;
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
    public Boolean getIgnorePagination() {
        return mIgnorePagination;
    }

    @Nullable
    public Boolean getAllowInlineScripts() {
        return mAllowInlineScripts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportExportOptions that = (ReportExportOptions) o;

        if (mAllowInlineScripts != null ? !mAllowInlineScripts.equals(that.mAllowInlineScripts) : that.mAllowInlineScripts != null)
            return false;
        if (mAnchor != null ? !mAnchor.equals(that.mAnchor) : that.mAnchor != null) return false;
        if (mAttachmentPrefix != null ? !mAttachmentPrefix.equals(that.mAttachmentPrefix) : that.mAttachmentPrefix != null)
            return false;
        if (mFormat != that.mFormat) return false;
        if (mIgnorePagination != null ? !mIgnorePagination.equals(that.mIgnorePagination) : that.mIgnorePagination != null)
            return false;
        if (mPageRange != null ? !mPageRange.equals(that.mPageRange) : that.mPageRange != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mFormat != null ? mFormat.hashCode() : 0;
        result = 31 * result + (mPageRange != null ? mPageRange.hashCode() : 0);
        result = 31 * result + (mAttachmentPrefix != null ? mAttachmentPrefix.hashCode() : 0);
        result = 31 * result + (mAnchor != null ? mAnchor.hashCode() : 0);
        result = 31 * result + (mIgnorePagination != null ? mIgnorePagination.hashCode() : 0);
        result = 31 * result + (mAllowInlineScripts != null ? mAllowInlineScripts.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RunExportCriteria{" +
                "format=" + mFormat +
                ", pages='" + mPageRange + '\'' +
                ", attachmentPrefix='" + mAttachmentPrefix + '\'' +
                ", anchor='" + mAnchor + '\'' +
                ", ignorePagination=" + mIgnorePagination +
                ", allowInlineScripts=" + mAllowInlineScripts +
                '}';
    }

    public static class Builder {
        private ReportFormat mFormat;

        private PageRange mPageRange;
        private String mAttachmentPrefix;
        private String mAnchor;

        private Boolean mIgnorePagination;
        private Boolean mAllowInlineScripts;

        private Builder() {
        }

        public Builder withFormat(@NotNull ReportFormat format) {
            mFormat = Preconditions.checkNotNull(format, "Format should not be null");
            return this;
        }

        public Builder withPageRange(@Nullable PageRange pages) {
            mPageRange = pages;
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

        public Builder withIgnorePagination(boolean ignorePagination) {
            mIgnorePagination = ignorePagination;
            return this;
        }

        public Builder withAllowInlineScripts(boolean allowInlineScripts) {
            mAllowInlineScripts = allowInlineScripts;
            return this;
        }

        public ReportExportOptions build() {
            if (mFormat == null) {
                throw new IllegalStateException("Format should be supplied");
            }
            return new ReportExportOptions(
                    mFormat,
                    mPageRange,
                    mAttachmentPrefix,
                    mAnchor,
                    mIgnorePagination,
                    mAllowInlineScripts
            );
        }
    }
}
