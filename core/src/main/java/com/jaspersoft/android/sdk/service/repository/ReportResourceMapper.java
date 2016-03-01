package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.service.data.report.AbstractResourceBuilder;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportResourceMapper extends AbstractResourceMapper<ReportResource, ReportLookup> {
    public ReportResourceMapper(@NotNull SimpleDateFormat format, @Nullable ResourceType backupType) {
        super(format, backupType);
    }

    public ReportResourceMapper(@NotNull SimpleDateFormat format) {
        super(format);
    }

    @Override
    public ReportResource transform(ReportLookup lookup) {
        ReportResource.Builder builder = new ReportResource.Builder();

        AbstractResourceBuilder abstractResourceBuilder = builder.addResource();
        buildLookup(abstractResourceBuilder, lookup);

        builder.withAlwaysPrompt(lookup.alwaysPromptControls());

        return builder.build();
    }
}