package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobOutputFormat;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobFormMapper {
    private final JobTriggerMapper mTriggerMapper;
    private final JobSourceMapper mJobSourceMapper;

    JobFormMapper(JobTriggerMapper triggerMapper, JobSourceMapper jobSourceMapper) {
        mTriggerMapper = triggerMapper;
        mJobSourceMapper = jobSourceMapper;
    }

    private static class InstanceHolder {
        private final static JobFormMapper INSTANCE = new JobFormMapper(
                JobTriggerMapper.INSTANCE,
                JobSourceMapper.INSTANCE
        );
    }

    public static JobFormMapper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @NotNull
    public JobFormEntity toFormEntity(@NotNull JobForm form) {
        JobFormEntity entity = new JobFormEntity();
        mapFormToCommonEntityFields(form, entity);
        mapFormDestinationOnEntity(form, entity);
        mJobSourceMapper.mapFormOnEntity(form, entity);
        mapFormFormatsOnEntity(form, entity);
        mTriggerMapper.mapFormOnEntity(form, entity);
        return entity;
    }

    @TestOnly
    void mapFormToCommonEntityFields(JobForm form, JobFormEntity entity) {
        entity.setLabel(form.getLabel());
        entity.setDescription(form.getDescription());
        entity.setBaseOutputFilename(form.getBaseOutputFilename());

        Integer version = form.getVersion();
        if (version != null) {
            entity.setVersion(version);
        }
    }

    @TestOnly
    void mapFormDestinationOnEntity(JobForm form, JobFormEntity entity) {
        RepositoryDestination repositoryDestination = form.getRepositoryDestination();
        entity.setRepositoryDestination(repositoryDestination.getFolderUri());
    }

    @TestOnly
    void mapFormFormatsOnEntity(JobForm form, JobFormEntity entity) {
        Set<JobOutputFormat> outputFormats = form.getOutputFormats();
        Collection<String> formats = new ArrayList<>(outputFormats.size());
        for (JobOutputFormat outputFormat : outputFormats) {
            formats.add(outputFormat.toString());
        }
        entity.addOutputFormats(formats);
    }

    @NotNull
    public JobForm toDataForm(@NotNull JobFormEntity entity) {
        JobForm.Builder form = new JobForm.Builder();
        mapEntityCommonFieldsToForm(form, entity);
        mJobSourceMapper.mapEntityOnForm(form, entity);
        mapEntityDestinationToForm(form, entity);
        mapEntityFormatsToForm(form, entity);
        mTriggerMapper.mapEntityOnForm(form, entity);
        return form.build();
    }

    @TestOnly
    void mapEntityCommonFieldsToForm(JobForm.Builder form, JobFormEntity entity) {
        form.withVersion(entity.getVersion());
        form.withLabel(entity.getLabel());
        form.withDescription(entity.getDescription());
        form.withBaseOutputFilename(entity.getBaseOutputFilename());
    }

    @TestOnly
    void mapEntityFormatsToForm(JobForm.Builder form, JobFormEntity entity) {
        Collection<String> formats = entity.getOutputFormats();
        List<JobOutputFormat> formatList = new ArrayList<>();
        for (String format : formats) {
            JobOutputFormat out = JobOutputFormat.valueOf(format);
            formatList.add(out);
        }
        form.withOutputFormats(formatList);
    }

    @TestOnly
    void mapEntityDestinationToForm(JobForm.Builder form, JobFormEntity entity) {
        RepositoryDestination.Builder builder = new RepositoryDestination.Builder();
        builder.withFolderUri(entity.getRepositoryDestination());
        RepositoryDestination destination = builder.build();
        form.withRepositoryDestination(destination);
    }
}
