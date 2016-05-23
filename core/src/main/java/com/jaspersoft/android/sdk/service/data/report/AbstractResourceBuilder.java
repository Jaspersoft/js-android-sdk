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
public class AbstractResourceBuilder<Parent> {
    private final Resource.Builder mBuilder = new Resource.Builder();
    private final Parent mParent;

    public AbstractResourceBuilder(Parent parent) {
        mParent = parent;
    }

    public AbstractResourceBuilder withCreationDate(@Nullable Date creationDate) {
        mBuilder.withCreationDate(creationDate);
        return this;
    }

    public AbstractResourceBuilder withUpdateDate(@Nullable Date updateDate) {
        mBuilder.withUpdateDate(updateDate);
        return this;
    }

    public AbstractResourceBuilder withResourceType(@NotNull ResourceType resourceType) {
        mBuilder.withResourceType(resourceType);
        return this;
    }

    public AbstractResourceBuilder withLabel(@NotNull String label) {
        mBuilder.withLabel(label);
        return this;
    }

    public AbstractResourceBuilder withDescription(@Nullable String description) {
        mBuilder.withDescription(description);
        return this;
    }

    public AbstractResourceBuilder withUri(@NotNull String uri) {
        mBuilder.withUri(uri);
        return this;
    }

    public AbstractResourceBuilder withPermissionMask(@NotNull PermissionMask permissionMask) {
        mBuilder.withPermissionMask(permissionMask);
        return this;
    }

    public AbstractResourceBuilder withVersion(int version) {
        mBuilder.withVersion(version);
        return this;
    }

    public Resource.Builder getResourceBuilder() {
        return mBuilder;
    }

    public Parent done() {
        return mParent;
    }
}
