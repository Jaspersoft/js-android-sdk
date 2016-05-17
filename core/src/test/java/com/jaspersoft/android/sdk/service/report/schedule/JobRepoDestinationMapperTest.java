/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobRepoDestinationMapperTest {
    private final JobFormFactory formFactory = new JobFormFactory();
    private JobRepoDestinationMapper mapperUnderTest;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = new JobRepoDestinationMapper();
    }

    @Test
    public void should_map_form_destination_on_entity() throws Exception {
        JobFormEntity mappedEntity = formFactory.givenJobFormEntityWithValues();
        JobForm form = formFactory.givenJobFormWithValues();

        mapperUnderTest.mapFormOnEntity(form, mappedEntity);

        assertThat(mappedEntity.getRepositoryDestination(), is("/temp"));
    }

    @Test
    public void should_map_entity_common_fields_to_form() throws Exception {
        JobFormEntity mappedEntity = formFactory.givenJobFormEntityWithValues();
        JobForm.Builder form = formFactory.givenJobFormBuilderWithValues();

        mapperUnderTest.mapEntityOnForm(form, mappedEntity);

        JobForm expected = form.build();
        assertThat(expected.getRepositoryDestination().getFolderUri(), is("/folder/uri"));
    }
}