/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.network.entity.execution;

import com.google.gson.annotations.Expose;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ExecutionRequestOptions {

    @Expose
    protected Boolean async;
    @Expose
    protected Boolean freshData;
    @Expose
    protected Boolean saveDataSnapshot;
    @Expose
    protected Boolean interactive;
    @Expose
    protected Boolean ignorePagination;
    @Expose
    protected Boolean allowInlineScripts;
    @Expose
    protected String outputFormat;
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
    protected String markupType;
    @Expose
    protected List<ReportParameter> parameters;

    protected ExecutionRequestOptions() {}

    public static ExecutionRequestOptions create() {
        return new ExecutionRequestOptions();
    }

    public ExecutionRequestOptions withAsync(Boolean async) {
        this.async = async;
        return this;
    }

    public ExecutionRequestOptions withFreshData(Boolean freshData) {
        this.freshData = freshData;
        return this;
    }

    public ExecutionRequestOptions withIgnorePagination(Boolean ignorePagination) {
        this.ignorePagination = ignorePagination;
        return this;
    }

    public ExecutionRequestOptions withInteractive(Boolean interactive) {
        this.interactive = interactive;
        return this;
    }

    public ExecutionRequestOptions withSaveDataSnapshot(Boolean saveDataSnapshot) {
        this.saveDataSnapshot = saveDataSnapshot;
        return this;
    }

    public ExecutionRequestOptions withParameters(List<ReportParameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    public ExecutionRequestOptions withAttachmentsPrefix(String attachmentsPrefix) {
        this.attachmentsPrefix = attachmentsPrefix;
        return this;
    }

    public ExecutionRequestOptions withMarkupType(String markupType) {
        this.markupType = markupType;
        return this;
    }

    public ExecutionRequestOptions withOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public ExecutionRequestOptions withPages(String pages) {
        this.pages = pages;
        return this;
    }

    public ExecutionRequestOptions withTransformerKey(String transformerKey) {
        this.transformerKey = transformerKey;
        return this;
    }

    public ExecutionRequestOptions withAnchor(String anchor) {
        this.anchor = anchor;
        return this;
    }

    public ExecutionRequestOptions withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public ExecutionRequestOptions withAllowInlineScripts(Boolean allowInlineScripts) {
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
        return parameters;
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

    public String getMarkupType() {
        return markupType;
    }

    @Override
    public String toString() {
        return "ExecutionRequestOptions{" +
                "allowInlineScripts=" + allowInlineScripts +
                ", async=" + async +
                ", freshData=" + freshData +
                ", saveDataSnapshot=" + saveDataSnapshot +
                ", interactive=" + interactive +
                ", ignorePagination=" + ignorePagination +
                ", outputFormat='" + outputFormat + '\'' +
                ", pages='" + pages + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", anchor='" + anchor + '\'' +
                ", transformerKey='" + transformerKey + '\'' +
                ", attachmentsPrefix='" + attachmentsPrefix + '\'' +
                '}';
    }
}
