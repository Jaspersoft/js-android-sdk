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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ExecutionRequestOptions {

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
    protected final String reportUnitUri;
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
    protected ReportParameters parameters = ReportParameters.empty();

    private ExecutionRequestOptions(String reportUnitUri) {
        this.reportUnitUri = reportUnitUri;
    }

    public static ExecutionRequestOptions newRequest(String uri) {
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("Uri should not be null");
        }
        return new ExecutionRequestOptions(uri);
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

    public ExecutionRequestOptions withParameters(Set<ReportParameter> parameters) {
        this.parameters = ReportParameters.wrap(parameters);
        return this;
    }

    public ExecutionRequestOptions withAttachmentsPrefix(String attachmentsPrefix) {
        if (attachmentsPrefix == null || attachmentsPrefix.length() == 0) {
            throw new IllegalArgumentException("Attachment prefix should not be null or empty");
        }
        try {
            this.attachmentsPrefix = URLEncoder.encode(attachmentsPrefix, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("This should not be possible", e);
        }
        return this;
    }

    public ExecutionRequestOptions withOutputFormat(String outputFormat) {
        if (outputFormat == null || outputFormat.length() == 0) {
            throw new IllegalArgumentException("Output format should not be null or empty");
        }
        this.outputFormat = outputFormat;
        return this;
    }

    public ExecutionRequestOptions withPages(String pages) {
        if (pages == null || pages.length() == 0) {
            throw new IllegalArgumentException("Pages should not be null or empty");
        }
        this.pages = pages;
        return this;
    }

    public ExecutionRequestOptions withTransformerKey(String transformerKey) {
        if (transformerKey == null || transformerKey.length() == 0) {
            throw new IllegalArgumentException("Transform key should not be null or empty");
        }
        this.transformerKey = transformerKey;
        return this;
    }

    public ExecutionRequestOptions withAnchor(String anchor) {
        if (anchor == null || anchor.length() == 0) {
            throw new IllegalArgumentException("Anchor should not be null or empty");
        }
        this.anchor = anchor;
        return this;
    }

    public ExecutionRequestOptions withBaseUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.length() == 0) {
            throw new IllegalArgumentException("Base url should not be null or empty");
        }
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

    public Set<ReportParameter> getParameters() {
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
        return "ExecutionRequestOptions{" +
                "allowInlineScripts=" + allowInlineScripts +
                ", async=" + async +
                ", freshData=" + freshData +
                ", saveDataSnapshot=" + saveDataSnapshot +
                ", interactive=" + interactive +
                ", ignorePagination=" + ignorePagination +
                ", reportUnitUri='" + reportUnitUri + '\'' +
                ", outputFormat='" + outputFormat + '\'' +
                ", pages='" + pages + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", anchor='" + anchor + '\'' +
                ", transformerKey='" + transformerKey + '\'' +
                ", attachmentsPrefix='" + attachmentsPrefix + '\'' +
                ", parameters=" + Arrays.toString(parameters.getReportParameters().toArray()) +
                '}';
    }
}
