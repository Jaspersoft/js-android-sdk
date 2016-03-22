package com.jaspersoft.android.sdk.service.repository;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class AccessTypeTest {
    @Test
    public void testViewedEnum() throws Exception {
        assertThat(AccessType.VIEWED.toString(), is("viewed"));
    }
    @Test
    public void testModifiedEnum() throws Exception {
        assertThat(AccessType.MODIFIED.toString(), is("modified"));
    }

    @Test
    @Parameters({
            "VIEWED|viewed",
            "MODIFIED|modified",
    })
    public void should_map_raw_value(String value, String raw) {
        AccessType actual = AccessType.valueOf(value);
        AccessType expected = AccessType.fromRawValue(raw);
        assertThat(expected, is(actual));
    }
}