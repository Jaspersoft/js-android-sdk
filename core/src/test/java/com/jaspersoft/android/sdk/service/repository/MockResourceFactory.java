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

import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import org.jetbrains.annotations.NotNull;

import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class MockResourceFactory {
    private MockResourceFactory() {
    }

    @NotNull
    public static <T extends ResourceLookup> T mockCommonFields(@NotNull T target) {
        when(target.getCreationDate()).thenReturn("2013-10-03 16:32:05");
        when(target.getUpdateDate()).thenReturn("2013-11-03 16:32:05");
        when(target.getResourceType()).thenReturn("reportUnit");
        when(target.getDescription()).thenReturn("description");
        when(target.getLabel()).thenReturn("label");
        when(target.getPermissionMask()).thenReturn(0);
        when(target.getVersion()).thenReturn(100);
        when(target.getUri()).thenReturn("/my/uri");
        when(target.getResourceType()).thenReturn("reportUnit");
        return target;
    }
}
