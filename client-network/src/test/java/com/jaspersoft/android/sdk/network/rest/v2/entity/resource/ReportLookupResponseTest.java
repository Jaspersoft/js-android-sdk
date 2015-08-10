/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.network.rest.v2.entity.resource;

import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportLookupResponseTest {
    @com.jaspersoft.android.sdk.test.resource.ResourceFile("json/report_unit_resource1.json")
    TestResource reportResponse1;
    @com.jaspersoft.android.sdk.test.resource.ResourceFile("json/report_unit_resource2.json")
    TestResource reportResponse2;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
    }

    @Test
    public void shouldDeserializeResponse1FromWholeJson() {
        ReportLookupResponse response1 = deserialize(reportResponse1.asString());
        assertThat(response1, is(notNullValue()));
    }

    @Test
    public void shouldDeserializeResponse2FromWholeJson() {
        ReportLookupResponse response2 = deserialize(reportResponse2.asString());
        assertThat(response2, is(notNullValue()));
    }

    @Test
    public void shouldAlwaysReturnReportUnitUriAsType() {
        ReportLookupResponse response = deserialize("{}");
        assertThat(response.getResourceType(), is("reportUnit"));
    }

    @Test
    public void shouldDeserializeVersion() {
        ReportLookupResponse response = deserialize("{\"version\": 2}");
        assertThat(response.getVersion(), is(2));
    }

    @Test
    public void shouldDeserializePermissionMask() {
        ReportLookupResponse response = deserialize("{\"permissionMask\": 1}");
        assertThat(response.getPermissionMask(), is(1));
    }

    @Test
    public void shouldDeserializeCreationDate() {
        ReportLookupResponse response = deserialize("{\"creationDate\": \"2015-06-05T07:21:11\"}");
        assertThat(response.getCreationDate(), is("2015-06-05T07:21:11"));
    }

    @Test
    public void shouldDeserializeUpdateDate() {
        ReportLookupResponse response = deserialize("{\"updateDate\": \"2014-05-14T17:38:49\"}");
        assertThat(response.getUpdateDate(), is("2014-05-14T17:38:49"));
    }

    @Test
    public void shouldDeserializeLabel() {
        ReportLookupResponse response = deserialize("{\"label\": \"01. Geographic Results by Segment\"}");
        assertThat(response.getLabel(), is("01. Geographic Results by Segment"));
    }

    @Test
    public void shouldDeserializeDescription() {
        ReportLookupResponse response = deserialize("{\"description\": \"Sample HTML5 multi-axis column chart\"}");
        assertThat(response.getDescription(), is("Sample HTML5 multi-axis column chart"));
    }

    @Test
    public void shouldDeserializeUri() {
        ReportLookupResponse response = deserialize("{\"uri\": \"/public/Samples/Ad_Hoc_Views/01__Geographic_Results_by_Segment\"}");
        assertThat(response.getUri(), is("/public/Samples/Ad_Hoc_Views/01__Geographic_Results_by_Segment"));
    }

    @Test
    public void shouldDeserializeDataSource() {
        ReportLookupResponse response = deserialize("{\"dataSource\": {\"dataSourceReference\": {\"uri\": \"/public/Samples/Data_Sources/JServerJNDIDS\"}}}");
        assertThat(response.getDataSource().getDataSourceReference().getUri(), is("/public/Samples/Data_Sources/JServerJNDIDS"));
    }

    @Test
    public void shouldDeserializeJrxml() {
        ReportLookupResponse response = deserialize("{\"jrxml\": {\"jrxmlFileReference\": {\"uri\": \"/public/Samples/Reports/AllAccounts_files/main_jrxml\"}}}");
        assertThat(response.getJrxml().getJrxmlFileReference().getUri(), is("/public/Samples/Reports/AllAccounts_files/main_jrxml"));
    }

    @Test
    public void shouldDeserializeInputControlRenderingView() {
        ReportLookupResponse response = deserialize("{\"inputControlRenderingView\": \"any view\"}");
        assertThat(response.getInputControlRenderingView(), is("any view"));
    }

    @Test
    public void shouldDeserializeReportRenderingView() {
        ReportLookupResponse response = deserialize("{\"reportRenderingView\": \"any view\"}");
        assertThat(response.getReportRenderingView(), is("any view"));
    }

    @Test
    public void shouldDeserializeAlwaysPrompt() {
        ReportLookupResponse response = deserialize("{\"alwaysPromptControls\": \"true\"}");
        assertThat(response.isAlwaysPromptControls(), is(true));
    }

    @Test
    public void shouldDeserializeControlsLayout() {
        ReportLookupResponse response = deserialize("{\"controlsLayout\": \"any layout\"}");
        assertThat(response.getControlsLayout(), is("any layout"));
    }

    @Test
    public void shouldDeserializeResources() {
        ReportLookupResponse response = deserialize("{\"resources\": {\"resource\": [{\"name\": \"SampleReportsStyles.jrtx\",\"file\": {\"fileReference\": {\"uri\": \"/public/Samples/Resources/Extras/SampleReportStyles.jrtx\"}}}]}}");

        ReportResource reportResource = response.getResources().get(0);
        ResourceFile file = reportResource.getFile();
        assertThat(file.getFileReference().getUri(), is("/public/Samples/Resources/Extras/SampleReportStyles.jrtx"));
        assertThat(reportResource.getName(), is("SampleReportsStyles.jrtx"));
    }

    private ReportLookupResponse deserialize(String json) {
        return GsonFactory.create().fromJson(json, ReportLookupResponse.class);
    }
}
