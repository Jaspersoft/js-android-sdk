package com.jaspersoft.android.sdk.service.report.schedule;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobSearchCriteriaMapper {

    @NotNull
    public Map<String, Object> transform(@NotNull JobSearchCriteria criteria) {
        Map<String, Object> searchParams = new HashMap<>(11);

        String reportUri = criteria.getReportUri();
        if (reportUri != null) {
            searchParams.put("reportUnitURI", reportUri);
        }

        JobOwner owner = criteria.getOwner();
        if (owner != null) {
            searchParams.put("owner", owner.toString());
        }

        String label = criteria.getLabel();
        if (label != null) {
            searchParams.put("label", label);
        }

        int offset = criteria.getOffset();
        searchParams.put("startIndex", String.valueOf(offset));

        int limit = criteria.getLimit();
        if (limit == JobSearchCriteria.UNLIMITED_ROW_NUMBER) {
            limit = -1;
        }
        searchParams.put("numberOfRows", String.valueOf(limit));

        JobSortType jobSortType = criteria.getSortType();
        if (jobSortType != null) {
            searchParams.put("sortType", jobSortType.toString());
        }

        Boolean ascending = criteria.getAscending();
        if (ascending != null) {
            searchParams.put("isAscending", String.valueOf(ascending));
        }

        return searchParams;
    }
}
