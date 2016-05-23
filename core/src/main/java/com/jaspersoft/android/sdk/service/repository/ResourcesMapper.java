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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.FileLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ResourcesMapper {
    @NotNull
    public List<Resource> toResources(@NotNull Collection<ResourceLookup> resources, @NotNull SimpleDateFormat dateTimeFormat) {
        List<Resource> result = new ArrayList<>(resources.size());
        ResourceMapper resourceMapper = new ResourceMapper(dateTimeFormat);

        for (ResourceLookup lookup : resources) {
            if (lookup != null) {
                Resource resource = resourceMapper.transform(lookup);
                result.add(resource);
            }
        }

        return result;
    }

    @NotNull
    public Resource toResource(@NotNull ResourceLookup lookup, @NotNull SimpleDateFormat dateTimeFormat) {
        return new ResourceMapper(dateTimeFormat).transform(lookup);
    }

    @NotNull
    public Resource toConcreteResource(ResourceLookup lookup, SimpleDateFormat datetimeFormatPattern, ResourceType resourceType) {
        switch (resourceType) {
            case reportUnit:
                return new ReportResourceMapper(datetimeFormatPattern, resourceType).transform((ReportLookup) lookup);
            case file:
                return new FileResourceMapper(datetimeFormatPattern, resourceType).transform((FileLookup) lookup);
            default:
                return new ResourceMapper(datetimeFormatPattern, resourceType).transform(lookup);
        }
    }
}
