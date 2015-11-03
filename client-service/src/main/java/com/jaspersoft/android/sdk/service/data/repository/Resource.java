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
package com.jaspersoft.android.sdk.service.data.repository;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class Resource {
    @Nullable
    private Date mCreationDate;
    @Nullable
    private Date mUpdateDate;

    private ResourceType mResourceType;
    private String mLabel;
    private String mDescription;

    @Nullable
    public Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(@Nullable Date creationDate) {
        mCreationDate = creationDate;
    }

    @Nullable
    public Date getUpdateDate() {
        return mUpdateDate;
    }

    public void setUpdateDate(@Nullable Date updateDate) {
        mUpdateDate = updateDate;
    }

    @NotNull
    public ResourceType getResourceType() {
        return mResourceType;
    }

    public void setResourceType(@NotNull ResourceType resourceType) {
        mResourceType = resourceType;
    }

    @NotNull
    public String getLabel() {
        return mLabel;
    }

    public void setLabel(@NotNull String label) {
        mLabel = label;
    }

    @NotNull
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(@NotNull String description) {
        mDescription = description;
    }
}
