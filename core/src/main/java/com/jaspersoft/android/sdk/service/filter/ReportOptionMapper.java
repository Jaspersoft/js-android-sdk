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

package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionEntity;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportOptionMapper {

    public Set<ReportOption> transform(Set<ReportOptionEntity> entities) {
        Set<ReportOption> options = new HashSet<>(entities.size());
        for (ReportOptionEntity entity : entities) {
            if (entity != null) {
                ReportOption option = transform(entity);
                options.add(option);
            }
        }
        return options;
    }

    public ReportOption transform(ReportOptionEntity entity) {
        return new ReportOption.Builder()
                .withId(entity.getId())
                .withUri(entity.getUri())
                .withLabel(entity.getLabel())
                .build();
    }
}
