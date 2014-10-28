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

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.JsServerProfile;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 1.8
 */

@Root
public class ReportExecutionRequest {

    public static final String DEFAULT_ATTACHMENT_PREFIX = "/reportExecutions/{reportExecutionId}/exports/{exportExecutionId}/attachments/";
    public static final String MARKUP_TYPE_EMBEDDABLE  = "embeddable";
    public static final String MARKUP_TYPE_FULL  = "full";

    @Element
    private String reportUnitUri;

    @Element(required=false)
    private String markupType;

    @Element(required=false)
    private String baseUrl;

    @Element(required=false)
    private boolean async;

    @Element(required=false)
    private boolean freshData;

    @Element(required=false)
    private boolean saveDataSnapshot;

    @Element
    private String outputFormat;

    @Element(required=false)
    private boolean interactive;

    @Element(required=false)
    private boolean ignorePagination;

    @Element(required=false)
    private String pages;

    @Element(required=false)
    private String attachmentsPrefix;

    @ElementList(required=false)
    private List<ReportParameter> parameters;

    public void configureExecutionForProfile(JsRestClient jsRestClient) {
        JsServerProfile jsServerProfile = jsRestClient.getServerProfile();
        String serverUrl = jsServerProfile.getServerUrl();
        setAttachmentsPrefix(serverUrl + DEFAULT_ATTACHMENT_PREFIX);
        setBaseUrl(serverUrl);
    }

    public void setAttachmentsPrefix(String attachmentsPrefix) {
        try {
            this.attachmentsPrefix = URLEncoder.encode(attachmentsPrefix, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            this.attachmentsPrefix = attachmentsPrefix;
        } catch (NullPointerException exception) {
            this.attachmentsPrefix = attachmentsPrefix;
        }
    }

    public String getAttachmentsPrefix() {
        return attachmentsPrefix;
    }

    public String getReportUnitUri() {
        return reportUnitUri;
    }

    public void setReportUnitUri(String reportUnitUri) {
        this.reportUnitUri = reportUnitUri;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isFreshData() {
        return freshData;
    }

    public void setFreshData(boolean freshData) {
        this.freshData = freshData;
    }

    public boolean isSaveDataSnapshot() {
        return saveDataSnapshot;
    }

    public void setSaveDataSnapshot(boolean saveDataSnapshot) {
        this.saveDataSnapshot = saveDataSnapshot;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public boolean isIgnorePagination() {
        return ignorePagination;
    }

    public void setIgnorePagination(boolean ignorePagination) {
        this.ignorePagination = ignorePagination;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public List<ReportParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ReportParameter> parameters) {
        this.parameters = parameters;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getMarkupType() {
        return markupType;
    }

    public void setMarkupType(String markupType) {
        this.markupType = markupType;
    }
}
