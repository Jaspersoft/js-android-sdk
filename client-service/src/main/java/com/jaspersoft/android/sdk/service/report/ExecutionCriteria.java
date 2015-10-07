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
package com.jaspersoft.android.sdk.service.report;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ExecutionCriteria {

    private final boolean mFreshData;
    private final boolean mInteractive;
    private final boolean mSaveSnapshot;
    private final Format mFormat;
    private final String mPages;
    private final Map<String, Set<String>> mParams;
    private final String mAttachmentPrefix;

    ExecutionCriteria(Builder builder) {
        mFreshData = builder.freshData;
        mInteractive = builder.interactive;
        mSaveSnapshot = builder.saveSnapshot;
        mFormat = builder.format;
        mPages = builder.pages;
        mParams = builder.params;
        mAttachmentPrefix = builder.attachmentPrefix;
    }

    public enum Format {
        HTML, PDF, XLS
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public Format getFormat() {
        return mFormat;
    }

    public boolean isFreshData() {
        return mFreshData;
    }

    public boolean isInteractive() {
        return mInteractive;
    }

    public boolean isSaveSnapshot() {
        return mSaveSnapshot;
    }

    @Nullable
    public String getPages() {
        return mPages;
    }

    @Nullable
    public Map<String, Set<String>> getParams() {
        return mParams;
    }

    @Nullable
    public String getAttachmentPrefix() {
        return mAttachmentPrefix;
    }

    public static class Builder {
        private boolean freshData;
        private boolean interactive;
        private boolean saveSnapshot;
        private Format format;
        private String pages;
        public Map<String, Set<String>> params;
        public String attachmentPrefix;

        public Builder() {
            interactive = true;
        }

        public Builder freshData(boolean freshData) {
            this.freshData = freshData;
            return this;
        }

        public Builder interactive(boolean interactive) {
            this.interactive = interactive;
            return this;
        }

        public Builder saveSnapshot(boolean saveSnapshot) {
            this.saveSnapshot = saveSnapshot;
            return this;
        }

        public Builder format(Format format) {
            this.format = format;
            return this;
        }

        public Builder pages(@Nullable String pages) {
            this.pages = pages;
            return this;
        }

        public Builder params(Map<String, Set<String>> params) {
            this.params = params;
            return this;
        }

        public Builder attachmentPrefix(String prefix) {
            this.attachmentPrefix = prefix;
            return this;
        }

        public ExecutionCriteria create() {
            return new ExecutionCriteria(this);
        }
    }
}
