package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class MonthsFieldWrapper {
    @Expose
    private Set<Integer> month;

    public Set<Integer> getMonth() {
        return month;
    }

    public void setMonth(Set<Integer> month) {
        this.month = month;
    }
}
