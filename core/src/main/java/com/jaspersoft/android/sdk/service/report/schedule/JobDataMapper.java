package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobDescriptor;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.data.schedule.JobOutputFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobDataMapper {
    public JobData transform(JobDescriptor jobDescriptor, SimpleDateFormat dateTimeFormat) {
        JobData.Builder builder = new JobData.Builder();
        builder.withId(jobDescriptor.getId());
        builder.withLabel(jobDescriptor.getLabel());
        builder.withDescription(jobDescriptor.getDescription());
        builder.withVersion(jobDescriptor.getVersion());
        builder.withLabel(jobDescriptor.getLabel());
        builder.withUsername(jobDescriptor.getUsername());

        Date creationDate;
        try {
            creationDate = dateTimeFormat.parse(jobDescriptor.getCreationDate());
        } catch (ParseException e) {
            creationDate = null;
        }
        builder.withCreationDate(creationDate);

        Collection<String> outputFormat = jobDescriptor.getOutputFormats().getOutputFormat();
        List<JobOutputFormat> formats = new ArrayList<>(outputFormat.size());
        for (String format : outputFormat) {
            formats.add(JobOutputFormat.valueOf(format));
        }
        builder.withOutputFormats(formats);

        return builder.build();
    }
}
