/*
 * Copyright (C) 2012-2013 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.async.request.cacheable;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlStateList;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;

import java.util.List;

/**
 * Request that gets the states with updated values for input controls with specified IDs
 * and according to specified parameters.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class GetUpdatedInputControlsRequest extends CacheableRequest<InputControlStateList> {

    private String reportUri;
    private List<String> controlsIds;
    private List<ReportParameter> selectedValues;

    /**
     * Creates a new instance of {@link com.jaspersoft.android.sdk.client.async.request.cacheable.GetUpdatedInputControlsRequest}.
     *
     * @param reportUri repository URI of the report
     * @param controlsIds list of input controls IDs
     * @param selectedValues list of selected values
     */
    public GetUpdatedInputControlsRequest(JsRestClient jsRestClient, String reportUri, List<String> controlsIds,
                                          List<ReportParameter> selectedValues) {
        super(jsRestClient, InputControlStateList.class);
        this.reportUri = reportUri;
        this.controlsIds = controlsIds;
        this.selectedValues = selectedValues;
    }

    @Override
    public InputControlStateList loadDataFromNetwork() throws Exception {
        return getJsRestClient().getUpdatedInputControlsValuesList(reportUri, controlsIds, selectedValues);
    }

    @Override
    protected String createCacheKeyString() {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(super.createCacheKeyString());
        keyBuilder.append(reportUri);
        for (String id : controlsIds) keyBuilder.append(id);
        for (ReportParameter parameter : selectedValues) {
            keyBuilder.append(parameter.getName());
            for (String value : parameter.getValues()) {
                keyBuilder.append(value);
            }
        }
        return keyBuilder.toString();
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getReportUri() {
        return reportUri;
    }

    public List<String> getControlsIds() {
        return controlsIds;
    }

    public List<ReportParameter> getSelectedValues() {
        return selectedValues;
    }
}
