package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobUnit {
    @Expose
    private int id;
    @Expose
    private String version;
    @Expose
    private String reportUnitURI;
    @Expose
    private String label;
    @Expose
    private String owner;
    @Expose
    private JobState state;

    public int getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getReportUnitURI() {
        return reportUnitURI;
    }

    public String getLabel() {
        return label;
    }

    public String getOwner() {
        return owner;
    }

    public JobState getState() {
        return state;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobUnit)) return false;

        JobUnit jobUnit = (JobUnit) o;

        if (id != jobUnit.id) return false;
        if (label != null ? !label.equals(jobUnit.label) : jobUnit.label != null) return false;
        if (owner != null ? !owner.equals(jobUnit.owner) : jobUnit.owner != null) return false;
        if (reportUnitURI != null ? !reportUnitURI.equals(jobUnit.reportUnitURI) : jobUnit.reportUnitURI != null)
            return false;
        if (state != null ? !state.equals(jobUnit.state) : jobUnit.state != null) return false;
        if (version != null ? !version.equals(jobUnit.version) : jobUnit.version != null) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = id;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (reportUnitURI != null ? reportUnitURI.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
