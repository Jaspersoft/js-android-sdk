package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobOwner;
import com.jaspersoft.android.sdk.service.data.schedule.JobState;
import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class SearchTaskSpec {

    private final ReportScheduleUseCase useCase;
    private final TaskProvider taskProvider;

    private JobSearchCriteria criteria;
    private JobSearchTask taskUnderTest;
    private List<JobUnit> searchResult;

    private SearchTaskSpec(Builder builder) {
        this.useCase = builder.useCase;
        this.taskProvider = builder.taskProvider;
    }

    public void givenSearchTaskByCriteria(JobSearchCriteria specCriteria) {
        criteria = specCriteria;
        taskUnderTest = taskProvider.provideTask(useCase, criteria);
    }

    public void givenEmptySearchResponse() throws Exception {
        givenSearchResponse(responseConfiguration(0));
    }

    public void givenSearchResponse(SearchResponseConfig config) throws Exception {
        List<JobUnit> list = new ArrayList<>();
        for (int i = 0; i < config.numberOfItems; i++) {
            JobUnit jobUnit = createMockJobUnit(config);
            list.add(jobUnit);
        }
        when(useCase.searchJob(any(JobSearchCriteria.class))).thenReturn(list);
    }

    private JobUnit createMockJobUnit(SearchResponseConfig config) {
        JobOwner jobOwner = JobOwner.newOwner("superuser", "superuser");
        JobUnit jobUnit = Mockito.mock(JobUnit.class);
        when(jobUnit.getLabel()).thenReturn(config.label);
        when(jobUnit.getReportUri()).thenReturn("/report/uri");
        when(jobUnit.getOwner()).thenReturn(jobOwner);
        when(jobUnit.getState()).thenReturn(JobState.NORMAL);
        return jobUnit;
    }

    public void whenNextLookupRequested() throws ServiceException {
        searchResult = taskUnderTest.nextLookup();
    }

    public void thenShouldBeNoSearchResults() {
        assertThat(searchResult.size(), is(0));
    }


    public void thenShouldBeExactNumberOfItemsInSearchResponse(int number) {
        assertThat(searchResult.size(), is(number));
    }

    public void thenShouldNotPerformNetworkRequest() {
        verifyNoMoreInteractions(useCase);
    }

    public void thenShouldRequestAllItems() throws ServiceException {
        verify(useCase).searchJob(
                JobSearchCriteria.builder()
                        .withLimit(JobSearchCriteria.UNLIMITED_ROW_NUMBER)
                        .withOffset(0)
                        .build()
        );
    }

    public void thenShouldPerformNetworkRequestWitOffset(int offset) throws ServiceException {
        verify(useCase).searchJob(
                criteria.newBuilder()
                        .withOffset(offset)
                        .build()
        );
    }

    public static SearchTaskSpec.SearchResponseConfig responseConfiguration(int numberOfItems) {
        return new SearchTaskSpec.SearchResponseConfig(numberOfItems);
    }

    public static class Builder {
        private ReportScheduleUseCase useCase;
        private TaskProvider taskProvider;

        public Builder useCase(ReportScheduleUseCase useCase) {
            this.useCase = useCase;
            return this;
        }

        public Builder taskProvider(TaskProvider taskProvider) {
            this.taskProvider = taskProvider;
            return this;
        }

        public SearchTaskSpec build() {
            Preconditions.checkNotNull(useCase, "useCase == null");
            Preconditions.checkNotNull(taskProvider, "taskProvider == null");
            return new SearchTaskSpec(this);
        }
    }

    public interface TaskProvider {
        JobSearchTask provideTask(ReportScheduleUseCase useCase, JobSearchCriteria criteria);
    }

    public static class SearchResponseConfig {
        private final int numberOfItems;
        private String label;

        private SearchResponseConfig(int numberOfItems) {
            this.numberOfItems = numberOfItems;
            label = "label";
        }

        public SearchResponseConfig withLabel(String label) {
            this.label = Preconditions.checkNotNull(label, "label == null");
            return this;
        }
    }
}
