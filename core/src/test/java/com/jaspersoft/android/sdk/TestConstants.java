package com.jaspersoft.android.sdk;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class TestConstants {
    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATE_TIME_FORMAT =
            new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN, Locale.getDefault());
}
