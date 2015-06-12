/*
 * Copyright (C) 2012-2013 Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.client.oxm.control;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 1.4
 */

@Root(name="inputControls")
public class InputControlsList {

    @Expose
    @SerializedName("inputControl")
    @ElementList(entry="inputControl", inline=true, empty=false)
    private List<InputControl> inputControls;

    public InputControlsList() {
        this.inputControls = new ArrayList<InputControl>();
    }

    public List<InputControl> getInputControls() {
        return inputControls;
    }

    public void setInputControls(List<InputControl> inputControls) {
        this.inputControls = inputControls;
    }
}
