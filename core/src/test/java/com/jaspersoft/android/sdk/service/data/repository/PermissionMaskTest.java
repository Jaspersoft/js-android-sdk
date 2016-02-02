package com.jaspersoft.android.sdk.service.data.repository;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

@RunWith(JUnitParamsRunner.class)
public class PermissionMaskTest {

    @Rule
    public ExpectedException expected = none();

    @Test
    @Parameters({
            "NO_ACCESS|0",
            "ADMINISTER|1",
            "READ_ONLY|2",
            "READ_WRITE|6",
            "READ_DELETE|18",
            "READ_WRITE_DELETE|30",
            "EXECUTE_ONLY|32",
    })
    public void mapWith(String value, int mask) {
        PermissionMask actual = PermissionMask.valueOf(value);
        PermissionMask expected = PermissionMask.valueOf(mask);
        assertThat(expected, is(actual));
    }

    @Test
    public void should_throw_illegal_argument_exception() throws Exception {
        expected.expectMessage("Undefined type of mask: '100'");
        expected.expect(IllegalArgumentException.class);
        PermissionMask.valueOf(100);
    }
}