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

package com.jaspersoft.android.sdk.network.entity.control;

import com.google.gson.annotations.Expose;

import java.util.Collections;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class InputControl {

    @Expose
    private String id;
    @Expose
    private String label;
    @Expose
    private boolean mandatory;
    @Expose
    private boolean readOnly;
    @Expose
    private String type;
    @Expose
    private String uri;
    @Expose
    private boolean visible;
    @Expose
    private Set<String> masterDependencies  = Collections.emptySet();
    @Expose
    private Set<String> slaveDependencies  = Collections.emptySet();
    @Expose
    private Set<ValidationRule> validationRules  = Collections.emptySet();
    @Expose
    private InputControlState state;

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public Set<String> getMasterDependencies() {
        return masterDependencies;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Set<String> getSlaveDependencies() {
        return slaveDependencies;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

    public Set<ValidationRule> getValidationRules() {
        return Collections.unmodifiableSet(validationRules);
    }

    public boolean isVisible() {
        return visible;
    }

    public InputControlState getState() {
        return state;
    }
}
