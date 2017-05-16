package com.jaspersoft.android.sdk.widget.report.view;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
abstract class Store<Persistable> {
    Map<ReportRendererKey, Persistable> storeMap = new HashMap<>();

    public Persistable restore(ReportRendererKey key){
        return storeMap.get(key);
    }

    public ReportRendererKey save(Persistable runOptions, ReportRendererKey key) {
        storeMap.put(key, runOptions);
        return key;
    }

    public void remove(ReportRendererKey key) {
        storeMap.remove(key);
    }
}
