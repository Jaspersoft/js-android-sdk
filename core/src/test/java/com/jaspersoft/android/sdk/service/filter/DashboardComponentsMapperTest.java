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

package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.dashboard.InputControlDashboardComponent;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardControlComponent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DashboardComponentsMapperTest {

    private static final String DASHBOARD_URI = "/dashboard/uri";

    private DashboardComponentsMapper mDashboardComponentsMapper;

    @Mock
    InputControlDashboardComponent mComponent1;
    @Mock
    InputControlDashboardComponent mComponent2;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mDashboardComponentsMapper = new DashboardComponentsMapper();
    }

    @Test
    public void should_accumulate_ids_for_same_location() throws Exception {
        when(mComponent1.getOwnerResourceId()).thenReturn("/public/uri");
        when(mComponent1.getOwnerResourceParameterName()).thenReturn("store_country_1");
        when(mComponent2.getOwnerResourceId()).thenReturn("/public/uri");
        when(mComponent2.getOwnerResourceParameterName()).thenReturn("store_country_2");

        DashboardComponentCollection componentCollection = new DashboardComponentCollection(Arrays.asList(mComponent1, mComponent2));
        List<ControlLocation> locations = mDashboardComponentsMapper.toLocations(DASHBOARD_URI, componentCollection);
        ControlLocation location = locations.get(0);

        assertThat(location.getUri(), is("/public/uri"));
        assertThat(location.getIds(), hasItems("store_country_1", "store_country_2"));
    }

    @Test
    public void should_map_differen_locations() throws Exception {
        when(mComponent1.getOwnerResourceId()).thenReturn("/another/uri");
        when(mComponent1.getOwnerResourceParameterName()).thenReturn("store_country_1");
        when(mComponent2.getOwnerResourceId()).thenReturn("/public/uri");
        when(mComponent2.getOwnerResourceParameterName()).thenReturn("store_country_2");

        DashboardComponentCollection componentCollection = new DashboardComponentCollection(Arrays.asList(mComponent1, mComponent2));
        List<ControlLocation> locations = mDashboardComponentsMapper.toLocations(DASHBOARD_URI, componentCollection);
        ControlLocation location1 = locations.get(0);
        ControlLocation location2 = locations.get(1);

        assertThat(Arrays.asList(location1.getUri(), location2.getUri()), hasItems("/public/uri", "/another/uri"));
    }

    @Test
    public void generate_uri_should_yield_owner_resource_id_by_default() throws Exception {
        String expected = mDashboardComponentsMapper.generateUri("/public/uri", DASHBOARD_URI);
        assertThat(expected, is("/public/uri"));
    }

    @Test
    public void generate_uri_should_combine_temp_owner_resource_id_with_dashboard_uri() throws Exception {
        String expected = mDashboardComponentsMapper.generateUri("/temp/tmpAdv_1414686450998_tqqd", DASHBOARD_URI);
        assertThat(expected, is("/dashboard/uri_files/tmpAdv_1414686450998_tqqd"));
    }

    @Test
    public void should_map_entity_component_to_data_counterpart() throws Exception {
        when(mComponent1.getId()).thenReturn("component_id");
        when(mComponent1.getOwnerResourceParameterName()).thenReturn("store_country_1");

        DashboardComponentCollection componentCollection =
                new DashboardComponentCollection(Collections.singletonList(mComponent1));
        List<DashboardControlComponent> components = mDashboardComponentsMapper.toComponents(componentCollection);
        DashboardControlComponent dashboardControlComponent = components.get(0);
        assertThat(dashboardControlComponent.getComponentId(), is("component_id"));
        assertThat(dashboardControlComponent.getControlId(), is("store_country_1"));
    }
}