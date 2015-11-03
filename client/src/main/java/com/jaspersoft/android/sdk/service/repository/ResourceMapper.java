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
package com.jaspersoft.android.sdk.service.repository;


import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ResourceMapper {

    @NotNull
    public Collection<Resource> transform(Collection<ResourceLookup> resources, SimpleDateFormat dateTimeFormat) {
        Collection<Resource> result = new LinkedList<>();
        for (ResourceLookup lookup : resources) {
            if (lookup != null) {
                result.add(transform(lookup, dateTimeFormat));
            }
        }
        return result;
    }

    @NotNull
    public Resource transform(ResourceLookup lookup, SimpleDateFormat dateTimeFormat) {
        Resource resource = new Resource();
        resource.setCreationDate(toDate(lookup.getCreationDate(), dateTimeFormat));
        resource.setUpdateDate(toDate(lookup.getUpdateDate(), dateTimeFormat));
        resource.setDescription(lookup.getDescription());
        resource.setLabel(lookup.getLabel());
        resource.setResourceType(toType(lookup.getResourceType()));
        return resource;
    }

    @Nullable
    private Date toDate(String creationDate, SimpleDateFormat dateTimeFormat) {
        try {
            return dateTimeFormat.parse(String.valueOf(creationDate));
        } catch (ParseException e) {
            return null;
        }
    }

    @NotNull
    private ResourceType toType(String resourceType) {
        return ResourceType.defaultParser().parse(String.valueOf(resourceType));
    }
}
