/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */
package com.jaspersoft.android.sdk.service.exception;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ExecutionException extends RuntimeException {
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
        return Kind.FAILED.report(reportUri);
    }

    public static ExecutionException reportFailed(String reportUri, Throwable throwable) {
        return Kind.FAILED.report(reportUri, throwable);
    }

    public static ExecutionException exportFailed(String reportUri) {
        return Kind.FAILED.export(reportUri);
    }

    public static ExecutionException exportFailed(String reportUri, Throwable throwable) {
        return Kind.FAILED.export(reportUri, throwable);
    }

    public static ExecutionException reportCancelled(String reportUri) {
        return Kind.CANCELLED.report(reportUri);
    }

    public static ExecutionException exportCancelled(String reportUri) {
        return Kind.CANCELLED.export(reportUri);
    }

    public boolean isCancelled() {
        return Kind.CANCELLED == mKind;
    }

    public boolean isFailed() {
        return Kind.FAILED == mKind;
    }

    private enum Kind {
        FAILED {
            @Override
            public ExecutionException report(String reportUri) {
                String message = String.format("Report execution '%s' failed on server side", reportUri);
                return new ExecutionException(this, message);
            }

            @Override
            public ExecutionException report(String reportUri, Throwable throwable) {
                String message = String.format("Export for report '%s' failed. Reason: %s", reportUri, throwable.getMessage());
                return new ExecutionException(this, message, throwable);
            }

            @Override
            public ExecutionException export(String reportUri) {
                String message = String.format("Export for report '%s' failed on server side", reportUri);
                return new ExecutionException(this, message);
            }

            @Override
            public ExecutionException export(String reportUri, Throwable throwable) {
                String message = String.format("Export for report '%s' failed. Reason: %s", reportUri, throwable.getMessage());
                return new ExecutionException(this, message, throwable);
            }
        }, CANCELLED {
            @Override
            public ExecutionException report(String reportUri) {
                String message = String.format("Report execution '%s' was cancelled", reportUri);
                return new ExecutionException(this, message);
            }

            @Override
            public ExecutionException report(String reportUri, Throwable throwable) {
                throw new UnsupportedOperationException();
            }

            @Override
            public ExecutionException export(String reportUri) {
                String message = String.format("Export for report '%s' was cancelled", reportUri);
                return new ExecutionException(this, message);
            }

            @Override
            public ExecutionException export(String reportUri, Throwable throwable) {
                throw new UnsupportedOperationException();
            }
        };

        public abstract ExecutionException report(String reportUri);

        public abstract ExecutionException report(String reportUri, Throwable throwable);

        public abstract ExecutionException export(String reportUri);

        public abstract ExecutionException export(String reportUri, Throwable throwable);
    }
}
