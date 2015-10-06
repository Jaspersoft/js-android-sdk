/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.network.entity.execution;

import com.google.gson.annotations.Expose;

import java.util.Map;
import java.util.Set;

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
    protected Map<String, Set<String>> parameters;

    protected ExecutionRequestOptions() {
    }

    public static ExecutionRequestOptions create() {
        return new ExecutionRequestOptions();
    }

    public ExecutionRequestOptions withAsync(boolean async) {
        this.async = async;
        return this;
    }

    public ExecutionRequestOptions withFreshData(boolean freshData) {
        this.freshData = freshData;
        return this;
    }

    public ExecutionRequestOptions withIgnorePagination(boolean ignorePagination) {
        this.ignorePagination = ignorePagination;
        return this;
    }

    public ExecutionRequestOptions withInteractive(boolean interactive) {
        this.interactive = interactive;
        return this;
    }

    public ExecutionRequestOptions withSaveDataSnapshot(boolean saveDataSnapshot) {
        this.saveDataSnapshot = saveDataSnapshot;
        return this;
    }

    public ExecutionRequestOptions withParameters(Map<String, Set<String>> parameters) {
        this.parameters = parameters;
        return this;
    }

    public ExecutionRequestOptions withAttachmentsPrefix(String attachmentsPrefix) {
        this.attachmentsPrefix = attachmentsPrefix;
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

    public Map<String, Set<String>> getParameters() {
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
