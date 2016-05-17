package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobOutputFormat;
import com.jaspersoft.android.sdk.service.data.schedule.JobSource;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobFormFactory {

    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());
    private static final TimeZone TIME_ZONE = TimeZone.getDefault();

    private static Date START_DATE;
    private static Date END_DATE;

    private static final String END_DATE_SRC = "2013-11-03 16:32";
    private static final String START_DATE_SRC = "2013-10-03 16:32";

    static {
        try {
            END_DATE = DATE_FORMAT.parse(END_DATE_SRC);
            START_DATE = DATE_FORMAT.parse(START_DATE_SRC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private final JobForm.Builder mFormBuilder;
    private final JobSource mSource;
    private final List<JobOutputFormat> mFormats = Arrays.asList(JobOutputFormat.HTML, JobOutputFormat.CSV);
    private final RepositoryDestination mDestination;

    public JobFormFactory() {
        mFormBuilder = new JobForm.Builder();
        mDestination = new RepositoryDestination.Builder()
                .withFolderUri("/temp")
                .build();
        mSource = new JobSource.Builder()
                .withUri("/my/uri")
                .build();
    }


    public JobForm.Builder givenJobFormBuilderWithValues() {
        return mFormBuilder.withVersion(100)
                .withLabel("my label")
                .withDescription("Description")
                .withRepositoryDestination(mDestination)
                .withJobSource(mSource)
                .withOutputFormats(mFormats)
                .withBaseOutputFilename("output")
                .withStartDate(START_DATE)
                .withTimeZone(TIME_ZONE);
    }

    public JobForm givenJobFormWithValues() {
        return givenJobFormBuilderWithValues().build();
    }

    public TimeZone provideTimeZone() {
        return TIME_ZONE;
    }

    public String provideStartDateSrc() {
        return START_DATE_SRC;
    }

    public String provideEndDateSrc() {
        return END_DATE_SRC;
    }

    public Date provideEndDate() {
        return END_DATE;
    }
}
