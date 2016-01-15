package com.jaspersoft.android.sdk.env;

/**
 * @author Tom Koptel
 * @since 2.0
 */
interface PendingTask<Result> {
    public Result perform() throws Exception;
}
