/*
 * Copyright (C) 2012-2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.android.sdk.client.oxm.resource;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 1.7
 */

@Root(name="resources")
public class ResourceLookupsList {

    @ElementList(entry="resourceLookup", inline=true, required=false, empty=false)
    private List<ResourceLookup> resourceLookups;

    @Element(required=false)
    private int resultCount;

    @Element(required=false)
    private int totalCount;

    @Element(required=false)
    private int nextOffset;

    @Element(required=false)
    private int startIndex;

    public ResourceLookupsList() {
        this.resourceLookups = new ArrayList<ResourceLookup>();
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
        this.nextOffset = (nextOffset != null) ? Integer.parseInt(nextOffset) : 0;
    }

    public void setNextOffset(int nextOffset) {
        this.nextOffset = nextOffset;
    }
}
