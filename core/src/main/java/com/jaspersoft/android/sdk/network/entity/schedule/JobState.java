package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobState {
    @Expose
    private String nextFireTime;
    @Expose
    private String value;

    public String getNextFileTime() {
        return nextFireTime;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobState jobState = (JobState) o;

        if (nextFireTime != null ? !nextFireTime.equals(jobState.nextFireTime) : jobState.nextFireTime != null)
            return false;
        if (value != null ? !value.equals(jobState.value) : jobState.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nextFireTime != null ? nextFireTime.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
