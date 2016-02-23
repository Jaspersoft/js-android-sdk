package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

public class TimePatternTest {

    private TimePattern mTimePattern;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        mTimePattern = new TimePattern(0, 59);
    }

    @Test
    public void should_accept_range_within_bounds() throws Exception {
        mTimePattern.setRange(0, 10);
        assertThat(mTimePattern.toString(), is("0-10"));
    }

    @Test
    public void should_accept_value_within_bounds() throws Exception {
        mTimePattern.setValue(10);
        assertThat(mTimePattern.toString(), is("10"));
    }

    @Test
    public void should_accept_increment_within_bounds() throws Exception {
        mTimePattern.setIncrement(5, 10);
        assertThat(mTimePattern.toString(), is("5/10"));
    }

    @Test
    public void should_throw_if_lower_bound_higher_than_upper_one() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("start must be lesser than end.");

        mTimePattern.setRange(10, 0);
    }

    @Test
    public void should_throw_if_start_less_than_lower_bound() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("start cannot be less than lower bound.");

        mTimePattern.setRange(-10, 0);
    }

    @Test
    public void should_throw_if_upper_bound_negative() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("end cannot be more than upper bound.");

        mTimePattern.setRange(0, 200);
    }

    @Test
    public void should_not_accept_value_that_less_than_lower_bound() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Value should be within bounds [ 0, 59 ]");

        mTimePattern.setValue(-10);
    }

    @Test
    public void should_not_accept_value_that_more_than_upper_bound() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Value should be within bounds [ 0, 59 ]");

        mTimePattern.setValue(100);
    }

    @Test
    public void should_not_accept_interval_that_less_than_lower_bound() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Interval should be within bounds [ 0, 59 ]");

        mTimePattern.setIncrement(-10, 5);
    }

    @Test
    public void should_not_accept_interval_that_more_than_upper_bound() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Interval should be within bounds [ 0, 59 ]");

        mTimePattern.setIncrement(100, -5);
    }

    @Test
    public void should_not_accept_from_that_less_than_lower_bound() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("From should be within bounds [ 0, 59 ]");

        mTimePattern.setIncrement(0, -5);
    }

    @Test
    public void should_not_accept_from_that_more_than_upper_bound() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("From should be within bounds [ 0, 59 ]");

        mTimePattern.setIncrement(0, 100);
    }


}