package com.jaspersoft.android.sdk.service.report.schedule;

import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.5
 */
abstract class JobUnitDateParser {
    private static final SimpleDateFormat FORMAT_WITHOUT_MILLISECONDS_ZONE =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Nullable
    abstract Date parseDate(String rawDate);

    static class Factory {
        static JobUnitDateParser createParser() {
            try {
                Class.forName("android.os.Build");
                return AndroidDateParser.getInstance();
            } catch (ClassNotFoundException ignored) {
                return BaseDateParser.getInstance();
            }
        }
    }

    static class AndroidDateParser extends JobUnitDateParser {
        private final SimpleDateFormat formatWithZone;
        private final SimpleDateFormat formatWithMillisecondsZone;

        private static class Holder {
            private static final AndroidDateParser INSTANCE = new AndroidDateParser();
        }

        public static AndroidDateParser getInstance() {
            return Holder.INSTANCE;
        }

        private AndroidDateParser() {
            formatWithZone =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            formatWithMillisecondsZone =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        }

        @Nullable
        @Override
        Date parseDate(String rawDate) {
            return parse(rawDate, formatWithZone, formatWithMillisecondsZone, FORMAT_WITHOUT_MILLISECONDS_ZONE);
        }
    }

    static class BaseDateParser extends JobUnitDateParser {
        private final SimpleDateFormat formatWithZone;
        private final SimpleDateFormat formatWithMillisecondsZone;

        private static class Holder {
            private static final BaseDateParser INSTANCE = new BaseDateParser();
        }

        public static BaseDateParser getInstance() {
            return Holder.INSTANCE;
        }

        private BaseDateParser() {
            formatWithZone =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            formatWithMillisecondsZone =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        }

        @Nullable
        @Override
        Date parseDate(String rawDate) {
            return parse(rawDate, formatWithZone, formatWithMillisecondsZone, FORMAT_WITHOUT_MILLISECONDS_ZONE);
        }
    }

    @Nullable
    private static Date parse(String rawDate, SimpleDateFormat... formats) {
        for (SimpleDateFormat format : formats) {
            Date date = parseSilently(format, rawDate);
            if (date != null) {
                return date;
            }
        }
        return null;
    }

    @Nullable
    private static Date parseSilently(SimpleDateFormat format, String raw) {
        try {
            return format.parse(raw);
        } catch (ParseException e) {
            return null;
        }
    }
}
