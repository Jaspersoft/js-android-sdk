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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.OutputFtpInfoEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.RepositoryDestinationEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobFtpAuthKey;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobOutputFtpInfo;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JobJobOutputFtpInfoMapperTest {

    private final JobFormFactory factory = new JobFormFactory();

    private JobOutputFtpInfoMapper mapperUnderTest;
    private JobFormEntity mappedEntity;
    private RepositoryDestination.Builder mappedBuilder;
    private JobForm targetFrom;
    private OutputFtpInfoEntity mappedOutputInfoEntity;
    private JobFormEntity targetEntity;
    private JobOutputFtpInfo mappedOutputInfo;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = new JobOutputFtpInfoMapper();
        mappedEntity = factory.givenNewJobFormEntity();
        mappedBuilder = new RepositoryDestination.Builder();
    }

    @Test
    public void should_map_type_from_form_to_entity() throws Exception {
        givenFormWithFtpType(JobOutputFtpInfo.Type.FTP);

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getType(), is("ftp"));
    }

    @Test
    public void should_map_auth_key_from_form_to_entity() throws Exception {
        givenFormWithAuthenticationKey("/public/id_rsa.pub", "1234");

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getSshKeyPath(), is("/public/id_rsa.pub"));
        assertThat(mappedOutputInfoEntity.getSshPassPhrase(), is("1234"));
    }

    @Test
    public void should_map_prot_from_form_to_entity() throws Exception {
        givenFormWithProt(JobOutputFtpInfo.Prot.CLEAR);

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getProt(), is("C"));
    }

    @Test
    public void should_map_protocol_from_form_to_entity() throws Exception {
        givenFormWithProtocol(JobOutputFtpInfo.Protocol.SSL);

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getProtocol(), is("SSL"));
    }

    @Test
    public void should_map_protection_buffer_size_from_form_to_entity() throws Exception {
        givenFormWithProtectionBufferSize(10);

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getProtectionBufferSize(), is(10));
    }

    @Test
    public void should_map_port_from_form_to_entity() throws Exception {
        givenFormWithPort(80);

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getPort(), is(80));
    }

    @Test
    public void should_map_implicit_flag_from_form_to_entity() throws Exception {
        givenFormWithImplicitFlag(true);

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getImplicit(), is(true));
    }

    @Test
    public void should_map_password_from_form_to_entity() throws Exception {
        givenFormWithPassword("1234");

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getPassword(), is("1234"));
    }

    @Test
    public void should_map_username_from_form_to_entity() throws Exception {
        givenFormWithUserName("Joe");

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getUserName(), is("Joe"));
    }

    @Test
    public void should_map_folder_path_from_form_to_entity() throws Exception {
        givenFormWithFolderPath("/temp");

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getFolderPath(), is("/temp"));
    }

    @Test
    public void should_map_server_name_from_form_to_entity() throws Exception {
        givenFormWithServerName("jrs");

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getServerName(), is("jrs"));
    }

    @Test
    public void should_map_type_from_entity_to_form() throws Exception {
        givenEntityWithFtpType("ftps");

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getType(), is(JobOutputFtpInfo.Type.FTPS));
    }


    @Test
    public void should_map_auth_key_from_entity_to_form() throws Exception {
        givenEntityWithAuthKey("/public/id_rsa.pub", "1234");

        whenMapsEntityToForm();

        JobFtpAuthKey key = mappedOutputInfo.getAuthenticationKey();
        assertThat(key.getKeyPath(), is("/public/id_rsa.pub"));
        assertThat(key.getPassPhrase(), is("1234"));
    }

    @Test
    public void should_map_prot_from_entity_to_form() throws Exception {
        givenEntityWithProt("C");

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getProt(), is(JobOutputFtpInfo.Prot.CLEAR));
    }

    @Test
    public void should_map_protocol_from_entity_to_form() throws Exception {
        givenEntityWithProtocol("SSL");

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getProtocol(), is(JobOutputFtpInfo.Protocol.SSL));
    }

    @Test
    public void should_map_protection_buffer_size_from_entity_to_form() throws Exception {
        givenEntityWithProtectionBufferSize(10);

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getProtectionBufferSize(), is(10));
    }

    @Test
    public void should_map_port_from_entity_to_form() throws Exception {
        givenEntityWithPort(80);

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getPort(), is(80));
    }

    @Test
    public void should_map_implicit_flag_from_entity_to_form() throws Exception {
        givenEntityWithImplicitFlag(true);

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getImplicit(), is(true));
    }

    @Test
    public void should_map_password_from_entity_to_form() throws Exception {
        givenEntityWithPassword("1234");

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getPassword(), is("1234"));
    }

    @Test
    public void should_map_username_from_entity_to_form() throws Exception {
        givenEntityWithUserName("joe");

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getUserName(), is("joe"));
    }

    @Test
    public void should_map_folder_path_from_entity_to_form() throws Exception {
        givenEntityWithFolderPath("/temp");

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getFolderPath(), is("/temp"));
    }

    @Test
    public void should_map_server_name_from_entity_to_form() throws Exception {
        givenEntityWithServerName("jrs");

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getServerName(), is("jrs"));
    }

    private void givenFormWithFtpType(JobOutputFtpInfo.Type type) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withType(type)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithAuthenticationKey(String path, String phrase) {
        JobFtpAuthKey authenticationKey = JobFtpAuthKey.newPair(path, phrase);
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withAuthenticationKey(authenticationKey)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithProt(JobOutputFtpInfo.Prot prot) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withProt(prot)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithProtocol(JobOutputFtpInfo.Protocol protocol) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withProtocol(protocol)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithProtectionBufferSize(int protectionBufferSize) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withProtectionBufferSize(protectionBufferSize)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithPort(int port) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withPort(port)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithImplicitFlag(boolean implicit) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withImplicit(implicit)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithPassword(String password) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withPassword(password)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithUserName(String userName) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withUserName(userName)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithFolderPath(String folderPath) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withFolderPath(folderPath)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithServerName(String serverName) {
        JobOutputFtpInfo ftpInfo = new JobOutputFtpInfo.Builder()
                .withServerName(serverName)
                .build();
        createForm(ftpInfo);
    }

    private void createForm(JobOutputFtpInfo ftpInfo) {
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withFtp(ftpInfo)
                .build();
        targetFrom = factory.givenJobFormBuilderWithValues()
                .withRepositoryDestination(destination)
                .build();
    }

    private void givenEntityWithFtpType(String type) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setType(type);
    }

    private void givenEntityWithAuthKey(String path, String phrase) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setSshKeyPath(path);
        infoEntity.setSshPassPhrase(phrase);
    }

    private void givenEntityWithProt(String prot) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setProt(prot);
    }

    private void givenEntityWithProtocol(String protocol) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setProtocol(protocol);
    }

    private void givenEntityWithProtectionBufferSize(int pbsz) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setProtectionBufferSize(pbsz);
    }

    private void givenEntityWithPort(int port) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setPort(port);
    }

    private void givenEntityWithImplicitFlag(boolean implicit) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setImplicit(implicit);
    }

    private void givenEntityWithPassword(String password) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setPassword(password);
    }

    private void givenEntityWithUserName(String userName) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setUserName(userName);
    }

    private void givenEntityWithFolderPath(String folderPath) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setFolderPath(folderPath);
    }

    private void givenEntityWithServerName(String serverName) {
        OutputFtpInfoEntity infoEntity = givenTargetInfoEntity();
        infoEntity.setServerName(serverName);
    }

    private OutputFtpInfoEntity givenTargetInfoEntity() {
        targetEntity = factory.givenJobFormEntityWithValues();
        RepositoryDestinationEntity repoDestination = targetEntity.getRepoDestination();
        OutputFtpInfoEntity infoEntity = new OutputFtpInfoEntity();
        repoDestination.setOutputFTPInfo(infoEntity);
        return infoEntity;
    }

    private void whenMapsFormToEntity() {
        mapperUnderTest.mapFormOnEntity(targetFrom, mappedEntity);
        RepositoryDestinationEntity repoDestination = mappedEntity.getRepoDestination();
        mappedOutputInfoEntity = repoDestination.getOutputFTPInfo();
    }

    private void whenMapsEntityToForm() {
        mapperUnderTest.mapEntityOnForm(mappedBuilder, targetEntity);
        mappedOutputInfo = mappedBuilder.build().getOutputFtpInfo();
    }
}