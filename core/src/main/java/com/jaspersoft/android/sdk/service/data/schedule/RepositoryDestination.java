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

package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class RepositoryDestination {
    private String folderUri;
    private Boolean sequentialFileNames;
    private Boolean overwriteFiles;
    private Boolean saveToRepository;
    private Boolean useDefaultReportOutputFolderURI;
    private String timestampPattern;
    private String defaultReportOutputFolderURI;
    private String outputDescription;
    private String outputLocalFolder;
    private JobOutputFtpInfo jobOutputFtpInfo;

    RepositoryDestination(Builder builder) {
        folderUri = builder.folderUri;
        sequentialFileNames = builder.sequentialFileNames;
        overwriteFiles = builder.overwriteFiles;
        saveToRepository = builder.saveToRepository;
        useDefaultReportOutputFolderURI = builder.useDefaultReportOutputFolderURI;
        timestampPattern = builder.timestampPattern;
        outputDescription = builder.outputDescription;
        outputLocalFolder = builder.outputLocalFolder;
        defaultReportOutputFolderURI = builder.defaultReportOutputFolderURI;
        jobOutputFtpInfo = builder.jobOutputFtpInfo;
    }

    @Nullable
    public Boolean getSequentialFileNames() {
        return sequentialFileNames;
    }

    @Nullable
    public Boolean getOverwriteFiles() {
        return overwriteFiles;
    }

    @Nullable
    public Boolean getSaveToRepository() {
        return saveToRepository;
    }

    @Nullable
    public Boolean getUseDefaultReportOutputFolderURI() {
        return useDefaultReportOutputFolderURI;
    }

    @Nullable
    public String getTimestampPattern() {
        return timestampPattern;
    }

    @Nullable
    public String getOutputDescription() {
        return outputDescription;
    }

    @Nullable
    public String getOutputLocalFolder() {
        return outputLocalFolder;
    }

    @NotNull
    public String getFolderUri() {
        return folderUri;
    }

    @Nullable
    public String getDefaultReportOutputFolderURI() {
        return defaultReportOutputFolderURI;
    }

    @Nullable
    public JobOutputFtpInfo getOutputFtpInfo() {
        return jobOutputFtpInfo;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryDestination that = (RepositoryDestination) o;

        if (folderUri != null ? !folderUri.equals(that.folderUri) : that.folderUri != null) return false;
        if (sequentialFileNames != null ? !sequentialFileNames.equals(that.sequentialFileNames) : that.sequentialFileNames != null)
            return false;
        if (overwriteFiles != null ? !overwriteFiles.equals(that.overwriteFiles) : that.overwriteFiles != null)
            return false;
        if (saveToRepository != null ? !saveToRepository.equals(that.saveToRepository) : that.saveToRepository != null)
            return false;
        if (useDefaultReportOutputFolderURI != null ? !useDefaultReportOutputFolderURI.equals(that.useDefaultReportOutputFolderURI) : that.useDefaultReportOutputFolderURI != null)
            return false;
        if (timestampPattern != null ? !timestampPattern.equals(that.timestampPattern) : that.timestampPattern != null)
            return false;
        if (outputDescription != null ? !outputDescription.equals(that.outputDescription) : that.outputDescription != null)
            return false;
        return !(outputLocalFolder != null ? !outputLocalFolder.equals(that.outputLocalFolder) : that.outputLocalFolder != null);
    }

    @Override
    public int hashCode() {
        int result = folderUri != null ? folderUri.hashCode() : 0;
        result = 31 * result + (sequentialFileNames != null ? sequentialFileNames.hashCode() : 0);
        result = 31 * result + (overwriteFiles != null ? overwriteFiles.hashCode() : 0);
        result = 31 * result + (saveToRepository != null ? saveToRepository.hashCode() : 0);
        result = 31 * result + (useDefaultReportOutputFolderURI != null ? useDefaultReportOutputFolderURI.hashCode() : 0);
        result = 31 * result + (timestampPattern != null ? timestampPattern.hashCode() : 0);
        result = 31 * result + (outputDescription != null ? outputDescription.hashCode() : 0);
        result = 31 * result + (outputLocalFolder != null ? outputLocalFolder.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private String folderUri;
        private Boolean sequentialFileNames;
        private Boolean overwriteFiles;
        private Boolean saveToRepository;
        private Boolean useDefaultReportOutputFolderURI;
        private String timestampPattern;
        private String outputDescription;
        private String outputLocalFolder;
        private String defaultReportOutputFolderURI;
        private JobOutputFtpInfo jobOutputFtpInfo;

        public Builder() {
        }

        Builder(RepositoryDestination destination) {
            folderUri = destination.folderUri;
            sequentialFileNames = destination.sequentialFileNames;
            overwriteFiles = destination.overwriteFiles;
            saveToRepository = destination.saveToRepository;
            useDefaultReportOutputFolderURI = destination.useDefaultReportOutputFolderURI;
            timestampPattern = destination.timestampPattern;
            outputDescription = destination.outputDescription;
            outputLocalFolder = destination.outputLocalFolder;
            defaultReportOutputFolderURI = destination.defaultReportOutputFolderURI;
            jobOutputFtpInfo = destination.jobOutputFtpInfo;
        }

        /**
         * Allows to specify location where generated resource will reside
         *
         * @param folderUri unique identifier of folder on JRS side
         * @return builder for convenient configuration
         */
        public Builder withFolderUri(@NotNull String folderUri) {
            this.folderUri = Preconditions.checkNotNull(folderUri, "Repository folder uri should not be null");
            return this;
        }

        /**
         * Decides if a timestamp is to be added to the names of the job output resources.
         * The timestamp added to the output resource names are created from the job execution time using the specified pattern.
         * See also definition of the field "timestampPattern" below.
         *
         * @param flag Supported values: true, false, null. Default: false
         * @return builder for convenient configuration
         */
        public Builder withSequentialFileNames(@Nullable Boolean flag) {
            this.sequentialFileNames = flag;
            return this;
        }

        /**
         * Defines the pattern to be used for the timestamp included in the job output resource names. Default value "yyyyMMddHHmm"
         *
         * @param timestampPattern pattern to be used for timestamp generation
         * @return builder for convenient configuration
         */
        public Builder withTimestampPattern(@Nullable String timestampPattern) {
            this.timestampPattern = timestampPattern;
            return this;
        }

        /**
         * Decides whether the scheduler can overwrite files in the repository when saving job output resources.
         * If the flag is not set, the job would fail if the repository already contains a resource with the same name as one of the job output resources.
         * If the flag is set and the job owner does not have the permission to overwrite an existing resource, the job execution will also fail.
         *
         * @param flag Supported values: true, false, null. Default: false
         * @return builder for convenient configuration
         */
        public Builder withOverwriteFiles(@Nullable Boolean flag) {
            this.overwriteFiles = flag;
            return this;
        }

        /**
         * The description to be used for job output resources. The description will be used as is for all output resources.
         *
         * @param outputDescription value of output description
         * @return builder for convenient configuration
         */
        public Builder withOutputDescription(@Nullable String outputDescription) {
            this.outputDescription = outputDescription;
            return this;
        }

        /**
         * Specifies whether the scheduler should write files to the repository.
         *
         * @param flag Supported values: true, false, null. Default: true
         * @return builder for convenient configuration
         */
        public Builder withSaveToRepository(@Nullable Boolean flag) {
            this.saveToRepository = flag;
            return this;
        }

        /**
         * Specifies whether export the output files to default report output folder URI of the job owner.
         *
         * @param flag Supported values: true, false, null. Default: false
         * @return builder for convenient configuration
         */
        public Builder withUseDefaultReportOutputFolderURI(@Nullable Boolean flag) {
            this.useDefaultReportOutputFolderURI = flag;
            return this;
        }

        /**
         * The default scheduled report output folder URI of the job owner
         *
         * @param folderUri uri that represents logical location of folder on JRS instance
         * @return builder for convenient configuration
         */
        public Builder withDefaultReportOutputFolderURI(@NotNull String folderUri) {
            this.defaultReportOutputFolderURI = Preconditions.checkNotNull(folderUri, "Repository folder uri should not be null");
            return this;
        }

        /**
         * The output local path of the folder under which job output resources would be created. Local path means path in the JRS host's local file system.
         * This functionality is by default disabled and this field is ignored.
         * You can enable it in applicationContext.xml bean "configurationBean", property "enableSaveToHostFS"
         *
         * @param outputLocalFolder path to local path of output folder related to server file system
         * @return builder for convenient configuration
         */
        public Builder withOutputLocalFolder(@Nullable String outputLocalFolder) {
            this.outputLocalFolder = Preconditions.checkNotNull(outputLocalFolder, "Output local path should not be null");
            return this;
        }

        /**
         * The output FTP location information which job output resources would be created.
         *
         * @param jobOutputFtpInfo details regarding FTP server
         * @return builder for convenient configuration
         */
        public Builder withFtp(JobOutputFtpInfo jobOutputFtpInfo) {
            this.jobOutputFtpInfo = jobOutputFtpInfo;
            return this;
        }

        public RepositoryDestination build() {
            ensureDefaults();
            validateTimesTamp();
            return new RepositoryDestination(this);
        }

        private void ensureDefaults() {
            if (folderUri == null) {
                useDefaultReportOutputFolderURI = true;
            }
        }

        private void validateTimesTamp() {
            if (timestampPattern != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat();
                    dateFormat.applyPattern(timestampPattern);
                    String date = dateFormat.format(new Date());
                    dateFormat.parse(date);
                } catch (IllegalArgumentException | ParseException e) {
                    throw new IllegalArgumentException("Unparseable timestamp '" + timestampPattern + "'");
                }
            }
        }
    }
}
