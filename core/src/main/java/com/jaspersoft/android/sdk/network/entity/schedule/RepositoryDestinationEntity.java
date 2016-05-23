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
public class RepositoryDestinationEntity {

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
    private Boolean usingDefaultReportOutputFolderURI;
    @Expose
    private String outputLocalFolder;
    @Expose
    private OutputFtpInfoEntity outputFTPInfo;

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

    public Boolean getUsingDefaultReportOutputFolderURI() {
        return usingDefaultReportOutputFolderURI;
    }

    public void setUsingDefaultReportOutputFolderURI(Boolean usingDefaultReportOutputFolderURI) {
        this.usingDefaultReportOutputFolderURI = usingDefaultReportOutputFolderURI;
    }

    public String getOutputLocalFolder() {
        return outputLocalFolder;
    }

    public void setOutputLocalFolder(String outputLocalFolder) {
        this.outputLocalFolder = outputLocalFolder;
    }

    public OutputFtpInfoEntity getOutputFTPInfo() {
        return outputFTPInfo;
    }

    public void setOutputFTPInfo(OutputFtpInfoEntity outputFTPInfo) {
        this.outputFTPInfo = outputFTPInfo;
    }
}
