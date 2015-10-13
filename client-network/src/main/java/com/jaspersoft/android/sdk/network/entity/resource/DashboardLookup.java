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

package com.jaspersoft.android.sdk.network.entity.resource;

import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class DashboardLookup extends ResourceLookup {

    @Expose
    private List<DashboardFoundation> foundations;
    @Expose
    private List<DashboardResource> resources;
    @Expose
    private String defaultFoundation;

    @Override
    public String getResourceType() {
        return "dashboard";
    }

    public String getDefaultFoundation() {
        return defaultFoundation;
    }

    public List<DashboardFoundation> getFoundations() {
        return foundations;
    }

    public List<DashboardResource> getResources() {
        return resources;
    }

    @Override
    public String toString() {
        return "DashboardLookup{" +
                "creationDate='" + creationDate + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", uri='" + uri + '\'' +
                ", resourceType='dashboard'" +
                ", version=" + version +
                ", permissionMask=" + permissionMask +
                ", updateDate='" + updateDate + '\'' +
                ", defaultFoundation='" + defaultFoundation + '\'' +
                ", foundations=" + Arrays.toString(foundations.toArray()) +
                ", resources=" + Arrays.toString(resources.toArray()) +
                '}';
    }
}
