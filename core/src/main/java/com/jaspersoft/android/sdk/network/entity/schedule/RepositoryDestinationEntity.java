package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class RepositoryDestinationEntity {

    @Expose
    private String folderURI;
    @Expose
    private Boolean sequentialFilenames;
    @Expose
    private String timestampPattern;
    @Expose
    private Boolean overwriteFiles;
    @Expose
    private String outputDescription;
    @Expose
    private Boolean saveToRepository;
    @Expose
    private String defaultReportOutputFolderURI;
    @Expose
    private String usingDefaultReportOutputFolderURI;
    @Expose
    private String outputLocalFolder;


    public String getFolderURI() {
        return folderURI;
    }

    public void setFolderURI(String folderURI) {
        this.folderURI = folderURI;
    }

    public Boolean getSequentialFilenames() {
        return sequentialFilenames;
    }

    public void setSequentialFilenames(Boolean sequentialFilenames) {
        this.sequentialFilenames = sequentialFilenames;
    }

    public String getTimestampPattern() {
        return timestampPattern;
    }

    public void setTimestampPattern(String timestampPattern) {
        this.timestampPattern = timestampPattern;
    }

    public Boolean getOverwriteFiles() {
        return overwriteFiles;
    }

    public void setOverwriteFiles(Boolean overwriteFiles) {
        this.overwriteFiles = overwriteFiles;
    }

    public String getOutputDescription() {
        return outputDescription;
    }

    public void setOutputDescription(String outputDescription) {
        this.outputDescription = outputDescription;
    }

    public Boolean getSaveToRepository() {
        return saveToRepository;
    }

    public void setSaveToRepository(Boolean saveToRepository) {
        this.saveToRepository = saveToRepository;
    }

    public String getDefaultReportOutputFolderURI() {
        return defaultReportOutputFolderURI;
    }

    public void setDefaultReportOutputFolderURI(String defaultReportOutputFolderURI) {
        this.defaultReportOutputFolderURI = defaultReportOutputFolderURI;
    }

    public String getUsingDefaultReportOutputFolderURI() {
        return usingDefaultReportOutputFolderURI;
    }

    public void setUsingDefaultReportOutputFolderURI(String usingDefaultReportOutputFolderURI) {
        this.usingDefaultReportOutputFolderURI = usingDefaultReportOutputFolderURI;
    }

    public String getOutputLocalFolder() {
        return outputLocalFolder;
    }

    public void setOutputLocalFolder(String outputLocalFolder) {
        this.outputLocalFolder = outputLocalFolder;
    }
}
