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
package com.jaspersoft.android.sdk.service.report;

import org.jetbrains.annotations.Nullable;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public abstract class ExecutionCriteria {

    private final boolean mFreshData;
    private final boolean mInteractive;
    private final boolean mSaveSnapshot;
    private final Format mFormat;
    private final String mPages;
    private final String mAttachmentPrefix;

    protected ExecutionCriteria(boolean freshData,
                                boolean interactive,
                                boolean saveSnapshot,
                                Format format,
                                String pages,
                                String attachmentPrefix) {
        mFreshData = freshData;
        mInteractive = interactive;
        mSaveSnapshot = saveSnapshot;
        mFormat = format;
        mPages = pages;
        mAttachmentPrefix = attachmentPrefix;
    }

    public enum Format {
        HTML, PDF, XLS
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
    public String getAttachmentPrefix() {
        return mAttachmentPrefix;
    }

}
