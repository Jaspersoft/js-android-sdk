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

package com.jaspersoft.android.sdk.service.data.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class FeatureSet {
    private final String mRawData;

    FeatureSet(String rawData) {
        mRawData = rawData;
    }

    public Set<String> asSet() {
        String[] split = mRawData.split(" ");
        return new HashSet<>(Arrays.asList(split));
    }

    public String asString() {
        return mRawData;
    }

    public static FeatureSet create(String rawString) {
        return new FeatureSet(rawString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureSet that = (FeatureSet) o;

        return !(mRawData != null ? !mRawData.equals(that.mRawData) : that.mRawData != null);

    }

    @Override
    public int hashCode() {
        return mRawData != null ? mRawData.hashCode() : 0;
    }
}
