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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobFormEntity)) return false;

        JobFormEntity entity = (JobFormEntity) o;

        if (version != entity.version) return false;
        if (baseOutputFilename != null ? !baseOutputFilename.equals(entity.baseOutputFilename) : entity.baseOutputFilename != null)
            return false;
        if (description != null ? !description.equals(entity.description) : entity.description != null) return false;
        if (label != null ? !label.equals(entity.label) : entity.label != null) return false;
        if (outputFormats != null ? !outputFormats.equals(entity.outputFormats) : entity.outputFormats != null)
            return false;
        if (repositoryDestination != null ? !repositoryDestination.equals(entity.repositoryDestination) : entity.repositoryDestination != null)
            return false;
        if (source != null ? !source.equals(entity.source) : entity.source != null) return false;
        if (trigger != null ? !trigger.equals(entity.trigger) : entity.trigger != null) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + version;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (baseOutputFilename != null ? baseOutputFilename.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (repositoryDestination != null ? repositoryDestination.hashCode() : 0);
        result = 31 * result + (outputFormats != null ? outputFormats.hashCode() : 0);
        result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
        return result;
    }
}
