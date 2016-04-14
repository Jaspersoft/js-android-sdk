package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobStateEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnitEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobUnitMapperTest {
    private static final SimpleDateFormat PREVIOUS_FIRE_TIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    private static final SimpleDateFormat NEXT_FIRE_TIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    private static final String PREVIOUS_FIRE_TIME = "2016-03-24T00:00:00+02:00";
    private static final String NEXT_FIRE_TIME = "2016-03-21T12:18:08.979+02:00";

    @Mock
    JobUnitEntity mJobUnitEntity;
    @Mock
    JobStateEntity mStateEntity;
    @Mock
    JobUnitDateParser jobUnitDateParser;

    private JobUnitMapper mJobUnitMapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(mJobUnitEntity.getId()).thenReturn(1);
        when(mJobUnitEntity.getVersion()).thenReturn(100);
        when(mJobUnitEntity.getLabel()).thenReturn("label");
        when(mJobUnitEntity.getDescription()).thenReturn("description");
        when(mJobUnitEntity.getReportUnitURI()).thenReturn("/my/uri");
        when(mJobUnitEntity.getReportLabel()).thenReturn("report label");
        when(mJobUnitEntity.getOwner()).thenReturn("jasperadmin|organization_1");

        when(mJobUnitEntity.getState()).thenReturn(mStateEntity);
        when(mStateEntity.getPreviousFireTime()).thenReturn(PREVIOUS_FIRE_TIME);
        when(mStateEntity.getNextFireTime()).thenReturn(NEXT_FIRE_TIME);
        when(mStateEntity.getValue()).thenReturn("NORMAL");

        mJobUnitMapper = new JobUnitMapper(jobUnitDateParser);
    }

    @Test
    public void should_map_collection_of_units() throws Exception {
        List<JobUnitEntity> entities = Collections.singletonList(mJobUnitEntity);
        List<JobUnit> expected = mJobUnitMapper.transform(entities);
        assertThat(expected.size(), is(1));
    }

    @Test
    public void should_map_entity_to_service_counterpart() throws Exception {
        long previousFireTime = PREVIOUS_FIRE_TIME_FORMAT.parse(PREVIOUS_FIRE_TIME).getTime();
        long nextFireTime = NEXT_FIRE_TIME_FORMAT.parse(NEXT_FIRE_TIME).getTime();

        JobUnit expected = mJobUnitMapper.transform(mJobUnitEntity);

        assertThat(expected.getId(), is(1));
        assertThat(expected.getVersion(), is(100));
        assertThat(expected.getLabel(), is("label"));
        assertThat(expected.getDescription(), is("description"));
        assertThat(expected.getReportUri(), is("/my/uri"));
        assertThat(expected.getReportLabel(), is("report label"));
        assertThat(expected.getOwner().toString(), is("jasperadmin|organization_1"));
        assertThat(expected.getState().toString(), is("NORMAL"));
        assertThat(expected.getPreviousFireTime().getTime(), is(previousFireTime));
        assertThat(expected.getNextFireTime().getTime(), is(nextFireTime));
    }
}