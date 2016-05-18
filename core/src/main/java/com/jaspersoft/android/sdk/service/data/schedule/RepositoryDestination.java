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
    private String mFolderUri;
    private Boolean mSequentialFileNames;
    private Boolean mOverwriteFiles;
    private Boolean mSaveToRepository;
    private Boolean mUseDefaultReportOutputFolderURI;
    private String mTimestampPattern;
    private String mDefaultReportOutputFolderURI;
    private String mOutputDescription;
    private String mOutputLocalFolder;

    RepositoryDestination(Builder builder) {
        mFolderUri = builder.folderUri;
        mSequentialFileNames = builder.sequentialFileNames;
        mOverwriteFiles = builder.overwriteFiles;
        mSaveToRepository = builder.saveToRepository;
        mUseDefaultReportOutputFolderURI = builder.useDefaultReportOutputFolderURI;
        mTimestampPattern = builder.timestampPattern;
        mOutputDescription = builder.outputDescription;
        mOutputLocalFolder = builder.outputLocalFolder;
        mDefaultReportOutputFolderURI = builder.defaultReportOutputFolderURI;
    }

    @Nullable
    public Boolean getSequentialFileNames() {
        return mSequentialFileNames;
    }

    @Nullable
    public Boolean getOverwriteFiles() {
        return mOverwriteFiles;
    }

    @Nullable
    public Boolean getSaveToRepository() {
        return mSaveToRepository;
    }

    @Nullable
    public Boolean getUseDefaultReportOutputFolderURI() {
        return mUseDefaultReportOutputFolderURI;
    }

    @Nullable
    public String getTimestampPattern() {
        return mTimestampPattern;
    }

    @Nullable
    public String getOutputDescription() {
        return mOutputDescription;
    }

    @Nullable
    public String getOutputLocalFolder() {
        return mOutputLocalFolder;
    }

    @NotNull
    public String getFolderUri() {
        return mFolderUri;
    }

    @Nullable
    public String getDefaultReportOutputFolderURI() {
        return mDefaultReportOutputFolderURI;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryDestination that = (RepositoryDestination) o;

        if (mFolderUri != null ? !mFolderUri.equals(that.mFolderUri) : that.mFolderUri != null) return false;
        if (mSequentialFileNames != null ? !mSequentialFileNames.equals(that.mSequentialFileNames) : that.mSequentialFileNames != null)
            return false;
        if (mOverwriteFiles != null ? !mOverwriteFiles.equals(that.mOverwriteFiles) : that.mOverwriteFiles != null)
            return false;
        if (mSaveToRepository != null ? !mSaveToRepository.equals(that.mSaveToRepository) : that.mSaveToRepository != null)
            return false;
        if (mUseDefaultReportOutputFolderURI != null ? !mUseDefaultReportOutputFolderURI.equals(that.mUseDefaultReportOutputFolderURI) : that.mUseDefaultReportOutputFolderURI != null)
            return false;
        if (mTimestampPattern != null ? !mTimestampPattern.equals(that.mTimestampPattern) : that.mTimestampPattern != null)
            return false;
        if (mOutputDescription != null ? !mOutputDescription.equals(that.mOutputDescription) : that.mOutputDescription != null)
            return false;
        return !(mOutputLocalFolder != null ? !mOutputLocalFolder.equals(that.mOutputLocalFolder) : that.mOutputLocalFolder != null);
    }

    @Override
    public int hashCode() {
        int result = mFolderUri != null ? mFolderUri.hashCode() : 0;
        result = 31 * result + (mSequentialFileNames != null ? mSequentialFileNames.hashCode() : 0);
        result = 31 * result + (mOverwriteFiles != null ? mOverwriteFiles.hashCode() : 0);
        result = 31 * result + (mSaveToRepository != null ? mSaveToRepository.hashCode() : 0);
        result = 31 * result + (mUseDefaultReportOutputFolderURI != null ? mUseDefaultReportOutputFolderURI.hashCode() : 0);
        result = 31 * result + (mTimestampPattern != null ? mTimestampPattern.hashCode() : 0);
        result = 31 * result + (mOutputDescription != null ? mOutputDescription.hashCode() : 0);
        result = 31 * result + (mOutputLocalFolder != null ? mOutputLocalFolder.hashCode() : 0);
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

        public Builder() {
        }

        Builder(RepositoryDestination destination) {
            folderUri = destination.mFolderUri;
            sequentialFileNames = destination.mSequentialFileNames;
            overwriteFiles = destination.mOverwriteFiles;
            saveToRepository = destination.mSaveToRepository;
            useDefaultReportOutputFolderURI = destination.mUseDefaultReportOutputFolderURI;
            timestampPattern = destination.mTimestampPattern;
            outputDescription = destination.mOutputDescription;
            outputLocalFolder = destination.mOutputLocalFolder;
            defaultReportOutputFolderURI = destination.mDefaultReportOutputFolderURI;
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
