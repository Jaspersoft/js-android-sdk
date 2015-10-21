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

package com.jaspersoft.android.sdk.client.oxm.report.adapter;

import com.jaspersoft.android.sdk.client.oxm.report.ExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.server.ServerInfo;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class ExecutionRequestAdapter {

    private final String versionCode;

    private ExecutionRequestAdapter(String versionCode) {
        this.versionCode = versionCode;
    }

    public static ExecutionRequestAdapter newInstance(String versionCode) {
        if (versionCode == null) {
            throw new IllegalArgumentException("VersionCode should not be null");
        }
        if (versionCode.trim().length() == 0) {
            throw new IllegalArgumentException("VersionCode should not be empty");
        }
        return new ExecutionRequestAdapter(versionCode);
    }

    public <T extends ExecutionRequest> T adapt(T adaptee) {
        if (isAmberMR2()) {
            adaptee.setAllowInlineScripts(null);
            adaptee.setBaseUrl(null);
            adaptee.setMarkupType(null);
        }
        return adaptee;
    }

    private boolean isAmberMR2() {
        return String.valueOf(ServerInfo.VERSION_CODES.EMERALD_TWO).equals(versionCode);
    }
}
