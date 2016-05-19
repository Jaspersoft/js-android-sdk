package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobSearchTaskFactory {
    private final ReportScheduleUseCase mUseCase;
    private final JobSearchCriteria mCriteria;

    public JobSearchTaskFactory(ReportScheduleUseCase useCase, JobSearchCriteria criteria) {
        mUseCase = useCase;
        mCriteria = criteria;
    }

    public JobSearchTask create(ServerVersion serverVersion) {
        String query = mCriteria.getLabel();
        boolean noQuery = (query == null || query.length() == 0);

        if (noQuery) {
            return new JobSearch62Task(mUseCase, mCriteria);
        }
        if (serverVersion.greaterThanOrEquals(ServerVersion.v6_2)) {
            return new JobSearch62Task(mUseCase, mCriteria);
        }
        return new LegacyJobSearchTask(mUseCase, mCriteria);
    }
}
