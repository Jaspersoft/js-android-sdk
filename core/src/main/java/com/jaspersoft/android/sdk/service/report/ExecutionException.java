/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.service.report.exception.ReportExportException;
import com.jaspersoft.android.sdk.service.report.exception.ReportRunException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ExecutionException extends RuntimeException {
    private final Kind mKind;

    private ExecutionException(Kind kind, String message) {
        super(message);
        mKind = kind;
    }

    private ExecutionException(Kind kind, String message, Throwable throwable) {
        super(message, throwable);
        mKind = kind;
    }

    public static ExecutionException reportFailed(String reportUri) {
        return Kind.REPORT_FAILED.exception(reportUri);
    }

    public static ExecutionException reportFailed(String reportUri, Throwable throwable) {
        return Kind.REPORT_FAILED.exception(reportUri, throwable);
    }

    public static ExecutionException reportCancelled(String reportUri) {
        return Kind.REPORT_CANCELLED.exception(reportUri);
    }

    public static ExecutionException exportFailed(String reportUri) {
        return Kind.EXPORT_FAILED.exception(reportUri);
    }

    public static ExecutionException exportFailed(String reportUri, Throwable throwable) {
        return Kind.EXPORT_FAILED.exception(reportUri, throwable);
    }

    public static ExecutionException exportCancelled(String reportUri) {
        return Kind.EXPORT_CANCELLED.exception(reportUri);
    }

    public boolean isCancelled() {
        return Kind.EXPORT_CANCELLED == mKind || Kind.REPORT_CANCELLED == mKind;
    }

    public RuntimeException adaptToClientException() {
        switch (mKind) {
            case REPORT_FAILED:
            case REPORT_CANCELLED:
                return new ReportRunException(getMessage(), getCause());
            case EXPORT_FAILED:
            case EXPORT_CANCELLED:
                return new ReportExportException(getMessage(), getCause());
        }
        return new UnsupportedOperationException("Unexpected type of kind: " + mKind);
    }

    private enum Kind {
        REPORT_FAILED {
            @Override
            public ExecutionException exception(String reportUri) {
                String message = String.format("Report execution '%s' failed on server side", reportUri);
                return new ExecutionException(this, message);
            }
            @Override
            public ExecutionException exception(String reportUri, Throwable throwable) {
                String message = String.format("Report execution '%s' failed. Reason: %s", reportUri, throwable.getMessage());
                return new ExecutionException(this, message, throwable);
            }
        },
        REPORT_CANCELLED {
            @Override
            public ExecutionException exception(String reportUri) {
                String message = String.format("Report execution '%s' was cancelled", reportUri);
                return new ExecutionException(this, message);
            }
        },
        EXPORT_FAILED {
            @Override
            public ExecutionException exception(String reportUri) {
                String message = String.format("Export for report '%s' failed on server side", reportUri);
                return new ExecutionException(this, message);
            }
            @Override
            public ExecutionException exception(String reportUri, Throwable throwable) {
                String message = String.format("Export for report '%s' failed. Reason: %s", reportUri, throwable.getMessage());
                return new ExecutionException(this, message, throwable);
            }
        },
        EXPORT_CANCELLED {
            @Override
            public ExecutionException exception(String reportUri) {
                String message = String.format("Export for report '%s' was cancelled", reportUri);
                return new ExecutionException(this, message);
            }
        };

        public ExecutionException exception(String message) {
            throw new UnsupportedOperationException();
        }

        public ExecutionException exception(String message, Throwable throwable) {
            throw new UnsupportedOperationException();
        }
    }
}
