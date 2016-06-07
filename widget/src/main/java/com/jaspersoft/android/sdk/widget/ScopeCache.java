package com.jaspersoft.android.sdk.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.5
 */
enum ScopeCache {
    INSTANCE;

    private final Map<String, Scope> cache = new HashMap<>();

    public Scope get(String key) {
        return cache.get(key);
    }

    public void put(String key, Scope scope) {
        cache.put(key, scope);
    }
}
