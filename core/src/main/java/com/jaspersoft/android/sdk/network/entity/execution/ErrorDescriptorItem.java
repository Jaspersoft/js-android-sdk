package com.jaspersoft.android.sdk.network.entity.execution;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class ErrorDescriptorItem {
    @Expose
    private String errorCode;
    @Expose
    private String field;
    @Expose
    private List<String> errorArguments;

    public String getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }

    public List<String> getErrorArguments() {
        return errorArguments;
    }

    public String getErrorArgument(int index) {
        if (errorArguments == null || errorArguments.isEmpty()) {
            return null;
        }
        return errorArguments.get(index);
    }
}
