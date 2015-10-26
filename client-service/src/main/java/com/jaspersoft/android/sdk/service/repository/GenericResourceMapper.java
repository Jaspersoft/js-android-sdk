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
package com.jaspersoft.android.sdk.service.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.service.data.repository.GenericResource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class GenericResourceMapper {

    @NonNull
    public Collection<GenericResource> transform(Collection<ResourceLookup> resources, SimpleDateFormat dateTimeFormat) {
        Collection<GenericResource> result = new LinkedList<>();
        for (ResourceLookup lookup : resources) {
            if (lookup != null) {
                result.add(transform(lookup, dateTimeFormat));
            }
        }
        return result;
    }

    @NonNull
    public GenericResource transform(ResourceLookup lookup, SimpleDateFormat dateTimeFormat) {
        GenericResource resource = new GenericResource();
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

    @NonNull
    private ResourceType toType(String resourceType) {
        return ResourceType.defaultParser().parse(String.valueOf(resourceType));
    }
}
