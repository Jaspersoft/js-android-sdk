package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BaseJobUnitSearchTaskTest {
    private static final JobSearchCriteria CRITERIA = JobSearchCriteria.empty();
    public static final int ROW_NUMBER = 5;

    @Mock
    ReportScheduleUseCase mUseCase;

    private BaseJobSearchTask searchTask;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void should_increment_offset_each_time_if_search_not_exhausted() throws Exception {
        mockConstantSearchResponse();

        JobSearchCriteria initialCriteria = JobSearchCriteria.builder()
                .withOffset(ROW_NUMBER)
                .withLimit(ROW_NUMBER)
                .build();

        BaseJobSearchTask searchTask = new BaseJobSearchTask(mUseCase, initialCriteria);
        searchTask.nextLookup();
        searchTask.nextLookup();

        verify(mUseCase).searchJob(initialCriteria);
        verify(mUseCase).searchJob(
                initialCriteria.newBuilder()
                        .withOffset(ROW_NUMBER * 2)
                        .build()
        );
    }

    @Test
    public void should_return_last_cached_values_if_search_exhausted() throws Exception {
        mockExhaustedSearchResponse();

        JobSearchCriteria initialCriteria = JobSearchCriteria.builder()
                .withLimit(JobSearchCriteria.UNLIMITED_ROW_NUMBER)
                .build();
        BaseJobSearchTask searchTask = new BaseJobSearchTask(mUseCase, initialCriteria);
        searchTask.nextLookup();
        searchTask.nextLookup();

        verify(mUseCase).searchJob(initialCriteria);
        verifyNoMoreInteractions(mUseCase);
    }

    @Test
    public void should_return_empty_result_if_first_run_exhausted() throws Exception {
        mockEmptySearchResponse();

        JobSearchCriteria initialCriteria = JobSearchCriteria.empty();
        BaseJobSearchTask searchTask = new BaseJobSearchTask(mUseCase, initialCriteria);

        List<JobUnit> result = searchTask.nextLookup();
        assertThat(result.size(), is(0));

        searchTask.nextLookup();

        verify(mUseCase).searchJob(initialCriteria);
        verifyNoMoreInteractions(mUseCase);
    }

    @Test
    public void should_have_next_by_default_true() throws Exception {
        BaseJobSearchTask searchTask = new BaseJobSearchTask(mUseCase, JobSearchCriteria.empty());
        assertThat("Should have next values by default", searchTask.hasNext(), is(true));
    }

    private void mockConstantSearchResponse() throws Exception {
        mockSearchResponse(ROW_NUMBER);
    }

    private void mockEmptySearchResponse() throws Exception {
        mockSearchResponse(0);
    }

    private void mockExhaustedSearchResponse() throws Exception {
        mockSearchResponse(ROW_NUMBER - 1);
    }

    private void mockSearchResponse(int number) throws Exception {
        List<JobUnit> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add(null);
        }
        when(mUseCase.searchJob(any(JobSearchCriteria.class))).thenReturn(list);
    }
}