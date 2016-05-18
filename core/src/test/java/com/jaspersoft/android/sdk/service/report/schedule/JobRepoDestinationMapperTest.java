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
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobRepoDestinationMapperTest {
    private final JobFormFactory formFactory = new JobFormFactory();
    private JobRepoDestinationMapper mapperUnderTest;

    private JobForm.Builder serviceFormBuilder;
    private JobFormEntity networkForm;
    private RepositoryDestination expectedRepoDestination;

    @Rule
    public ExpectedException expected = none();

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
    public void should_map_entity_folderUri_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithFolderUri("/folder/uri");

        whenMapsEntityOnForm();

        assertThat(expectedRepoDestination.getFolderUri(), is("/folder/uri"));
    }

    @Test
    public void should_map_entity_outputLocalFolder_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithOutputLocalFolder("/folder/uri");

        whenMapsEntityOnForm();

        assertThat(expectedRepoDestination.getOutputLocalFolder(), is("/folder/uri"));
    }

    @Test
    public void should_map_entity_outputDescription_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithOutputDescription("Output description!");

        whenMapsEntityOnForm();

        assertThat(expectedRepoDestination.getOutputDescription(), is("Output description!"));
    }

    @Test
    public void should_map_timestampPattern_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithTimestampPattern("yyyy");

        whenMapsEntityOnForm();

        assertThat(expectedRepoDestination.getTimestampPattern(), is("yyyy"));
    }

    @Test
    public void should_map_DefaultReportOutputFolderURI_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithDefaultReportOutputFolderURI("/folder/uri");

        whenMapsEntityOnForm();

        assertThat(expectedRepoDestination.getDefaultReportOutputFolderURI(), is("/folder/uri"));
    }

    @Test
    public void should_map_SequentialFilenames_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithSequentialFilenames(true);

        whenMapsEntityOnForm();

        assertThat(expectedRepoDestination.getSequentialFilenames(), is(true));
    }

    @Test
    public void should_map_OverwriteFiles_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithOverwriteFiles(true);

        whenMapsEntityOnForm();

        assertThat(expectedRepoDestination.getOverwriteFiles(), is(true));
    }

    @Test
    public void should_map_SaveToRepository_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithSaveToRepository(true);

        whenMapsEntityOnForm();

        assertThat(expectedRepoDestination.getSaveToRepository(), is(true));
    }

    @Test
    public void should_map_UseDefaultReportOutputFolderURI_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithUseDefaultReportOutputFolderURI(true);

        whenMapsEntityOnForm();

        assertThat(expectedRepoDestination.getUseDefaultReportOutputFolderURI(), is(true));
    }

    private void whenMapsEntityOnForm() {
        mapperUnderTest.mapEntityOnForm(serviceFormBuilder, networkForm);
        JobForm mappedServiceForm = serviceFormBuilder.build();
        expectedRepoDestination = mappedServiceForm.getRepositoryDestination();
    }

    private void givenServiceFormBuilder() {
        serviceFormBuilder = formFactory.givenJobFormBuilderWithValues();
    }

    private void givenNetworkForm() {
        networkForm = formFactory.givenJobFormEntityWithValues();
    }

    private void givenRepositoryDestinationWithOutputLocalFolder(String uri) {
        givenNetworkForm();
        networkForm.getRepoDestination().setOutputLocalFolder(uri);
    }

    private void givenRepositoryDestinationWithFolderUri(String uri) {
        givenNetworkForm();
        networkForm.getRepoDestination().setFolderURI(uri);
    }

    private void givenRepositoryDestinationWithOutputDescription(String desc) {
        givenNetworkForm();
        networkForm.getRepoDestination().setOutputDescription(desc);
    }

    private void givenRepositoryDestinationWithTimestampPattern(String pattern) {
        givenNetworkForm();
        networkForm.getRepoDestination().setTimestampPattern(pattern);
    }

    private void givenRepositoryDestinationWithDefaultReportOutputFolderURI(String uri) {
        givenNetworkForm();
        networkForm.getRepoDestination().setDefaultReportOutputFolderURI(uri);
    }

    private void givenRepositoryDestinationWithSequentialFilenames(boolean flag) {
        givenNetworkForm();
        networkForm.getRepoDestination().setSequentialFilenames(true);
    }

    private void givenRepositoryDestinationWithOverwriteFiles(boolean flag) {
        givenNetworkForm();
        networkForm.getRepoDestination().setOverwriteFiles(true);
    }

    private void givenRepositoryDestinationWithSaveToRepository(boolean flag) {
        givenNetworkForm();
        networkForm.getRepoDestination().setSaveToRepository(true);
    }

    private void givenRepositoryDestinationWithUseDefaultReportOutputFolderURI(boolean flag) {
        givenNetworkForm();
        networkForm.getRepoDestination().setUsingDefaultReportOutputFolderURI(true);
    }
}