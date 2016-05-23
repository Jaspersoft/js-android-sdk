package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.rules.ExpectedException.none;

public class JobOutputFtpInfoTest {

    @Rule
    public ExpectedException expected = none();

    @Test
    public void should_not_accept_negative_port_number() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Port should be in range of 0 and 2^32 - 1");
        new JobOutputFtpInfo.Builder()
                .withPort(-1)
                .build();
    }
}