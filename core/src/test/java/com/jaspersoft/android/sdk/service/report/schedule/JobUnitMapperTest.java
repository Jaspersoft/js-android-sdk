package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobStateEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnitEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobUnitMapperTest {
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
        JobUnit expected = mJobUnitMapper.transform(mJobUnitEntity);

        assertThat(expected.getId(), is(1));
        assertThat(expected.getVersion(), is(100));
        assertThat(expected.getLabel(), is("label"));
        assertThat(expected.getDescription(), is("description"));
        assertThat(expected.getReportUri(), is("/my/uri"));
        assertThat(expected.getReportLabel(), is("report label"));
        assertThat(expected.getOwner().toString(), is("jasperadmin|organization_1"));
        assertThat(expected.getState().toString(), is("NORMAL"));
    }
}