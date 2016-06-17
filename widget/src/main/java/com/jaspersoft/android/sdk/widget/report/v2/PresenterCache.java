package com.jaspersoft.android.sdk.widget.report.v2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class PresenterCache {
    private static class InstanceHolder {
        private static final PresenterCache INSTANCE = new PresenterCache();
    }

    static PresenterCache getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private final Map<PresenterKey, ReportPresenter> cache = new HashMap<>();

    public ReportPresenter get(PresenterKey key) {
        return cache.get(key);
    }

    public void put(ReportPresenter presenter) {
        cache.put(presenter.getKey(), presenter);
    }

    public void remove(PresenterKey key) {
        cache.remove(key);
    }
}
