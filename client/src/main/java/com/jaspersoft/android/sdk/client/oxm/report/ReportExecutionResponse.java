/*
 * Copyright (C) 2012-2014 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */


package com.jaspersoft.android.sdk.client.oxm.report;

import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 1.8
 */

@Root(name="reportExecution", strict=false)
public class ReportExecutionResponse {

    @Expose
    @Element
    private String requestId;

    @Expose
    @Element
    private String reportURI;

    @Expose
    @Element
    private String status;

    @Expose
    @ElementList(empty=false)
    private List<ExportExecution> exports;

    @Expose
    @Element(required=false)
    private int currentPage;

    @Expose
    @Element(required=false)
    private int totalPages;

    @Expose
    @Element(required=false)
    ErrorDescriptor errorDescriptor;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReportURI() {
        return reportURI;
    }

    public void setReportURI(String reportURI) {
        this.reportURI = reportURI;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ExportExecution> getExports() {
        return exports;
    }

    public void setExports(List<ExportExecution> exports) {
        this.exports = exports;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ReportStatus getReportStatus() {
        return ReportStatus.valueOf(status);
    }

    public ErrorDescriptor getErrorDescriptor() {
        return errorDescriptor;
    }

    public void setErrorDescriptor(ErrorDescriptor errorDescriptor) {
        this.errorDescriptor = errorDescriptor;
    }

}
