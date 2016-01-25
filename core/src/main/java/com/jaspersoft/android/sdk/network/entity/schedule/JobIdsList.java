package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobIdsList {
    @Expose
    private Set<Integer> jobId;

    public Set<Integer> getJobId() {
        return jobId;
    }
}
