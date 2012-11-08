/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.android.sdk.client.oxm.control;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.4
 */
@Root(name="option")
public class InputControlOption {

    public InputControlOption() { }

    public InputControlOption(String label, String value) {
        this(label, value, false);
    }

    public InputControlOption(String label, String value, boolean selected) {
        this.label = label;
        this.value = value;
        this.selected = selected;
    }

    @Element
    private String label;

    @Element
    private String value;

    @Element
    private boolean selected;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return label;
    }
}
