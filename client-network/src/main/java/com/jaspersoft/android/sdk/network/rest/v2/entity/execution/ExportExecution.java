/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network.rest.v2.entity.execution;

import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ExportExecution {
    @Expose
    private String id;
    @Expose
    private String status;
    @Expose
    private ExecutionRequestData options;
    @Expose
    private ReportOutputResource outputResource;
    @Expose
    private Set<ReportOutputResource> attachments = Collections.emptySet();
    @Expose
    private ErrorDescriptor errorDescriptor;

    public Set<ReportOutputResource> getAttachments() {
        return attachments;
    }

    public ErrorDescriptor getErrorDescriptor() {
        return errorDescriptor;
    }

    public String getId() {
        return id;
    }

    public ExecutionRequestData getOptions() {
        return options;
    }

    public ReportOutputResource getOutputResource() {
        return outputResource;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExportExecution that = (ExportExecution) o;

        if (!id.equals(that.id)) return false;
        if (!status.equals(that.status)) return false;
        if (options != null ? !options.equals(that.options) : that.options != null) return false;
        if (outputResource != null ? !outputResource.equals(that.outputResource) : that.outputResource != null)
            return false;
        if (attachments != null ? !attachments.equals(that.attachments) : that.attachments != null)
            return false;
        return !(errorDescriptor != null ? !errorDescriptor.equals(that.errorDescriptor) : that.errorDescriptor != null);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + (options != null ? options.hashCode() : 0);
        result = 31 * result + (outputResource != null ? outputResource.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        result = 31 * result + (errorDescriptor != null ? errorDescriptor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExportExecution{" +
                "attachments=" + Arrays.toString(attachments.toArray()) +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", options=" + options +
                ", outputResource=" + outputResource +
                ", errorDescriptor=" + errorDescriptor +
                '}';
    }
}
