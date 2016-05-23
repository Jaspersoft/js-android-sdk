/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.network.entity.resource;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class ResourceSearchResult {

    @Expose
    @SerializedName("resourceLookup")
    private List<ResourceLookup> mResources;
    private int mResultCount;
    private int mTotalCount;
    private int mStartIndex;
    private int mNextOffset;

    private ResourceSearchResult(List<ResourceLookup> resources) {
        mResources = resources;
    }

    public static ResourceSearchResult empty() {
        return new ResourceSearchResult(Collections.<ResourceLookup>emptyList());
    }

    public int getNextOffset() {
        return mNextOffset;
    }

    public List<ResourceLookup> getResources() {
        return mResources;
    }

    public int getResultCount() {
        return mResultCount;
    }

    public int getStartIndex() {
        return mStartIndex;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    @Override
    public String toString() {
        return "ResourceSearchResult{" +
                "mNextOffset=" + mNextOffset +
                ", mResourcesCount=" + mResources.size() +
                ", mResultCount=" + mResultCount +
                ", mStartIndex=" + mStartIndex +
                ", mTotalCount=" + mTotalCount +
                '}';
    }

    public void setResultCount(int resultCount) {
        mResultCount = resultCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public void setStartIndex(int startIndex) {
        mStartIndex = startIndex;
    }

    public void setNextOffset(int nextOffset) {
        mNextOffset = nextOffset;
    }
}
