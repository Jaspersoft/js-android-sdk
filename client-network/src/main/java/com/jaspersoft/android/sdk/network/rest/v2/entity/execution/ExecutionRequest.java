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

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ExecutionRequest {

    @Expose
    protected final String reportUnitUri;
    @Expose
    protected Boolean async;
    @Expose
    protected Boolean freshData;
    @Expose
    protected Boolean saveDataSnapshot;
    @Expose
    protected String outputFormat;
    @Expose
    protected Boolean interactive;
    @Expose
    protected Boolean ignorePagination;
    @Expose
    protected Boolean allowInlineScripts;
    @Expose
    protected String pages;
    @Expose
    protected String baseUrl;
    @Expose
    protected String anchor;
    @Expose
    protected String transformerKey;
    @Expose
    protected String attachmentsPrefix;
    @Expose
    protected ReportParametersList parameters;

    private ExecutionRequest(String reportUnitUri) {
        this.reportUnitUri = reportUnitUri;
    }

    public static ExecutionRequest newRequest(String uri) {
        return new ExecutionRequest(uri);
    }

    public ExecutionRequest withAsync(Boolean async) {
        this.async = async;
        return this;
    }

    public ExecutionRequest withAttachmentsPrefix(String attachmentsPrefix) {
        this.attachmentsPrefix = attachmentsPrefix;
        return this;
    }

    public ExecutionRequest withFreshData(Boolean freshData) {
        this.freshData = freshData;
        return this;
    }

    public ExecutionRequest withIgnorePagination(Boolean ignorePagination) {
        this.ignorePagination = ignorePagination;
        return this;
    }

    public ExecutionRequest withInteractive(Boolean interactive) {
        this.interactive = interactive;
        return this;
    }

    public ExecutionRequest withOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public ExecutionRequest withPages(String pages) {
        this.pages = pages;
        return this;
    }

    public ExecutionRequest withParameters(List<ReportParameter> parameters) {
        this.parameters = ReportParametersList.wrap(parameters);
        return this;
    }

    public ExecutionRequest withSaveDataSnapshot(Boolean saveDataSnapshot) {
        this.saveDataSnapshot = saveDataSnapshot;
        return this;
    }

    public ExecutionRequest withTransformerKey(String transformerKey) {
        this.transformerKey = transformerKey;
        return this;
    }

    public ExecutionRequest withAnchor(String anchor) {
        this.anchor = anchor;
        return this;
    }

    public ExecutionRequest withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public ExecutionRequest withAllowInlineScripts(Boolean allowInlineScripts) {
        this.allowInlineScripts = allowInlineScripts;
        return this;
    }

    public Boolean getAsync() {
        return async;
    }

    public String getAttachmentsPrefix() {
        return attachmentsPrefix;
    }

    public Boolean getFreshData() {
        return freshData;
    }

    public Boolean getIgnorePagination() {
        return ignorePagination;
    }

    public Boolean getInteractive() {
        return interactive;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public String getPages() {
        return pages;
    }

    public List<ReportParameter> getParameters() {
        return parameters.getReportParameters();
    }

    public String getReportUnitUri() {
        return reportUnitUri;
    }

    public Boolean getSaveDataSnapshot() {
        return saveDataSnapshot;
    }

    public Boolean getAllowInlineScripts() {
        return allowInlineScripts;
    }

    public String getAnchor() {
        return anchor;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getTransformerKey() {
        return transformerKey;
    }

    @Override
    public String toString() {
        return "ExecutionRequest{" +
                "async=" + async +
                ", reportUnitUri='" + reportUnitUri + '\'' +
                ", freshData=" + freshData +
                ", saveDataSnapshot=" + saveDataSnapshot +
                ", outputFormat='" + outputFormat + '\'' +
                ", interactive=" + interactive +
                ", ignorePagination=" + ignorePagination +
                ", pages='" + pages + '\'' +
                ", attachmentsPrefix='" + attachmentsPrefix + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
