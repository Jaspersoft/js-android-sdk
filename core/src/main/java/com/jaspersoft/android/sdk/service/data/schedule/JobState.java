package com.jaspersoft.android.sdk.service.data.schedule;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public enum JobState {
    /**
     * The job is scheduled.
     */
    NORMAL,
    /**
     * The server is generating the report output.
     */
    EXECUTING,
    /**
     * The server has finished running the job and placed output to the repository.
     */
    COMPLETE,
    /**
     * The job has been disabled. Click Enabled to resume the schedule.
     */
    PAUSED,
    /**
     * The scheduler encountered an error while scheduling or triggering the job. This doesn’t include
     * cases where the job is successfully triggered, but an error occurs while it runs.
     */
    ERROR,
    /**
     * The scheduler encountered an error with the job trigger.
     */
    UNKNOWN
}
