/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.network.entity.execution;

import com.google.gson.annotations.Expose;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ErrorDescriptor {
    @Expose
    private String errorCode;
    @Expose
    private String message;
    @Expose
    private List<ErrorDescriptorItem> error;
    @Expose
    private Set<String> parameters = Collections.emptySet();

    public Set<String> getErrorCodes() {
        if (errorCode != null) {
            return Collections.singleton(errorCode);
        }
        if (error != null) {
            Set<String> codes = new HashSet<>();
            for (ErrorDescriptorItem item : error) {
                codes.add(item.getErrorCode());
            }
            return codes;
        }
        return Collections.emptySet();
    }

    public String getMessage() {
        return message;
    }

    public Set<String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "ErrorDescriptor{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                ", parameters=" + Arrays.toString(parameters.toArray()) +
                '}';
    }

    public String getFieldByCode(String requestedErrorCode) {
        if (error == null || error.isEmpty()) return null;

        for (ErrorDescriptorItem item : error) {
            if (item.getErrorCode().equals(requestedErrorCode)) {
                return item.getField();
            }
        }
        return null;
    }
}
