/*
 * Copyright (C) 2012-2014 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network.rest.v2.entity.resource;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ResourceLookupResponse {

    @Expose
    protected String label;
    @Expose
    protected String description;
    @Expose
    protected String uri;
    @Expose
    private String resourceType;

    @Expose
    protected int version;
    @Expose
    protected int permissionMask;
    @Expose
    protected String creationDate;
    @Expose
    protected String updateDate;

    public String getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public int getPermissionMask() {
        return permissionMask;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getUri() {
        return uri;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "ResourceLookupResponse{" +
                "creationDate='" + creationDate + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", uri='" + uri + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", version=" + version +
                ", permissionMask=" + permissionMask +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
