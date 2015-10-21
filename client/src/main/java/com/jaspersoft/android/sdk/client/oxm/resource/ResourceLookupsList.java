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

package com.jaspersoft.android.sdk.client.oxm.resource;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 1.7
 */

@Root(name = "resources")
public class ResourceLookupsList {
    public static int NO_OFFSET = -1;

    @Expose
    @SerializedName("resourceLookup")
    @ElementList(entry = "resourceLookup", inline = true, required = false, empty = false)
    private List<ResourceLookup> resourceLookups = new ArrayList<ResourceLookup>();

    @Expose
    @Element(required = false)
    private int resultCount;

    @Expose
    @Element(required = false)
    private int totalCount;

    @Expose
    @Element(required = false)
    private int nextOffset;

    @Expose
    @Element(required = false)
    private int startIndex;

    public ResourceLookupsList() {
        this.resultCount = 0;
        this.totalCount = 0;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public List<ResourceLookup> getResourceLookups() {
        return resourceLookups;
    }

    public void setResourceLookups(List<ResourceLookup> resourceLookups) {
        this.resourceLookups = resourceLookups;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public void setResultCount(String resultCount) {
        this.resultCount = (resultCount != null) ? Integer.parseInt(resultCount) : 0;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = (totalCount != null) ? Integer.parseInt(totalCount) : 0;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = (startIndex != null) ? Integer.parseInt(startIndex) : 0;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(String nextOffset) {
        this.nextOffset = (nextOffset != null) ? Integer.parseInt(nextOffset) : NO_OFFSET;
    }

    public void setNextOffset(int nextOffset) {
        this.nextOffset = nextOffset;
    }
}
