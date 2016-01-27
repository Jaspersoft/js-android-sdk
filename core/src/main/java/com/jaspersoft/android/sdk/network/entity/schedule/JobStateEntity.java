package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobStateEntity {
    @Expose
    private String nextFireTime;
    @Expose
    private String previousFireTime;
    @Expose
    private String value;

    public String getNextFireTime() {
        return nextFireTime;
    }

    public String getPreviousFireTime() {
        return previousFireTime;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobStateEntity jobState = (JobStateEntity) o;

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
