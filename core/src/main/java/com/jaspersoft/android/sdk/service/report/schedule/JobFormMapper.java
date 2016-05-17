package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobOutputFormat;
import com.jaspersoft.android.sdk.service.data.schedule.JobSource;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobFormMapper {
    private final JobTriggerMapper mTriggerMapper;

    JobFormMapper(JobTriggerMapper triggerMapper) {
        mTriggerMapper = triggerMapper;
    }

    private static class InstanceHolder {
        private final static JobFormMapper INSTANCE = new JobFormMapper(
                JobTriggerMapper.getInstance());
    }

    public static JobFormMapper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @NotNull
    public JobFormEntity toFormEntity(@NotNull JobForm form) {
        JobFormEntity entity = new JobFormEntity();
        mapFormToCommonEntityFields(form, entity);
        mapFormDestinationOnEntity(form, entity);
        mapFormSourceOnEntity(form, entity);
        mapFormFormatsOnEntity(form, entity);
        mTriggerMapper.toTriggerEntity(form, entity);
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
    void mapFormSourceOnEntity(JobForm form, JobFormEntity entity) {
        JobSource source = form.getSource();
        List<ReportParameter> params = source.getParameters();
        Map<String, Set<String>> values = mapSourceParamValues(params);
        entity.setSourceUri(source.getUri());
        entity.setSourceParameters(values);
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

    private Map<String, Set<String>> mapSourceParamValues(List<ReportParameter> params) {
        Map<String, Set<String>> values = new HashMap<>(params.size());
        for (ReportParameter param : params) {
            values.put(param.getName(), param.getValue());
        }
        return values;
    }

    @NotNull
    public JobForm toDataForm(@NotNull JobFormEntity entity) {
        JobForm.Builder form = new JobForm.Builder();
        mapEntityCommonFieldsToForm(form, entity);
        mapEntitySourceToForm(form, entity);
        mapEntityDestinationToForm(form, entity);
        mapEntityFormatsToForm(form, entity);
        mTriggerMapper.toDataForm(form, entity);
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
    void mapEntitySourceToForm(JobForm.Builder form, JobFormEntity entity) {
        JobSource.Builder builder = new JobSource.Builder();
        builder.withUri(entity.getSourceUri());

        Map<String, Set<String>> parameters = entity.getSourceParameters();
        if (parameters != null) {
            List<ReportParameter> params = mapParams(parameters);
            builder.withParameters(params);
        }

        JobSource source = builder.build();
        form.withJobSource(source);
    }

    @TestOnly
    void mapEntityDestinationToForm(JobForm.Builder form, JobFormEntity entity) {
        RepositoryDestination.Builder builder = new RepositoryDestination.Builder();
        builder.withFolderUri(entity.getRepositoryDestination());
        RepositoryDestination destination = builder.build();
        form.withRepositoryDestination(destination);
    }

    public List<ReportParameter> mapParams(Map<String, Set<String>> parameters) {
        List<ReportParameter> params = new ArrayList<>(parameters.size());
        for (Map.Entry<String, Set<String>> entry : parameters.entrySet()) {
            params.add(new ReportParameter(entry.getKey(), entry.getValue()));
        }
        return params;
    }
}
