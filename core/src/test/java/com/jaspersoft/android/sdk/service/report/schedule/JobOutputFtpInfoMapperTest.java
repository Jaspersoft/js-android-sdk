package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.OutputFtpInfoEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.RepositoryDestinationEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.OutputFtpInfo;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JobOutputFtpInfoMapperTest {

    private final JobFormFactory factory = new JobFormFactory();

    private JobOutputFtpInfoMapper mapperUnderTest;
    private JobFormEntity mappedEntity;
    private RepositoryDestination.Builder mappedBuilder;
    private JobForm targetFrom;
    private OutputFtpInfoEntity mappedOutputInfoEntity;
    private JobFormEntity targetEntity;
    private OutputFtpInfo mappedOutputInfo;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = new JobOutputFtpInfoMapper();
        mappedEntity = factory.givenNewJobFormEntity();
        mappedBuilder = new RepositoryDestination.Builder();
    }

    @Test
    public void should_map_type_from_form_to_entity() throws Exception {
        givenFormWithFtpType(OutputFtpInfo.Type.FTP);

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getType(), is("ftp"));
    }

    @Test
    public void should_map_prot_from_form_to_entity() throws Exception {
        givenFormWithProt(OutputFtpInfo.Prot.CLEAR);

        whenMapsFormToEntity();

        assertThat(mappedOutputInfoEntity.getProt(), is("C"));
    }

    @Test
    public void should_map_protocol_from_form_to_entity() throws Exception {
        givenFormWithProtocol(OutputFtpInfo.Protocol.SSL);

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

        assertThat(mappedOutputInfo.getType(), is(OutputFtpInfo.Type.FTPS));
    }

    @Test
    public void should_map_prot_from_entity_to_form() throws Exception {
        givenEntityWithProt("C");

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getProt(), is(OutputFtpInfo.Prot.CLEAR));
    }

    @Test
    public void should_map_protocol_from_entity_to_form() throws Exception {
        givenEntityWithProtocol("SSL");

        whenMapsEntityToForm();

        assertThat(mappedOutputInfo.getProtocol(), is(OutputFtpInfo.Protocol.SSL));
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

    private void givenFormWithFtpType(OutputFtpInfo.Type type) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withType(type)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithProt(OutputFtpInfo.Prot prot) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withProt(prot)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithProtocol(OutputFtpInfo.Protocol protocol) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withProtocol(protocol)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithProtectionBufferSize(int protectionBufferSize) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withProtectionBufferSize(protectionBufferSize)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithPort(int port) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withPort(port)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithImplicitFlag(boolean implicit) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withImplicit(implicit)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithPassword(String password) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withPassword(password)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithUserName(String userName) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withUserName(userName)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithFolderPath(String folderPath) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withFolderPath(folderPath)
                .build();
        createForm(ftpInfo);
    }

    private void givenFormWithServerName(String serverName) {
        OutputFtpInfo ftpInfo = new OutputFtpInfo.Builder()
                .withServerName(serverName)
                .build();
        createForm(ftpInfo);
    }

    private void createForm(OutputFtpInfo ftpInfo) {
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