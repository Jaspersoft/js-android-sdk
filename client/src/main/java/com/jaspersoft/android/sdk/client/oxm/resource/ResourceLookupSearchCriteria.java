/*
 * Copyright (C) 2012-2014 Jaspersoft Corporation. All rights reserved.
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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 2.0
 */
public class ResourceLookupSearchCriteria implements Parcelable {

    private String folderUri;
    private String query;
    private List<String> types;
    private String sortBy;

    private boolean recursive = true;
    private boolean forceFullPage;
    private int offset = 0;
    private int limit = 100;

    public ResourceLookupSearchCriteria() {
    }

    public ResourceLookupSearchCriteria(ResourceLookupSearchCriteria oldCriteria) {
        this.folderUri = oldCriteria.getFolderUri();
        this.query = oldCriteria.getQuery();
        this.types = new ArrayList<String>(oldCriteria.getTypes());
        this.sortBy = oldCriteria.getSortBy();
        this.recursive = oldCriteria.isRecursive();
        this.forceFullPage = oldCriteria.isForceFullPage();
        this.offset = oldCriteria.getOffset();
        this.limit = oldCriteria.getLimit();
    }

    //---------------------------------------------------------------------
    // Parcelable
    //---------------------------------------------------------------------

    public ResourceLookupSearchCriteria(Parcel source) {
        this.folderUri = source.readString();
        this.query = source.readString();
        this.types = source.createStringArrayList();
        this.sortBy = source.readString();
        this.recursive = source.readByte() != 0;
        this.forceFullPage = source.readByte() != 0;
        this.offset = source.readInt();
        this.limit = source.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ResourceLookupSearchCriteria createFromParcel(Parcel source) {
            return new ResourceLookupSearchCriteria(source);
        }

        public ResourceLookupSearchCriteria[] newArray(int size) {
            return new ResourceLookupSearchCriteria[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(folderUri);
        dest.writeString(query);
        dest.writeStringList(types);
        dest.writeString(sortBy);
        dest.writeByte((byte) (recursive ? 1 : 0));
        dest.writeByte((byte) (forceFullPage ? 1 : 0));
        dest.writeInt(offset);
        dest.writeInt(limit);
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    /**
     * {@link ResourceLookupSearchCriteria#setFolderUri}
     */
    public String getFolderUri() {
        return folderUri;
    }

    /**
     * The path of the base folder for the search.
     */
    public void setFolderUri(String folderUri) {
        this.folderUri = folderUri;
    }

    /**
     * {@link ResourceLookupSearchCriteria#setQuery}
     */
    public String getQuery() {
        return query;
    }

    /**
     * Search for resources having the specified text in the name or description.
     * Note that the search string does not match in the ID of resources.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * {@link ResourceLookupSearchCriteria#setTypes}
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * Match only resources of the given type. Multiple type parameters are allowed.
     * Wrong values are ignored.
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    /**
     * {@link ResourceLookupSearchCriteria#setSortBy}
     */
    public String getSortBy() {
        return sortBy;
    }

    /**
     * One of the following strings representing a field in the results to sort by: uri,
     * label, description, type, creationDate, updateDate, accessTime, or popularity
     * (based on access events).
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * {@link ResourceLookupSearchCriteria#setRecursive}
     */
    public boolean isRecursive() {
        return recursive;
    }

    /**
     * Indicates whether search should include all sub-folders recursively. When
     * omitted, the default behavior is recursive (true).
     */
    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    /**
     * {@link ResourceLookupSearchCriteria#setOffset}
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Used for pagination to request an offset in the set of results. This is
     * equivalent to a specific page number. The default offset is 1 (first page).
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * {@link ResourceLookupSearchCriteria#setLimit}
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Used for pagination to specify the maximum number of resources to return in
     * each response. This is equivalent to the number of results per page. The
     * default limit is 100.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * {@link ResourceLookupSearchCriteria#setForceFullPage}
     */
    public boolean isForceFullPage() {
        return forceFullPage;
    }

    /**
     * With forceFullPage=false,
     * This is the offset to request the next page.  The Next-Offset is equivalent to Start-Index+limit, except on the last page.
     * On the the last page, the Next-Offset is omitted to indicate there are no further pages.
     * {@link ResourceLookupSearchCriteria#setLimit}
     */

    /**
     * With <b>forceFullPage=false</b> we receive different pagination experience. Considering header
     * for response Next-Offset which is the offset to request the next page. The Next-Offset
     * is equivalent to Start-Index+limit, except on the last page.
     * On the the last page, the Next-Offset is omitted to indicate there are no further pages.
     * <br/>
     * <br/>
     * With <b>forceFullPage=true</b> enables full page pagination. Depending on the type of search and
     * user permissions, this parameter can cause significant performance delays.
     *
     * @param forceFullPage accepts boolean value
     */
    public void setForceFullPage(boolean forceFullPage) {
        this.forceFullPage = forceFullPage;
    }
}
