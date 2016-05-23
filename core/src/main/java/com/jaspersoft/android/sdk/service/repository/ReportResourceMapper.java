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

import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.service.data.report.AbstractResourceBuilder;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportResourceMapper extends AbstractResourceMapper<ReportResource, ReportLookup> {
    public ReportResourceMapper(@NotNull SimpleDateFormat format, @Nullable ResourceType backupType) {
        super(format, backupType);
    }

    public ReportResourceMapper(@NotNull SimpleDateFormat format) {
        super(format);
    }

    @Override
    public ReportResource transform(ReportLookup lookup) {
        ReportResource.Builder builder = new ReportResource.Builder();

        AbstractResourceBuilder abstractResourceBuilder = builder.addResource();
        buildLookup(abstractResourceBuilder, lookup);

        builder.withAlwaysPrompt(lookup.alwaysPromptControls());

        return builder.build();
    }
}