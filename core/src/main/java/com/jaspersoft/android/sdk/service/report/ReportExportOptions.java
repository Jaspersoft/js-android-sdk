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
 * Report export options that are required to initiate report export execution process
 *
 * @author Tom Koptel
 * @since 2.3
 */
public final class ReportExportOptions {
    private final ReportMarkup mMarkupType;
    private final ReportFormat mFormat;

    private final PageRange mPageRange;
    private final String mAttachmentPrefix;
    private final String mAnchor;

    private final Boolean mIgnorePagination;
    private final Boolean mAllowInlineScripts;

    @TestOnly
    ReportExportOptions(ReportMarkup markupType,
                        ReportFormat format,
                        PageRange pageRange,
                        String attachmentPrefix,
                        String anchor,
                        Boolean ignorePagination,
                        Boolean allowInlineScripts) {
        mMarkupType = markupType;
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

    @Nullable
    public ReportMarkup getMarkupType() {
        return mMarkupType;
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
        if (mMarkupType != that.mMarkupType) return false;
        if (mPageRange != null ? !mPageRange.equals(that.mPageRange) : that.mPageRange != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mMarkupType != null ? mMarkupType.hashCode() : 0;
        result = 31 * result + (mFormat != null ? mFormat.hashCode() : 0);
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
        private ReportMarkup mMarkup;

        private Builder() {
        }

        /**
         * Allows to define output format of report. If not provided, then no export is executed
         *
         * @param format defines the initial exports format
         * @return builder for convenient configuration
         */
        public Builder withFormat(@NotNull ReportFormat format) {
            mFormat = Preconditions.checkNotNull(format, "Format should not be null");
            return this;
        }

        /**
         * Allows to specify how much pages to generate
         *
         * @param pages can be single page or range format
         * @return builder for convenient configuration
         */
        public Builder withPageRange(@Nullable PageRange pages) {
            mPageRange = pages;
            return this;
        }

        /**
         * Affects HTML export only. Specifies what kind of HTML markup is requested.
         *
         * @param markup
         * @return builder for convenient configuration
         */
        public Builder withMarkupType(@Nullable ReportMarkup markup) {
            mMarkup = markup;
            return this;
        }

        /**
         * URL prefix for report attachments. This parameter matter for HTML output only.
         * Placeholders {contextPath}, {reportExecutionId} and {exportOptions} can be used. They are replaced in runtime by corresponding values
         *
         * @param prefix {contextPath}/rest_v2/reportExecutions/{reportExecutionId}/exports/{exportExecutionId}/attachments/
         * @return builder for convenient configuration
         */
        public Builder withAttachmentPrefix(@Nullable String prefix) {
            mAttachmentPrefix = prefix;
            return this;
        }

        /**
         * Allows to start export execution starting from specified anchor.
         * Anchor name. Has lower priority, than pages parameter.
         * If pages parameter is specified, then anchor is ignored.
         * If pages parameter isn't specified, then export will generate a page, where is an anchor with corresponding name.
         *
         * @param anchor the entry point of exact page
         * @return builder for convenient configuration
         */
        public Builder withAnchor(@Nullable String anchor) {
            mAnchor = anchor;
            return this;
        }

        /**
         * Allows to ignore pagination and combine all exports as single page
         * The corresponding flag included for JRS instances starting from version 6.2
         *
         * @param ignorePagination indicates whether to combine pages in export
         * @return builder for convenient configuration
         */
        public Builder withIgnorePagination(boolean ignorePagination) {
            mIgnorePagination = ignorePagination;
            return this;
        }

        /**
         * Affects HTML export only. If true, then inline scripts are allowed, otherwise no inline script is included to the HTML export output.
         * If markupType is "embeddable', then value of this parameter is ignored. No inline scripts in this case.
         *
         * @param allowInlineScripts flags whether include scripts or not
         * @return builder for convenient configuration
         */
        public Builder withAllowInlineScripts(boolean allowInlineScripts) {
            mAllowInlineScripts = allowInlineScripts;
            return this;
        }

        public ReportExportOptions build() {
            if (mFormat == null) {
                throw new IllegalStateException("Format should be supplied");
            }
            return new ReportExportOptions(
                    mMarkup,
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
