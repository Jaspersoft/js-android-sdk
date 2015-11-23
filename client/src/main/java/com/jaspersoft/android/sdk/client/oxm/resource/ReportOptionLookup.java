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

package com.jaspersoft.android.sdk.client.oxm.resource;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 1.10
 */
@Root(strict=false, name = "reportOptions")
public class ReportOptionLookup extends ResourceLookup {
    @Expose
    @Element(required=false)
    protected String reportUri;

    @Expose
    @SerializedName("reportParameters")
    @ElementList(inline=true, empty=false)
    private List<ReportParameter> reportParameters = new ArrayList<ReportParameter>();

    public String getReportUri() {
       return reportUri;
    }

    public List<ReportParameter> getReportParameters() {
        return reportParameters;
    }
}
