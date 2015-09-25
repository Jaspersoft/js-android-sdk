/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.report.option.ReportOption;

import java.util.Map;
import java.util.Set;

/**
 * Class that wraps {@link JsRestClient} instance in order to send create request of report option
 *
 * @author Tom Koptel
 * @since 1.11
 */
public class CreateReportOptionsRequest extends BaseRequest<ReportOption> {
    private final String mReportUri;
    private final String mOptionLabel;
    private final Map<String, Set<String>> mControlsValue;
    private final boolean mOverwrite;

    public CreateReportOptionsRequest(JsRestClient jsRestClient, String reportUri,
                                      String optionLabel, Map<String, Set<String>> controlsValues,
                                      boolean overwrite) {
        super(jsRestClient, ReportOption.class);
        mReportUri = reportUri;
        mOptionLabel = optionLabel;
        mControlsValue = controlsValues;
        mOverwrite = overwrite;
    }
    public CreateReportOptionsRequest(JsRestClient jsRestClient, String reportUri,
                                      String optionLabel, Map<String, Set<String>> controlsValues) {
        this(jsRestClient, reportUri, optionLabel, controlsValues, true);
    }

    @Override
    public ReportOption loadDataFromNetwork() throws Exception {
        return getJsRestClient().createReportOption(mReportUri, mOptionLabel, mControlsValue, mOverwrite);
    }
}
