package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionEntity;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportOptionMapper {

    public Set<ReportOption> transform(Set<ReportOptionEntity> entities) {
        Set<ReportOption> options = new HashSet<>(entities.size());
        for (ReportOptionEntity entity : entities) {
            if (entity != null) {
                ReportOption option = transform(entity);
                options.add(option);
            }
        }
        return options;
    }

    public ReportOption transform(ReportOptionEntity entity) {
        return new ReportOption.Builder()
                .withId(entity.getId())
                .withUri(entity.getUri())
                .withLabel(entity.getLabel())
                .build();
    }
}
