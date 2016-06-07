/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

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