package com.jaspersoft.android.sdk.env;

/**
 * @author Tom Koptel
 * @since 2.3
 */
interface PendingTask<Result> {
    public Result perform() throws Exception;
}
