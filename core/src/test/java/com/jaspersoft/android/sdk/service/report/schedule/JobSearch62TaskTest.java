package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
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

public class JobSearch62TaskTest {
    public static final int ROW_NUMBER = 5;

    @Mock
    ReportScheduleUseCase mUseCase;

    private JobSearchTask taskUnderTest;
    private JobSearchCriteria criteria;
    private List<JobUnit> searchResult;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void should_increment_offset_each_time_if_search_not_exhausted() throws Exception {
        givenSearchResponseWithPredefined(ROW_NUMBER);
        givenSearchTaskWithOffsetAndLimit(ROW_NUMBER, ROW_NUMBER);

        whenNextLookupRequested();
        thenShouldPerformNetworkRequestWitOffset(ROW_NUMBER);

        whenNextLookupRequested();
        thenShouldPerformNetworkRequestWitOffset(ROW_NUMBER + ROW_NUMBER);
    }

    @Test
    public void should_return_last_cached_values_if_search_exhausted() throws Exception {
        givenExhaustedSearchResponse();
        givenSearchTaskWithNoLimits();

        whenNextLookupRequested();
        thenShouldPerformNetworkRequestWitOffset(0);

        whenNextLookupRequested();
        thenShouldNotPerformNetworkRequest();
    }

    @Test
    public void should_return_empty_result_if_first_run_exhausted() throws Exception {
        givenEmptySearchResponse();
        givenSearchTaskWithNoConfigurations();

        whenNextLookupRequested();
        thenShouldPerformNetworkRequestWitOffset(0);
        thenShouldBeNoSearchResults();

        whenNextLookupRequested();
        thenShouldNotPerformNetworkRequest();
    }

    @Test
    public void should_have_next_by_default_true() throws Exception {
        JobSearch62Task searchTask = new JobSearch62Task(mUseCase, JobSearchCriteria.empty());
        assertThat("Should have next values by default", searchTask.hasNext(), is(true));
    }

    private void givenSearchTaskWithNoConfigurations() {
        criteria = JobSearchCriteria.empty();
        taskUnderTest = new JobSearch62Task(mUseCase, criteria);
    }

    private void givenSearchTaskWithOffsetAndLimit(int offset, int limit) {
        criteria = JobSearchCriteria.builder()
                .withOffset(offset)
                .withLimit(limit)
                .build();
        taskUnderTest = new JobSearch62Task(mUseCase, criteria);
    }

    private void givenSearchTaskWithNoLimits() {
        criteria = JobSearchCriteria.builder()
                .withLimit(JobSearchCriteria.UNLIMITED_ROW_NUMBER)
                .build();
        taskUnderTest = new JobSearch62Task(mUseCase, criteria);
    }

    private void givenEmptySearchResponse() throws Exception {
        givenSearchResponseWithPredefined(0);
    }

    private void givenExhaustedSearchResponse() throws Exception {
        givenSearchResponseWithPredefined(ROW_NUMBER - 1);
    }

    private void givenSearchResponseWithPredefined(int number) throws Exception {
        List<JobUnit> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add(null);
        }
        when(mUseCase.searchJob(any(JobSearchCriteria.class))).thenReturn(list);
    }

    private void whenNextLookupRequested() throws ServiceException {
        searchResult = taskUnderTest.nextLookup();
    }

    private void thenShouldBeNoSearchResults() {
        assertThat(searchResult.size(), is(0));
    }

    private void thenShouldNotPerformNetworkRequest() {
        verifyNoMoreInteractions(mUseCase);
    }

    private void thenShouldPerformNetworkRequestWitOffset(int offset) throws ServiceException {
        verify(mUseCase).searchJob(
                criteria.newBuilder()
                        .withOffset(offset)
                        .build()
        );
    }
}