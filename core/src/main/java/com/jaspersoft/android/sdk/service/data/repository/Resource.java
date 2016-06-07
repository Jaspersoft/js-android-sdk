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

package com.jaspersoft.android.sdk.service.data.repository;


import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.3
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
    @NotNull
    private final String mUri;
    @NotNull
    private final PermissionMask mPermissionMask;
    private final int mVersion;

    protected Resource(
            @Nullable Date creationDate,
            @Nullable Date updateDate,
            @NotNull ResourceType resourceType,
            @NotNull String label,
            @NotNull String description,
            @NotNull String uri,
            @NotNull PermissionMask permissionMask,
            int version
    ) {
        mCreationDate = creationDate;
        mUpdateDate = updateDate;
        mResourceType = resourceType;
        mLabel = label;
        mDescription = description;
        mUri = uri;
        mPermissionMask = permissionMask;
        mVersion = version;
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

    @NotNull
    public String getUri() {
        return mUri;
    }

    @NotNull
    public PermissionMask getPermissionMask() {
        return mPermissionMask;
    }

    public int getVersion() {
        return mVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (mVersion != resource.mVersion) return false;
        if (mCreationDate != null ? !mCreationDate.equals(resource.mCreationDate) : resource.mCreationDate != null)
            return false;
        if (!mDescription.equals(resource.mDescription)) return false;
        if (!mLabel.equals(resource.mLabel)) return false;
        if (mPermissionMask != resource.mPermissionMask) return false;
        if (mResourceType != resource.mResourceType) return false;
        if (mUpdateDate != null ? !mUpdateDate.equals(resource.mUpdateDate) : resource.mUpdateDate != null)
            return false;
        if (!mUri.equals(resource.mUri)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mCreationDate != null ? mCreationDate.hashCode() : 0;
        result = 31 * result + (mUpdateDate != null ? mUpdateDate.hashCode() : 0);
        result = 31 * result + mResourceType.hashCode();
        result = 31 * result + mLabel.hashCode();
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + mUri.hashCode();
        result = 31 * result + mPermissionMask.hashCode();
        result = 31 * result + mVersion;
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
                ", mUri='" + mUri + '\'' +
                ", mPermissionMask=" + mPermissionMask +
                ", mVersion=" + mVersion +
                '}';
    }

    public static class Builder {
        private Date creationDate;
        private Date updateDate;
        private ResourceType resourceType;
        private String label;
        private String description;
        private String uri;
        private PermissionMask permissionMask;
        private int version;

        public Builder withCreationDate(@Nullable Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder withUpdateDate(@Nullable Date updateDate) {
            this.updateDate = updateDate;
            return this;
        }

        public Builder withResourceType(@NotNull ResourceType resourceType) {
            this.resourceType = Preconditions.checkNotNull(resourceType, "Resource type == null");
            return this;
        }

        public Builder withLabel(@NotNull String label) {
            this.label = Preconditions.checkNotNull(label, "Label == null");
            return this;
        }

        public Builder withDescription(@Nullable String description) {
            this.description = description;
            return this;
        }

        public Builder withUri(@NotNull String uri) {
            this.uri = Preconditions.checkNotNull(uri, "Uri == null");
            return this;
        }

        public Builder withPermissionMask(@NotNull PermissionMask permissionMask) {
            this.permissionMask = Preconditions.checkNotNull(permissionMask, "Mask == null");
            return this;
        }

        public Builder withVersion(int version) {
            this.version = version;
            return this;
        }

        protected void checkState() {
            Preconditions.checkNotNull(resourceType, "Can not create resource without type");
            Preconditions.checkNotNull(label, "Can not create resource without label");
            Preconditions.checkNotNull(uri, "Can not create resource without uri");
            Preconditions.checkNotNull(permissionMask, "Can not create resource without mask");
        }

        public Resource build() {
            checkState();
            return new Resource(
                    creationDate,
                    updateDate,
                    resourceType,
                    label,
                    description,
                    uri,
                    permissionMask,
                    version
            );
        }
    }
}
