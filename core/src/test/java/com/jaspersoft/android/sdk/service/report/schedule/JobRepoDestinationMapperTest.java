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
import com.jaspersoft.android.sdk.network.entity.schedule.RepositoryDestinationEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class JobRepoDestinationMapperTest {
    private final JobFormFactory formFactory = new JobFormFactory();
    private JobRepoDestinationMapper mapperUnderTest;

    private JobForm.Builder serviceFormBuilder;
    private JobForm serviceForm;
    private JobFormEntity networkForm;
    private RepositoryDestination expectedRepoDestination;
    private RepositoryDestinationEntity expectedEntityRepoDestination;

    @Rule
    public ExpectedException expected = none();

    @Mock
    JobOutputFtpInfoMapper ftpInfoMapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mapperUnderTest = new JobRepoDestinationMapper(ftpInfoMapper);
    }

    @Test
    public void should_map_form_folderUri_on_entity() throws Exception {
        givenNetworkForm();
        givenFormRepoDestinationWithFolderUri("/temp");

        whenMapsFormOnEntity();
        thenShouldMapFtpInfoOnEntity();

        assertThat(expectedEntityRepoDestination.getFolderURI(), is("/temp"));
    }

    @Test
    public void should_map_form_outputLocalFolder_on_entity() throws Exception {
        givenNetworkForm();
        givenFormRepoDestinationWithOutputLocalFolder("/temp");

        whenMapsFormOnEntity();
        thenShouldMapFtpInfoOnEntity();

        assertThat(expectedEntityRepoDestination.getOutputLocalFolder(), is("/temp"));
    }

    @Test
    public void should_map_form_OutputDescription_on_entity() throws Exception {
        givenNetworkForm();
        givenFormRepoDestinationWithOutputDescription("Output description");

        whenMapsFormOnEntity();
        thenShouldMapFtpInfoOnEntity();

        assertThat(expectedEntityRepoDestination.getOutputDescription(), is("Output description"));
    }

    @Test
    public void should_map_form_TimestampPattern_on_entity() throws Exception {
        givenNetworkForm();
        givenFormRepoDestinationWithTimestampPattern("yyyy");

        whenMapsFormOnEntity();
        thenShouldMapFtpInfoOnEntity();

        assertThat(expectedEntityRepoDestination.getTimestampPattern(), is("yyyy"));
    }

    @Test
    public void should_map_form_DefaultReportOutputFolderURI_on_entity() throws Exception {
        givenNetworkForm();
        givenFormRepoDestinationWithDefaultReportOutputFolderURI("/temp");

        whenMapsFormOnEntity();
        thenShouldMapFtpInfoOnEntity();

        assertThat(expectedEntityRepoDestination.getDefaultReportOutputFolderURI(), is("/temp"));
    }

    @Test
    public void should_map_form_SequentialFilenames_on_entity() throws Exception {
        givenNetworkForm();
        givenFormRepoDestinationWithSequentialFilenames(true);

        whenMapsFormOnEntity();
        thenShouldMapFtpInfoOnEntity();

        assertThat(expectedEntityRepoDestination.getSequentialFilenames(), is(true));
    }

    @Test
    public void should_map_form_OverwriteFiles_on_entity() throws Exception {
        givenNetworkForm();
        givenFormRepoDestinationWithOverwriteFiles(true);

        whenMapsFormOnEntity();
        thenShouldMapFtpInfoOnEntity();

        assertThat(expectedEntityRepoDestination.getOverwriteFiles(), is(true));
    }

    @Test
    public void should_map_form_SaveToRepository_on_entity() throws Exception {
        givenNetworkForm();
        givenFormRepoDestinationWithSaveToRepository(true);

        whenMapsFormOnEntity();
        thenShouldMapFtpInfoOnEntity();

        assertThat(expectedEntityRepoDestination.getSaveToRepository(), is(true));
    }

    @Test
    public void should_map_entity_folderUri_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithFolderUri("/folder/uri");

        whenMapsEntityOnForm();
        thenShouldMapFtpInfoOnForm();

        assertThat(expectedRepoDestination.getFolderUri(), is("/folder/uri"));
    }

    @Test
    public void should_map_entity_outputLocalFolder_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithOutputLocalFolder("/folder/uri");

        whenMapsEntityOnForm();
        thenShouldMapFtpInfoOnForm();

        assertThat(expectedRepoDestination.getOutputLocalFolder(), is("/folder/uri"));
    }

    @Test
    public void should_map_entity_outputDescription_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithOutputDescription("Output description!");

        whenMapsEntityOnForm();
        thenShouldMapFtpInfoOnForm();

        assertThat(expectedRepoDestination.getOutputDescription(), is("Output description!"));
    }

    @Test
    public void should_map_timestampPattern_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithTimestampPattern("yyyy");

        whenMapsEntityOnForm();
        thenShouldMapFtpInfoOnForm();

        assertThat(expectedRepoDestination.getTimestampPattern(), is("yyyy"));
    }

    @Test
    public void should_map_DefaultReportOutputFolderURI_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithDefaultReportOutputFolderURI("/folder/uri");

        whenMapsEntityOnForm();
        thenShouldMapFtpInfoOnForm();

        assertThat(expectedRepoDestination.getDefaultReportOutputFolderURI(), is("/folder/uri"));
    }

    @Test
    public void should_map_SequentialFilenames_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithSequentialFilenames(true);

        whenMapsEntityOnForm();
        thenShouldMapFtpInfoOnForm();

        assertThat(expectedRepoDestination.getSequentialFileNames(), is(true));
    }

    @Test
    public void should_map_OverwriteFiles_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithOverwriteFiles(true);

        whenMapsEntityOnForm();
        thenShouldMapFtpInfoOnForm();

        assertThat(expectedRepoDestination.getOverwriteFiles(), is(true));
    }

    @Test
    public void should_map_SaveToRepository_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithSaveToRepository(true);

        whenMapsEntityOnForm();
        thenShouldMapFtpInfoOnForm();

        assertThat(expectedRepoDestination.getSaveToRepository(), is(true));
    }

    @Test
    public void should_map_UseDefaultReportOutputFolderURI_on_form() throws Exception {
        givenServiceFormBuilder();
        givenRepositoryDestinationWithUseDefaultReportOutputFolderURI(true);

        whenMapsEntityOnForm();
        thenShouldMapFtpInfoOnForm();

        assertThat(expectedRepoDestination.getUseDefaultReportOutputFolderURI(), is(true));
    }

    private void whenMapsEntityOnForm() {
        mapperUnderTest.mapEntityOnForm(serviceFormBuilder, networkForm);
        JobForm mappedServiceForm = serviceFormBuilder.build();
        expectedRepoDestination = mappedServiceForm.getRepositoryDestination();
    }

    private void whenMapsFormOnEntity() {
        mapperUnderTest.mapFormOnEntity(serviceForm, networkForm);
        expectedEntityRepoDestination = networkForm.getRepoDestination();
    }

    private void givenFormRepoDestinationWithFolderUri(String uri) {
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withFolderUri(uri)
                .build();
        createForm(destination);
    }

    private void givenFormRepoDestinationWithSaveToRepository(boolean flag) {
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withSaveToRepository(flag)
                .build();
        createForm(destination);
    }

    private void givenFormRepoDestinationWithOverwriteFiles(boolean flag) {
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withOverwriteFiles(flag)
                .build();
        createForm(destination);
    }

    private void givenFormRepoDestinationWithSequentialFilenames(boolean flag) {
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withSequentialFileNames(flag)
                .build();
        createForm(destination);
    }

    private void givenFormRepoDestinationWithDefaultReportOutputFolderURI(String uri) {
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withDefaultReportOutputFolderURI(uri)
                .build();
        createForm(destination);
    }

    private void createForm(RepositoryDestination destination) {
        serviceForm = formFactory.givenJobFormBuilderWithValues()
                .withRepositoryDestination(destination)
                .build();
    }

    private void givenFormRepoDestinationWithTimestampPattern(String pattern) {
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withTimestampPattern(pattern)
                .build();
        createForm(destination);
    }

    private void givenFormRepoDestinationWithOutputDescription(String desc) {
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withOutputDescription(desc)
                .build();
        createForm(destination);
    }

    private void givenFormRepoDestinationWithOutputLocalFolder(String uri) {
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withOutputLocalFolder(uri)
                .build();
        createForm(destination);
    }

    private void givenServiceFormBuilder() {
        serviceFormBuilder = formFactory.givenJobFormBuilderWithValues();
    }

    private void givenNetworkForm() {
        networkForm = formFactory.givenNewJobFormEntity();
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

    private void thenShouldMapFtpInfoOnForm() {
        verify(ftpInfoMapper).mapEntityOnForm(serviceFormBuilder, networkForm);
    }

    private void thenShouldMapFtpInfoOnEntity() {
        verify(ftpInfoMapper).mapFormOnEntity(serviceForm, networkForm);
    }
}