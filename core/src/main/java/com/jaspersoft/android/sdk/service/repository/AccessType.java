package com.jaspersoft.android.sdk.service.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public enum AccessType {
    VIEWED, MODIFIED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static AccessType fromRawValue(String value) {
        Map<String, AccessType> map = new HashMap<>(2);
        map.put("viewed", VIEWED);
        map.put("modified", MODIFIED);

        AccessType accessType = map.get(value);
        if (accessType == null) {
            throw new IllegalArgumentException("There is no such access type '" + value + "'");
        }

        return accessType;
    }
}
