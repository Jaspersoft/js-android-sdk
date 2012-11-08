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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * This class represents a resource parameter entity for convenient XML serialization process.
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
@Root(name="parameter")
public class ResourceParameter {
    
    @Attribute
    private String name;

    @Attribute(required=false)
    private boolean isListItem;

    @Text
    private String value;

    public ResourceParameter() { }

    public ResourceParameter(String name, String value) {
        this(name, value, false);
    }

    public ResourceParameter(String name, boolean value, boolean isListItem) {
        this(name, Boolean.toString(value), isListItem);
    }

    public ResourceParameter(String name, String value, boolean isListItem) {
        setName(name);
        setValue(value);
        isListItem(isListItem);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isListItem() {
        return isListItem;
    }

    public void isListItem(boolean listItem) {
        isListItem = listItem;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
