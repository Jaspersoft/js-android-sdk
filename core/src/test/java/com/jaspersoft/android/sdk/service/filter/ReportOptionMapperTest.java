package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionEntity;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportOptionMapperTest {

    @Mock
    ReportOptionEntity mEntity;

    private ReportOptionMapper mReportOptionMapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mReportOptionMapper = new ReportOptionMapper();
        when(mEntity.getId()).thenReturn("id");
        when(mEntity.getUri()).thenReturn("/my/uri");
        when(mEntity.getLabel()).thenReturn("label");
    }

    @Test
    public void should_map_entities_to_data_objects() throws Exception {
        Set<ReportOption> expected = mReportOptionMapper.transform(Collections.singleton(mEntity));
        assertThat(expected, is(not(empty())));
    }

    @Test
    public void should_map_entity_to_data_object() throws Exception {
        ReportOption expected = mReportOptionMapper.transform(mEntity);
        assertThat(expected.getId(), is("id"));
        assertThat(expected.getUri(), is("/my/uri"));
        assertThat(expected.getLabel(), is("label"));
    }
}