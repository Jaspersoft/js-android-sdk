package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobCalendarTriggerEntity extends JobTriggerEntity {
    @Expose
    private String minutes;
    @Expose
    private String hours;
    @Expose
    private MonthsFieldWrapper months;
    @Expose
    private String daysType;
    @Expose
    private WeekDaysFieldWrapper weekDays;
    @Expose
    private String monthDays;

    public JobCalendarTriggerEntity() {
        months = new MonthsFieldWrapper();
        weekDays = new WeekDaysFieldWrapper();
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public Set<Integer> getMonths() {
        return months.getMonth();
    }

    public void setMonths(Set<Integer> months) {
        this.months.setMonth(months);
    }

    public String getDaysType() {
        return daysType;
    }

    public void setDaysType(String daysType) {
        this.daysType = daysType;
    }

    public Set<Integer> getWeekDays() {
        return weekDays.getDay();
    }

    public void setWeekDays(Set<Integer> weekDays) {
        this.weekDays.setDay(weekDays);
    }

    public String getMonthDays() {
        return monthDays;
    }

    public void setMonthDays(String monthDays) {
        this.monthDays = monthDays;
    }
}
