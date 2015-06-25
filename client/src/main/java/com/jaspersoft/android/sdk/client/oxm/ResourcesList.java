/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.android.sdk.client.oxm;

import com.google.gson.annotations.Expose;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */

@Root(name = "resourceDescriptors")
public class ResourcesList {

    @Expose
    @ElementList(entry = "resourceDescriptor", inline = true, required = false, empty = false)
    private List<ResourceDescriptor> resourceDescriptors = new ArrayList<ResourceDescriptor>();

    public List<ResourceDescriptor> getResourceDescriptors() {
        return resourceDescriptors;
    }

    public void setResourceDescriptors(List<ResourceDescriptor> resourceDescriptors) {
        this.resourceDescriptors = resourceDescriptors;
    }
}
