/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ReportResourceMapper {

    public ReportResource transform(ReportLookup lookup, SimpleDateFormat dateTimeFormat) {
        Date creationDate;
        Date updateDate;

        try {
            creationDate = dateTimeFormat.parse(String.valueOf(lookup.getCreationDate()));
            updateDate = dateTimeFormat.parse(String.valueOf(lookup.getUpdateDate()));
        } catch (ParseException e) {
            creationDate = updateDate = null;
        }

        String type = lookup.getResourceType();
        ResourceType resourceType = ResourceType.unknown;
        if (type != null) {
            resourceType = ResourceType.defaultParser().parse(type);
        }
        return new ReportResource(creationDate,
                updateDate,
                resourceType,
                lookup.getLabel(),
                lookup.getDescription(),
                lookup.getUri(),
                lookup.alwaysPromptControls()
        );
    }
}
