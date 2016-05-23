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

package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class InputControlDashboardComponent extends DashboardComponent {
    @Expose
    private String label;
    @Expose
    private String ownerResourceId;
    @Expose
    private String ownerResourceParameterName;
    @Expose
    private String parentId;
    @Expose
    private String resource;
    @Expose
    private int position;

    public String getLabel() {
        return label;
    }

    public String getOwnerResourceId() {
        return ownerResourceId;
    }

    public String getOwnerResourceParameterName() {
        return ownerResourceParameterName;
    }

    public String getParentId() {
        return parentId;
    }

    public String getResource() {
        return resource;
    }

    public int getPosition() {
        return position;
    }
}
