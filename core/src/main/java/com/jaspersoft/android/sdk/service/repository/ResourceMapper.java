package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.service.data.report.AbstractResourceBuilder;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ResourceMapper extends AbstractResourceMapper<Resource, ResourceLookup> {
    public ResourceMapper(@NotNull SimpleDateFormat format) {
        super(format);
    }

    public ResourceMapper(@NotNull SimpleDateFormat format, @Nullable ResourceType backupType) {
        super(format, backupType);
    }

    @Override
    public Resource transform(ResourceLookup lookup) {
        AbstractResourceBuilder<?> builder = new AbstractResourceBuilder<>(null);
        buildLookup(builder, lookup);
        return builder.getResourceBuilder().build();
    }
}
