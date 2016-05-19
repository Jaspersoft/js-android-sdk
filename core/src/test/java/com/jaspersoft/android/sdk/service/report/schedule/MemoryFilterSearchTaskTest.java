package com.jaspersoft.android.sdk.service.report.schedule;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.jaspersoft.android.sdk.service.report.schedule.SearchTaskSpec.responseConfiguration;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.MockitoAnnotations.initMocks;

public class MemoryFilterSearchTaskTest {
    public static final int ROW_NUMBER = 5;
    private static final String LABEL = "query";

    @Mock
    ReportScheduleUseCase mUseCase;

    private SearchTaskSpec spec;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        spec = new SearchTaskSpec.Builder()
                .useCase(mUseCase)
                .taskProvider(new SearchTaskSpec.TaskProvider() {
                    @Override
                    public JobSearchTask provideTask(ReportScheduleUseCase useCase, JobSearchCriteria criteria) {
                        return new MemoryFilterSearchTask(useCase, criteria);
                    }
                }).build();
    }

    @Test
    public void should_increment_offset_each_time_if_search_not_exhausted() throws Exception {
        spec.givenSearchResponse(
                responseConfiguration(ROW_NUMBER * 3).withLabel(LABEL)
        );
        spec.givenSearchTaskByCriteria(
                JobSearchCriteria.builder()
                        .withLabel(LABEL)
                        .withOffset(ROW_NUMBER)
                        .withLimit(ROW_NUMBER)
                        .build()
        );

        spec.whenNextLookupRequested();
        spec.thenShouldRequestAllItems();
        spec.thenShouldBeExactNumberOfItemsInSearchResponse(5);

        spec.whenNextLookupRequested();
        spec.thenShouldNotPerformNetworkRequest();
        spec.thenShouldBeExactNumberOfItemsInSearchResponse(5);

        spec.whenNextLookupRequested();
        spec.thenShouldNotPerformNetworkRequest();
        spec.thenShouldBeNoSearchResults();
    }

    @Test
    public void should_return_last_cached_values_if_search_exhausted() throws Exception {
        spec.givenSearchResponse(responseConfiguration(ROW_NUMBER - 1));
        spec.givenSearchTaskByCriteria(
                JobSearchCriteria.builder()
                        .withLabel(LABEL)
                        .withOffset(0)
                        .withLimit(JobSearchCriteria.UNLIMITED_ROW_NUMBER)
                        .build()
        );

        spec.whenNextLookupRequested();
        spec.thenShouldRequestAllItems();

        spec.whenNextLookupRequested();
        spec.thenShouldNotPerformNetworkRequest();
    }

    @Test
    public void should_return_empty_result_if_first_run_exhausted() throws Exception {
        spec.givenEmptySearchResponse();
        spec.givenSearchTaskByCriteria(
                JobSearchCriteria.builder()
                        .withOffset(0)
                        .withLimit(ROW_NUMBER)
                        .withLabel(LABEL)
                        .build()
        );

        spec.whenNextLookupRequested();
        spec.thenShouldRequestAllItems();
        spec.thenShouldBeNoSearchResults();

        spec.whenNextLookupRequested();
        spec.thenShouldNotPerformNetworkRequest();
    }

    @Test
    public void should_have_next_by_default_true() throws Exception {
        RestFilterSearchTask searchTask = new RestFilterSearchTask(mUseCase, JobSearchCriteria.empty());
        assertThat("Should have next values by default", searchTask.hasNext(), is(true));
    }
}