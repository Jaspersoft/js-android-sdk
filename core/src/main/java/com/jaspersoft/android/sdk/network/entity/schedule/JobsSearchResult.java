package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobsSearchResult {
    @Expose
    private List<JobUnitEntity> jobsummary = Collections.emptyList();

    public List<JobUnitEntity> getJobSummary() {
        return Collections.unmodifiableList(jobsummary);
    }
}
