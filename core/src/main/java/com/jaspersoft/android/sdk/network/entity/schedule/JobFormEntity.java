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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobFormEntity {
    @Expose
    private String label;

    @Expose
    private int version;

    @Expose
    private String description;

    @Expose
    private String baseOutputFilename;

    @Expose
    private final JobSourceEntity source;

    @Expose
    private final RepositoryDestinationEntity repositoryDestination;

    @Expose
    private final JobOutputFormatsEntity outputFormats;

    @Expose
    private final JobTriggerWrapper trigger;

    @Expose
    private JobMailNotificationEntity mailNotification;

    @Expose
    private JobAlertEntity alert;

    public JobFormEntity() {
        this.source = new JobSourceEntity();
        this.repositoryDestination = new RepositoryDestinationEntity();
        this.outputFormats = new JobOutputFormatsEntity();
        this.trigger = new JobTriggerWrapper();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBaseOutputFilename(String baseOutputFilename) {
        this.baseOutputFilename = baseOutputFilename;
    }

    public void setSourceUri(String sourceUri) {
        source.setReportUnitURI(sourceUri);
    }

    public void setSourceParameters(Map<String, Set<String>> params) {
        source.setParameters(params);
    }

    /**
     * Refrain from use. Consider to set folder uri on repository destination.
     * See {@link JobFormEntity#getRepoDestination()}
     *
     * @param folderUri that represents particular target repository
     */
    @Deprecated
    public void setRepositoryDestination(String folderUri) {
        repositoryDestination.setFolderURI(folderUri);
    }

    /**
     * Exposes repository destination DTO entity
     *
     * @return repository destination
     */
    public RepositoryDestinationEntity getRepoDestination() {
        return repositoryDestination;
    }

    public void addOutputFormats(Collection<String> formats) {
        outputFormats.setOutputFormat(formats);
    }

    public void setSimpleTrigger(JobSimpleTriggerEntity simpleTrigger) {
        trigger.setSimpleTrigger(simpleTrigger);
    }

    public void setCalendarTrigger(JobCalendarTriggerEntity calendarTrigger) {
        trigger.setCalendarTrigger(calendarTrigger);
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getBaseOutputFilename() {
        return baseOutputFilename;
    }

    public String getSourceUri() {
        return source.getReportUnitURI();
    }

    public Map<String, Set<String>> getSourceParameters() {
        return source.getParameters();
    }

    /**
     * Refrain from use. Consider to set folder uri on repository destination.
     * See {@link JobFormEntity#getRepoDestination()}
     *
     * @return folder Uri that represents particular target repository
     */
    @Deprecated
    public String getRepositoryDestination() {
        return repositoryDestination.getFolderURI();
    }

    public Collection<String> getOutputFormats() {
        return outputFormats.getOutputFormat();
    }

    public JobSimpleTriggerEntity getSimpleTrigger() {
        return trigger.getSimpleTrigger();
    }

    public JobCalendarTriggerEntity getCalendarTrigger() {
        return trigger.getCalendarTrigger();
    }

    public void setMailNotification(JobMailNotificationEntity mailNotification) {
        this.mailNotification = mailNotification;
    }

    public JobMailNotificationEntity getMailNotification() {
        return mailNotification;
    }

    public JobTriggerEntity getTrigger() {
        JobSimpleTriggerEntity simpleTrigger = getSimpleTrigger();
        if (simpleTrigger == null) {
            return getCalendarTrigger();
        }
        return simpleTrigger;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public JobAlertEntity getAlert() {
        return alert;
    }

    public void setAlert(JobAlertEntity alert) {
        this.alert = alert;
    }
}
