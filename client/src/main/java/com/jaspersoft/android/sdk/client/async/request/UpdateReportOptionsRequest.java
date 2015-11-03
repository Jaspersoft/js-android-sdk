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

package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;

import java.util.Map;
import java.util.Set;

/**
 * Class that wraps {@link JsRestClient} instance in order to send update request of report option
 *
 * @author Tom Koptel
 * @since 1.11
 */
public class UpdateReportOptionsRequest extends BaseRequest<Void> {
    private final String mReportUri;
    private final String mOptionId;
    private final Map<String, Set<String>> mControlsValue;

    public UpdateReportOptionsRequest(JsRestClient jsRestClient, String reportUri,
                                      String optionId, Map<String, Set<String>> controlsValues) {
        super(jsRestClient, Void.class);
        mReportUri = reportUri;
        mOptionId = optionId;
        mControlsValue = controlsValues;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        getJsRestClient().updateReportOption(mReportUri, mOptionId, mControlsValue);
        // We do not care about response
        return null;
    }
}
