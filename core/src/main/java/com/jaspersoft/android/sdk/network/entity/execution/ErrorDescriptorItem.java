package com.jaspersoft.android.sdk.network.entity.execution;

import com.google.gson.annotations.Expose;

import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ErrorDescriptorItem {
    @Expose
    private String errorCode;
    @Expose
    private String field;
    @Expose
    private Set<String> errorArguments;

    public String getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }

    public Set<String> getErrorArguments() {
        return errorArguments;
    }
}
