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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;

/**
 * This class represents a resource lookup entity for convenient XML serialization process.
 *
 * @author Ivan Gadzhega
 * @since 1.7
 */
public class ResourceLookup implements Parcelable {

    @Expose
    @Element(required=false)
    protected String label;
    @Expose
    @Element(required=false)
    protected String description;
    @Expose
    @Element
    protected String uri;
    @Expose
    @Element(required=false)
    protected String resourceType;

    @Expose
    @Element(required=false)
    protected int version;
    @Expose
    @Element(required=false)
    protected int permissionMask;
    @Expose
    @Element(required=false)
    protected String creationDate;
    @Expose
    @Element(required=false)
    protected String updateDate;

    public ResourceLookup() { }

    //---------------------------------------------------------------------
    // Parcelable
    //---------------------------------------------------------------------

    public ResourceLookup(Parcel source) {
        this.label = source.readString();
        this.description = source.readString();
        this.uri = source.readString();
        this.resourceType = source.readString();
        this.creationDate = source.readString();
        this.updateDate = source.readString();
        this.version = source.readInt();
        this.permissionMask = source.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ResourceLookup createFromParcel(Parcel source) {
            return new ResourceLookup(source);
        }

        public ResourceLookup[] newArray(int size) {
            return new ResourceLookup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(description);
        dest.writeString(uri);
        dest.writeString(resourceType);
        dest.writeString(creationDate);
        dest.writeString(updateDate);
        dest.writeInt(version);
        dest.writeInt(permissionMask);
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public ResourceType getResourceType() {
        try {
            return ResourceType.valueOf(resourceType);
        } catch (IllegalArgumentException ex) {
            return ResourceType.unknown;
        }
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType.toString();
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getPermissionMask() {
        return permissionMask;
    }

    public void setPermissionMask(int permissionMask) {
        this.permissionMask = permissionMask;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    //---------------------------------------------------------------------
    // Nested Classes
    //---------------------------------------------------------------------

    public enum ResourceType {
        folder,
        reportUnit,
        reportOptions,
        dashboard,
        legacyDashboard,
        file,
        unknown
    }

}
