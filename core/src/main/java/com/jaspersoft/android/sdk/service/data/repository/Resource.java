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
    private final Date mCreationDate;
    @Nullable
    private final Date mUpdateDate;
    @NotNull
    private final ResourceType mResourceType;
    @NotNull
    private final String mLabel;
    @NotNull
    private final String mDescription;

    public Resource(@Nullable Date creationDate,
                    @Nullable Date updateDate,
                    @NotNull ResourceType resourceType,
                    @NotNull String label,
                    @NotNull String description) {
        mCreationDate = creationDate;
        mUpdateDate = updateDate;
        mResourceType = resourceType;
        mLabel = label;
        mDescription = description;
    }

    @Nullable
    public Date getCreationDate() {
        return mCreationDate;
    }

    @Nullable
    public Date getUpdateDate() {
        return mUpdateDate;
    }

    @NotNull
    public ResourceType getResourceType() {
        return mResourceType;
    }

    @NotNull
    public String getLabel() {
        return mLabel;
    }

    @NotNull
    public String getDescription() {
        return mDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (mCreationDate != null ? !mCreationDate.equals(resource.mCreationDate) : resource.mCreationDate != null)
            return false;
        if (mDescription != null ? !mDescription.equals(resource.mDescription) : resource.mDescription != null)
            return false;
        if (mLabel != null ? !mLabel.equals(resource.mLabel) : resource.mLabel != null) return false;
        if (mResourceType != resource.mResourceType) return false;
        if (mUpdateDate != null ? !mUpdateDate.equals(resource.mUpdateDate) : resource.mUpdateDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mCreationDate != null ? mCreationDate.hashCode() : 0;
        result = 31 * result + (mUpdateDate != null ? mUpdateDate.hashCode() : 0);
        result = 31 * result + (mResourceType != null ? mResourceType.hashCode() : 0);
        result = 31 * result + (mLabel != null ? mLabel.hashCode() : 0);
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "mCreationDate=" + mCreationDate +
                ", mUpdateDate=" + mUpdateDate +
                ", mResourceType=" + mResourceType +
                ", mLabel='" + mLabel + '\'' +
                ", mDescription='" + mDescription + '\'' +
                '}';
    }
}
