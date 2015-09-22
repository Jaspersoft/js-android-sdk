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

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class SearchCriteria {
    public static int ALL = 0;
    public static int REPORT = 1;
    public static int DASHBOARD = 2;
    public static int LEGACY_DASHBOARD = 4;

    private final int mCount;
    private final int mResourceMask;

    private SearchCriteria(int count,
                           int resourceMask) {
        mCount = count;
        mResourceMask = resourceMask;
    }

    public static class Builder {

        private int mCount;
        private int mResourceMask;

        public Builder limitCount(int count) {
            mCount = count;
            return this;
        }

        public Builder resourceMask(int resourceMask) {
            mResourceMask = resourceMask;
            return this;
        }

        public SearchCriteria build() {
            return new SearchCriteria(mCount, mResourceMask);
        }
    }
}
