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

package com.jaspersoft.android.sdk.service.data.report;

import com.jaspersoft.android.sdk.service.data.repository.PermissionMask;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class ReportResource extends Resource {
    private final boolean mAlwaysPrompt;

    private ReportResource(
            @Nullable Date creationDate,
            @Nullable Date updateDate,
            @NotNull ResourceType resourceType,
            @NotNull String label,
            @NotNull String description,
            @NotNull String uri,
            @NotNull PermissionMask permissionMask,
            int version,
            boolean alwaysPrompt
    ) {
        super(creationDate, updateDate, resourceType, label, description, uri, permissionMask, version);
        mAlwaysPrompt = alwaysPrompt;
    }

    public boolean alwaysPromptControls() {
        return mAlwaysPrompt;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ReportResource that = (ReportResource) o;

        if (mAlwaysPrompt != that.mAlwaysPrompt) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (mAlwaysPrompt ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReportResource{" +
                "mCreationDate=" + getCreationDate() +
                ", mUpdateDate=" + getUpdateDate() +
                ", mResourceType=" + getResourceType() +
                ", mLabel='" + getLabel() + '\'' +
                ", mDescription='" + getDescription() + '\'' +
                ", mAlwaysPrompt=" + mAlwaysPrompt +
                '}';
    }

    public static class Builder {
        private final Resource.Builder mBuilder = new Resource.Builder();
        private final AbstractResourceBuilder<Builder> mResourceBuilder;
        private boolean alwaysPrompt;

        public Builder() {
            mResourceBuilder = new AbstractResourceBuilder<>(this);
        }

        public AbstractResourceBuilder addResource() {
            return mResourceBuilder;
        }

        public Builder withAlwaysPrompt(boolean alwaysPrompt) {
            this.alwaysPrompt = alwaysPrompt;
            return this;
        }

        public ReportResource build() {
            Resource resource = mResourceBuilder.getResourceBuilder().build();
            return new ReportResource(
                    resource.getCreationDate(),
                    resource.getUpdateDate(),
                    resource.getResourceType(),
                    resource.getLabel(),
                    resource.getDescription(),
                    resource.getUri(),
                    resource.getPermissionMask(),
                    resource.getVersion(),
                    alwaysPrompt
            );
        }
    }
}
