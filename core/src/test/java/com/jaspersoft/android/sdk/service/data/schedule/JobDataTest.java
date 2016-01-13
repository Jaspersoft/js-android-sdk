package com.jaspersoft.android.sdk.service.data.schedule;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class JobDataTest {
    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(JobData.class).verify();
    }
}