package com.jaspersoft.android.sdk.widget.report.event;

import com.google.gson.annotations.SerializedName;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class JsException {
    @SerializedName("errorCode")
    public String errorCode;

    @SerializedName("message")
    public String errorMessage;
}
