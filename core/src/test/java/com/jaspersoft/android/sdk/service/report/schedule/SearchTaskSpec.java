package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;

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
        givenSearchResponse(0);
    }

    public void givenSearchResponse(int number) throws Exception {
        List<JobUnit> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add(null);
        }
        when(useCase.searchJob(any(JobSearchCriteria.class))).thenReturn(list);
    }

    public void whenNextLookupRequested() throws ServiceException {
        searchResult = taskUnderTest.nextLookup();
    }

    public void thenShouldBeNoSearchResults() {
        assertThat(searchResult.size(), is(0));
    }

    public void thenShouldNotPerformNetworkRequest() {
        verifyNoMoreInteractions(useCase);
    }

    public void thenShouldPerformNetworkRequestWitOffset(int offset) throws ServiceException {
        verify(useCase).searchJob(
                criteria.newBuilder()
                        .withOffset(offset)
                        .build()
        );
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
}
