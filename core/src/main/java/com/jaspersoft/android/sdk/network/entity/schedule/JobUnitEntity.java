package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobUnitEntity {
    @Expose
    private int id;
    @Expose
    private int version;
    @Expose
    private String reportUnitURI;
    @Expose
    private String label;
    @Expose
    private String description;
    @Expose
    private String owner;
    @Expose
    private JobStateEntity state;

    public int getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getReportUnitURI() {
        return reportUnitURI;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public JobStateEntity getState() {
        return state;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobUnitEntity)) return false;

        JobUnitEntity that = (JobUnitEntity) o;

        if (id != that.id) return false;
        if (version != that.version) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        if (reportUnitURI != null ? !reportUnitURI.equals(that.reportUnitURI) : that.reportUnitURI != null)
            return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = id;
        result = 31 * result + version;
        result = 31 * result + (reportUnitURI != null ? reportUnitURI.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
