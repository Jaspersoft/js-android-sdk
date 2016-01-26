package com.jaspersoft.android.sdk.service.filter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ControlLocation {
    private final String mUri;
    private final Set<String> mIds;

    ControlLocation(String uri) {
        mUri = uri;
        mIds = new HashSet<>();
    }

    public String getUri() {
        return mUri;
    }

    public Set<String> getIds() {
        return mIds;
    }

    public ControlLocation addId(String id) {
        mIds.add(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ControlLocation that = (ControlLocation) o;

        if (mIds != null ? !mIds.equals(that.mIds) : that.mIds != null) return false;
        if (mUri != null ? !mUri.equals(that.mUri) : that.mUri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mUri != null ? mUri.hashCode() : 0;
        result = 31 * result + (mIds != null ? mIds.hashCode() : 0);
        return result;
    }
}
