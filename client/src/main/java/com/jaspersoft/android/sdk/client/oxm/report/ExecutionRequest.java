package com.jaspersoft.android.sdk.client.oxm.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ExecutionRequest {
    public static final String DEFAULT_ATTACHMENT_PREFIX = "/reportExecutions/{reportExecutionId}/exports/{exportExecutionId}/attachments/";
    public static final String MARKUP_TYPE_EMBEDDABLE  = "embeddable";
    public static final String MARKUP_TYPE_FULL  = "full";

    @Expose
    @Element(required=false)
    protected String reportUnitUri;

    @Expose
    @Element(required=false)
    protected String markupType;

    @Element(required=false)
    protected String baseUrl;

    @Expose
    @Element(required=false)
    protected Boolean async;

    @Expose
    @Element(required=false)
    protected Boolean freshData;

    @Expose
    @Element(required=false)
    protected Boolean saveDataSnapshot;

    @Expose
    @Element
    protected String outputFormat;

    @Expose
    @Element(required=false)
    protected Boolean interactive;

    @Expose
    @Element(required=false)
    protected Boolean ignorePagination;

    @Expose
    @Element(required=false)
    protected Boolean allowInlineScripts;

    @Expose
    @Element(required=false)
    protected String pages;

    @Expose
    @Element(required=false)
    protected String attachmentsPrefix;

    @ElementList(required=false)
    protected List<ReportParameter> parameters = new ArrayList<ReportParameter>();

    @Expose
    @SerializedName("parameters")
    protected ReportParametersList reportParameters;

    public void setAttachmentsPrefix(String attachmentsPrefix) {
        this.attachmentsPrefix = attachmentsPrefix;
    }

    public void setEscapedAttachmentsPrefix(String attachmentsPrefix) {
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
        ReportParametersList reportParametersList = new ReportParametersList();
        reportParametersList.setReportParameters(parameters);
        this.reportParameters = reportParametersList;
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

    public Boolean getAllowInlineScripts() {
        return allowInlineScripts;
    }

    public void setAllowInlineScripts(Boolean allowInlineScripts) {
        this.allowInlineScripts = allowInlineScripts;
    }

}
