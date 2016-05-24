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

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobForm {
    @Nullable
    private final Integer version;
    @NotNull
    private final String label;
    @Nullable
    private final String description;
    @NotNull
    private final String baseOutputFilename;
    @NotNull
    private final JobSource source;
    @NotNull
    private final RepositoryDestination repositoryDestination;
    @NotNull
    private final Set<JobOutputFormat> outputFormats;
    @Nullable
    private final Date startDate;
    @Nullable
    private final TimeZone timeZone;
    @Nullable
    private final Trigger trigger;
    @Nullable
    private final JobMailNotification mailNotification;

    JobForm(Builder builder) {
        version = builder.version;
        label = builder.label;
        description = builder.description;
        baseOutputFilename = builder.baseOutputFilename;
        source = builder.source;
        repositoryDestination = builder.repositoryDestination;
        outputFormats = builder.outputFormats;
        startDate = builder.startDate;
        timeZone = builder.timeZone;
        trigger = builder.trigger;
        mailNotification = builder.mailNotification;
    }

    @Nullable
    public Integer getVersion() {
        return version;
    }

    @NotNull
    public String getLabel() {
        return label;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @NotNull
    public String getBaseOutputFilename() {
        return baseOutputFilename;
    }

    @NotNull
    public JobSource getSource() {
        return source;
    }

    @NotNull
    public RepositoryDestination getRepositoryDestination() {
        return repositoryDestination;
    }

    @NotNull
    public Set<JobOutputFormat> getOutputFormats() {
        return outputFormats;
    }

    @Nullable
    public Date getStartDate() {
        return startDate;
    }

    @Nullable
    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Nullable
    public Trigger getTrigger() {
        return trigger;
    }

    @Nullable
    public JobMailNotification getMailNotification() {
        return mailNotification;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private Integer version;
        private String label;
        private String description;
        private String baseOutputFilename;
        private RepositoryDestination repositoryDestination;
        private JobSource source;
        private Date startDate;
        private TimeZone timeZone;
        private JobMailNotification mailNotification;

        private Set<JobOutputFormat> outputFormats;
        private Trigger trigger;

        public Builder() {}

        private Builder(JobForm form) {
            version = form.version;
            label = form.label;
            description = form.description;
            baseOutputFilename = form.baseOutputFilename;
            source = form.source;
            repositoryDestination = form.repositoryDestination;
            outputFormats = form.outputFormats;
            startDate = form.startDate;
            timeZone = form.timeZone;
            trigger = form.trigger;
            mailNotification = form.mailNotification;
        }

        /**
         * Allows to specify version of form. One is required for update operations.
         *
         * @param version can be any whole number that represents
         * @return builder for convenient configuration
         */
        public Builder withVersion(@Nullable Integer version) {
            this.version = version;
            return this;
        }

        /**
         * Allows to specify report that was targeted for job execution.
         *
         * @param jobSource object that encapsulates job source metadata
         * @return builder for convenient configuration
         */
        public Builder withJobSource(@NotNull JobSource jobSource) {
            this.source = jobSource;
            return this;
        }

        /**
         * Allows to specify destination, such as output folder where result of schedule will be stored
         *
         * @param repositoryDestination the place where schedule execution artifacts will be stored
         * @return builder for convenient configuration
         */
        public Builder withRepositoryDestination(@NotNull RepositoryDestination repositoryDestination) {
            this.repositoryDestination = repositoryDestination;
            return this;
        }

        /**
         * Allows to specify the name of schedule job
         *
         * @param label name of job
         * @return builder for convenient configuration
         */
        public Builder withLabel(@NotNull String label) {
            Preconditions.checkNotNull(label, "Label should not be null");
            this.label = label;
            return this;
        }

        /**
         * Allows to specify the description of schedule job
         *
         * @param description of job
         * @return builder for convenient configuration
         */
        public Builder withDescription(@Nullable String description) {
            this.description = description;
            return this;
        }

        /**
         * Name of base output file
         *
         * @param baseOutputFilename any acceptable name that is restricted by set of invalid file system characters
         * @return builder for convenient configuration
         */
        public Builder withBaseOutputFilename(@NotNull String baseOutputFilename) {
            Preconditions.checkNotNull(baseOutputFilename, "Output file name should not be null");
            this.baseOutputFilename = baseOutputFilename;
            return this;
        }

        /**
         * Allows to specify collection of target output formats
         *
         * @param outputFormats any allowed job schedule formats
         * @return builder for convenient configuration
         */
        public Builder withOutputFormats(@NotNull Collection<JobOutputFormat> outputFormats) {
            Preconditions.checkNotNull(outputFormats, "Formats should not be null");
            this.outputFormats = new HashSet<>(outputFormats);
            return this;
        }

        /**
         * Allows to specify start data of job schedule execution
         *
         * @param startDate any future date. All dates supplied by this field would use default server timezone if one skipped
         * @return builder for convenient configuration
         */
        public Builder withStartDate(@Nullable Date startDate) {
            this.startDate = startDate;
            return this;
        }

        /**
         * Allows to specify respective timezone, so that time conversion issue will be resolved
         *
         * @param timeZone if value not supplied will fallback to JRS current time zone
         * @return builder for convenient configuration
         */
        public Builder withTimeZone(@Nullable TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        /**
         * Allows to specify either none, simple or calendar triggers
         * 
         * @param trigger specifies the frequency with which job will be executed
         * @return builder for convenient configuration
         */
        public Builder withTrigger(@Nullable Trigger trigger) {
            this.trigger = trigger;
            return this;
        }

        /**
         * The mail notification settings
         *
         * @param mailNotification specifies the configuration of mail notifications
         * @return builder for convenient configuration
         */
        public Builder withMailNotification(@Nullable JobMailNotification mailNotification) {
            this.mailNotification = mailNotification;
            return this;
        }

        public JobForm build() {
            assertState();
            return new JobForm(this);
        }

        private void assertState() {
            if (outputFormats.isEmpty()) {
                throw new IllegalStateException("Job can not be scheduled without output format");
            }
            Preconditions.checkNotNull(label, "Job can not be scheduled without label");
            Preconditions.checkNotNull(source, "Job can not be scheduled without source");
            Preconditions.checkNotNull(repositoryDestination, "Job can not be scheduled without repository destination");
            Preconditions.checkNotNull(baseOutputFilename, "Job can not be scheduled without output file name");
        }
    }
}
