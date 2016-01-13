package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobSearchResult {
    @Expose
    private List<JobUnit> mJobsummary = Collections.emptyList();

    public List<JobUnit> getJobSummary() {
        return Collections.unmodifiableList(mJobsummary);
    }
}
