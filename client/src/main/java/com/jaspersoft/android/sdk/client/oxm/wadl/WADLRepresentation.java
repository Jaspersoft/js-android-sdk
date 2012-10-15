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

package com.jaspersoft.android.sdk.client.oxm.wadl;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.4
 */
@Root(name="representation", strict=false)
public class WADLRepresentation {

    @Attribute(required=false)
    private String id;

    @Attribute
    private String mediaType;

    @Attribute(required=false)
    private String element;


    public String getId() {
        return id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getElement() {
        return element;
    }
}
