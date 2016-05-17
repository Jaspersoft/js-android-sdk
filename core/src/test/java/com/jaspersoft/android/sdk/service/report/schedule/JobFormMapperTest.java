package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobOutputFormat;
import com.jaspersoft.android.sdk.service.data.schedule.JobSource;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobFormMapperTest {

    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());
    private static final TimeZone TIME_ZONE = TimeZone.getDefault();

    private static Date START_DATE;

    public static final String START_DATE_SRC = "2013-10-03 16:32";

    static {
        try {
            START_DATE = DATE_FORMAT.parse(START_DATE_SRC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private JobFormMapper mJobFormMapper;
    private JobForm.Builder mJobBuilder;
    private JobForm mForm;
    private JobFormEntity mEntity;
    private JobFormEntity mPreparedEntity;

    @Mock
    JobTriggerMapper mJobTriggerMapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mJobFormMapper = new JobFormMapper(mJobTriggerMapper);

        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withFolderUri("/temp")
                .build();
        JobSource source = new JobSource.Builder()
                .withUri("/my/uri")
                .build();
        List<JobOutputFormat> jobOutputFormats = new ArrayList<>();
        jobOutputFormats.add(JobOutputFormat.HTML);
        jobOutputFormats.add(JobOutputFormat.CSV);

        mJobBuilder = new JobForm.Builder()
                .withVersion(100)
                .withLabel("my label")
                .withDescription("Description")
                .withRepositoryDestination(destination)
                .withJobSource(source)
                .withOutputFormats(jobOutputFormats)
                .withBaseOutputFilename("output")
                .withStartDate(START_DATE)
                .withTimeZone(TIME_ZONE);

        mForm = mJobBuilder.build();
        mEntity = new JobFormEntity();

        mPreparedEntity = new JobFormEntity();
        mPreparedEntity.setVersion(100);
        mPreparedEntity.setDescription("description");
        mPreparedEntity.setLabel("label");
        mPreparedEntity.setSourceUri("/my/uri");
        mPreparedEntity.setSourceParameters(Collections.singletonMap("key", Collections.singleton("value")));
        mPreparedEntity.addOutputFormats(Arrays.asList("PDF"));
        mPreparedEntity.setRepositoryDestination("/folder/uri");
        mPreparedEntity.setBaseOutputFilename("file.txt");
    }

    @Test
    public void should_map_form_to_common_entity_fields() throws Exception {
        mJobFormMapper.mapFormToCommonEntityFields(mForm, mEntity);

        assertThat(mEntity.getVersion(), is(100));
        assertThat(mEntity.getLabel(), is("my label"));
        assertThat(mEntity.getDescription(), is("Description"));
        assertThat(mEntity.getBaseOutputFilename(), is("output"));
    }

    @Test
    public void should_map_form_destination_on_entity() throws Exception {
        mJobFormMapper.mapFormDestinationOnEntity(mForm, mEntity);

        assertThat(mEntity.getRepositoryDestination(), is("/temp"));
    }

    @Test
    public void should_map_form_source_on_entity() throws Exception {
        mJobFormMapper.mapFormSourceOnEntity(mForm, mEntity);

        assertThat(mEntity.getSourceUri(), is("/my/uri"));
    }

    @Test
    public void should_map_form_formats_on_entity() throws Exception {
        mJobFormMapper.mapFormFormatsOnEntity(mForm, mEntity);

        assertThat(mEntity.getOutputFormats(), hasItems("HTML", "CSV"));
    }

    @Test
    public void should_map_source_param_values() throws Exception {
        List<ReportParameter> parameters = Collections.singletonList(
                new ReportParameter("key", Collections.singleton("value")));

        JobSource source = new JobSource.Builder()
                .withUri("/my/uri")
                .withParameters(parameters)
                .build();

        JobForm form = mJobBuilder
                .withJobSource(source)
                .build();
        JobFormEntity entity = mJobFormMapper.toFormEntity(form);


        Map<String, Set<String>> params = entity.getSourceParameters();
        Collection<String> values = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : params.entrySet()) {
            values.addAll(entry.getValue());
        }

        assertThat(params.keySet(), hasItem("key"));
        assertThat(values, hasItem("value"));
    }

    @Test
    public void should_map_entity_common_fields_to_form() throws Exception {
        JobForm.Builder form = new JobForm.Builder();
        mJobFormMapper.mapEntityCommonFieldsToForm(form, mPreparedEntity);
        mJobFormMapper.mapEntitySourceToForm(form, mPreparedEntity);
        mJobFormMapper.mapEntityDestinationToForm(form, mPreparedEntity);
        mJobFormMapper.mapEntityFormatsToForm(form, mPreparedEntity);

        JobForm expected = form.build();
        assertThat(expected.getLabel(), is("label"));
        assertThat(expected.getVersion(), is(100));
        assertThat(expected.getDescription(), is("description"));
        assertThat(expected.getOutputFormats(), hasItem(JobOutputFormat.PDF));
        assertThat(expected.getRepositoryDestination().getFolderUri(), is("/folder/uri"));
        assertThat(expected.getBaseOutputFilename(), is("file.txt"));

        JobSource source = expected.getSource();
        assertThat(source.getUri(), is("/my/uri"));
        assertThat(source.getParameters(), hasItem(new ReportParameter("key", Collections.singleton("value"))));
    }
}