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
