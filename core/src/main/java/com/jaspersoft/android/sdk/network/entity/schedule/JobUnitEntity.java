/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobUnitEntity {
    @Expose
    private int id;
    @Expose
    private int version;
    @Expose
    private String reportUnitURI;
    @Expose
    private String reportLabel;
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

    public String getReportLabel() {
        return reportLabel;
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
        if (reportLabel != null ? !reportLabel.equals(that.reportLabel) : that.reportLabel != null) return false;
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
        result = 31 * result + (reportLabel != null ? reportLabel.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
