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

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ExecutionConfiguration {

    private final boolean mFreshData;
    private final boolean mInteractive;
    private final boolean mSaveSnapshot;
    private final int mFormat;
    private final String mPages;

    ExecutionConfiguration(Builder builder) {
        mFreshData = builder.freshData;
        mInteractive = builder.interactive;
        mSaveSnapshot = builder.saveSnapshot;
        mFormat = builder.format;
        mPages = builder.pages;
    }

    public static class Format {
        public static int NONE = 0;
        public static int HTML = 1;
        public static int PDF = 2;
        public static int XLS = 3;

        private Format() {
        }
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public int getFormat() {
        return mFormat;
    }

    public boolean isFreshData() {
        return mFreshData;
    }

    public boolean isInteractive() {
        return mInteractive;
    }

    @Nullable
    public String getPages() {
        return mPages;
    }

    public boolean isSaveSnapshot() {
        return mSaveSnapshot;
    }

    public static class Builder {
        private boolean freshData;
        private boolean interactive;
        private boolean saveSnapshot;
        private int format;
        private String pages;

        public Builder() {
            interactive = true;
            format = Format.NONE;
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

        public Builder format(int format) {
            this.format = format;
            return this;
        }

        public Builder pages(@Nullable String pages) {
            this.pages = pages;
            return this;
        }

        public ExecutionConfiguration create() {
            return new ExecutionConfiguration(this);
        }
    }
}
