package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.service.data.report.AbstractResourceBuilder;
import com.jaspersoft.android.sdk.service.data.repository.PermissionMask;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.3
 */
abstract class AbstractResourceMapper<Target extends Resource, Source extends ResourceLookup> {
    @NotNull
    private final SimpleDateFormat mFormat;
    @Nullable
    private final ResourceType mBackupType;

    public AbstractResourceMapper(
            @NotNull SimpleDateFormat format,
            @Nullable ResourceType backupType
    ) {
        mFormat = format;
        mBackupType = backupType;
    }

    public AbstractResourceMapper(@NotNull SimpleDateFormat format) {
        this(format, null);
    }

    public abstract Target transform(Source lookup);

    protected AbstractResourceBuilder buildLookup(AbstractResourceBuilder builder, Source lookup) {
        Date creationDate;
        Date updateDate;

        try {
            creationDate = mFormat.parse(String.valueOf(lookup.getCreationDate()));
            updateDate = mFormat.parse(String.valueOf(lookup.getUpdateDate()));
        } catch (ParseException e) {
            creationDate = updateDate = null;
        }

        builder.withCreationDate(creationDate);
        builder.withUpdateDate(updateDate);
        builder.withLabel(lookup.getLabel());
        builder.withDescription(lookup.getDescription());
        builder.withUri(lookup.getUri());
        builder.withPermissionMask(PermissionMask.fromRawValue(lookup.getPermissionMask()));
        builder.withVersion(lookup.getVersion());

        String type = lookup.getResourceType();
        ResourceType resourceType = mBackupType;
        if (resourceType == null) {
            resourceType = ResourceType.defaultParser().parse(type);
        }
        builder.withResourceType(resourceType);

        return builder;
    }
}
