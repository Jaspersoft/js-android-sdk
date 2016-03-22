package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobDescriptor;
import com.jaspersoft.android.sdk.network.entity.schedule.JobOutputFormatsEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.data.schedule.JobOutputFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobDataMapperTest {
    public static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());

    private JobDataMapper jobDataMapper;

    @Mock
    JobDescriptor mJobDescriptor;

    @Mock
    JobOutputFormatsEntity mJobOutputFormats;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        setupMocks();
        jobDataMapper = new JobDataMapper();
    }

    @Test
    public void testTransform() throws Exception {
        long creationTime = DATE_FORMAT.parse("2013-10-03 16:32:05").getTime();

        JobData jobData = jobDataMapper.transform(mJobDescriptor, DATE_FORMAT);

        assertThat("Should map id", jobData.getId(), is(20));
        assertThat("Should map version", jobData.getVersion(), is(2));
        assertThat("Should map username", jobData.getUsername(), is("user"));
        assertThat("Should map label", jobData.getLabel(), is("label"));
        assertThat("Should map description", jobData.getDescription(), is("description"));
        assertThat("Should map creation date", jobData.getCreationDate().getTime(), is(creationTime));
        assertThat("Should map description", jobData.getOutputFormats(), hasItem(JobOutputFormat.HTML));
    }

    private void setupMocks() {
        when(mJobDescriptor.getId()).thenReturn(20);
        when(mJobDescriptor.getVersion()).thenReturn(2);
        when(mJobDescriptor.getUsername()).thenReturn("user");
        when(mJobDescriptor.getLabel()).thenReturn("label");
        when(mJobDescriptor.getDescription()).thenReturn("description");
        when(mJobDescriptor.getCreationDate()).thenReturn("2013-10-03 16:32:05");
        when(mJobDescriptor.getOutputFormats()).thenReturn(mJobOutputFormats);
        when(mJobOutputFormats.getOutputFormat()).thenReturn(Collections.singletonList("HTML"));
    }
}