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

package com.jaspersoft.android.sdk.network.entity.resource;

import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportLookup extends ResourceLookup {

    @Expose
    private DataSource dataSource;
    @Expose
    private JRXml jrxml;
    @Expose
    private String inputControlRenderingView;
    @Expose
    private String reportRenderingView;
    @Expose
    private boolean alwaysPromptControls;
    @Expose
    private String controlsLayout;
    @Expose
    private List<ReportResource> resources;

    public ReportLookup() {}

    @Override
    public String getResourceType() {
        return "reportUnit";
    }

    public boolean isAlwaysPromptControls() {
        return alwaysPromptControls;
    }

    public String getControlsLayout() {
        return controlsLayout;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getInputControlRenderingView() {
        return inputControlRenderingView;
    }

    public JRXml getJrxml() {
        return jrxml;
    }

    public String getReportRenderingView() {
        return reportRenderingView;
    }

    public List<ReportResource> getResources() {
        return resources;
    }

    @Override
    public String toString() {
        return "ReportLookup{" +
                "alwaysPromptControls=" + alwaysPromptControls +
                ", dataSource=" + dataSource +
                ", jrxml=" + jrxml +
                ", inputControlRenderingView='" + inputControlRenderingView + '\'' +
                ", reportRenderingView='" + reportRenderingView + '\'' +
                ", controlsLayout='" + controlsLayout + '\'' +
                ", resources=" + Arrays.toString(resources.toArray()) +
                '}';
    }
}
