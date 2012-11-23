/*
 * Copyright (C) 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jasperforge.org/projects/androidsdk
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.ui.adapters;

import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.4
 */
public class ResourceDescriptorComparator implements Comparator<ResourceDescriptor>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(ResourceDescriptor object1, ResourceDescriptor object2) {
        if (object1.getWsType() == ResourceDescriptor.WsType.folder) {
            if (object2.getWsType() != ResourceDescriptor.WsType.folder) {
                return -1;
            }
        } else {
            if (object2.getWsType() == ResourceDescriptor.WsType.folder) {
                return 1;
            }
        }
        return object1.getLabel().compareToIgnoreCase(object2.getLabel());
    }
}
