package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class WeekDaysFieldWrapper {
    @Expose
    private Set<Integer> day;

    public Set<Integer> getDay() {
        return day;
    }

    public void setDay(Set<Integer> day) {
        this.day = day;
    }
}
