package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.junit.Before;
import org.junit.Test;

public class JobExceptionMapperTest extends BaseExceptionMapperTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setExceptionMapper(new JobExceptionMapper(mDelegate));
    }

    @Test
    public void should_transform_job_output_filename_duplication() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("error.duplicate.report.job.output.filename");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.JOB_DUPLICATE_OUTPUT_FILE_NAME);
    }

    @Test
    public void should_transform_job_output_folder_does_not_exist() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("error.report.job.output.folder.inexistent");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.JOB_OUTPUT_FOLDER_DOES_NOT_EXIST);
    }

    @Test
    public void should_transform_job_output_folder_is_not_writable() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("error.report.job.output.folder.notwriteable");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.JOB_OUTPUT_FOLDER_IS_NOT_WRITABLE);
    }

    @Test
    public void should_transform_job_output_filename_invalid_chars() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("error.invalid.chars");
        givenErrorDescriptorWithField("baseOutputFilename");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.JOB_OUTPUT_FILENAME_INVALID_CHARS);
    }

    @Test
    public void should_transform_job_start_date_in_the_past() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("error.before.current.date");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.JOB_START_DATE_IN_THE_PAST);
    }

    @Test
    public void should_transform_job_output_file_name_too_long() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("error.length");
        givenErrorDescriptorWithField("baseOutputFilename");
        givenErrorDescriptorWithArguments("100");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.JOB_OUTPUT_FILENAME_TOO_LONG);
    }

    @Test
    public void should_transform_job_output_file_label_too_long() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("error.length");
        givenErrorDescriptorWithField("label");
        givenErrorDescriptorWithArguments("100");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.JOB_LABEL_TOO_LONG);
    }


}