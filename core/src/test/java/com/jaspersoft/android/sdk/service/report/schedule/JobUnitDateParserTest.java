package com.jaspersoft.android.sdk.service.report.schedule;

import org.junit.Before;
import org.junit.Test;

public class JobUnitDateParserTest {
    private static final String PREVIOUS_FIRE_TIME = "2016-03-24T00:00:00+02:00";
    private static final String NEXT_FIRE_TIME = "2016-03-21T12:18:08.979+02:00";
    private JobUnitDateParser parser;

    @Before
    public void setUp() throws Exception {
        parser = JobUnitDateParser.Factory.createParser();
    }

    @Test
    public void should_parse_without_milliseconds() throws Exception {
        parser.parseDate(PREVIOUS_FIRE_TIME);
    }

    @Test
    public void should_parse_with_milliseconds() throws Exception {
        parser.parseDate(NEXT_FIRE_TIME);
    }
}