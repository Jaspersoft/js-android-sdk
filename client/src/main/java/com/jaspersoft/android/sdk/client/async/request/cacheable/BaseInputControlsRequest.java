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

package com.jaspersoft.android.sdk.client.async.request.cacheable;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.control.InputControl;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 1.6
 */
abstract class BaseInputControlsRequest<T> extends CacheableRequest<T> {

    protected static final String TAG_IC_VALUES = "INPUT_CONTROLS_VALUES";

    private String reportUri;
    private List<String> controlsIds;
    private List<ReportParameter> selectedValues;


    BaseInputControlsRequest(JsRestClient jsRestClient, String reportUri, Class<T> clazz) {
        this(jsRestClient, reportUri, new ArrayList<String>(), new ArrayList<ReportParameter>(), clazz);
    }

    BaseInputControlsRequest(JsRestClient jsRestClient, String reportUri, List<InputControl> inputControls, Class<T> clazz) {
        this(jsRestClient, reportUri, clazz);
        for(InputControl control : inputControls) {
            controlsIds.add(control.getId());
            selectedValues.add(new ReportParameter(control.getId(), control.getSelectedValues()));
        }
    }

    BaseInputControlsRequest(JsRestClient jsRestClient, String reportUri, List<String> controlsIds,
                             List<ReportParameter> selectedValues, Class<T> clazz) {
        super(jsRestClient, clazz);
        this.reportUri = reportUri;
        this.controlsIds = controlsIds;
        this.selectedValues = selectedValues;
    }


    @Override
    public abstract T loadDataFromNetwork() throws Exception;

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
