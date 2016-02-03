package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.FileLookup;
import com.jaspersoft.android.sdk.service.data.report.AbstractResourceBuilder;
import com.jaspersoft.android.sdk.service.data.report.FileResource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class FileResourceMapper extends AbstractResourceMapper<FileResource, FileLookup> {
    public FileResourceMapper(@NotNull SimpleDateFormat format, @Nullable ResourceType backupType) {
        super(format, backupType);
    }

    public FileResourceMapper(@NotNull SimpleDateFormat format) {
        super(format);
    }

    @Override
    public FileResource transform(FileLookup lookup) {
        FileResource.Builder builder = new FileResource.Builder();

        AbstractResourceBuilder abstractResourceBuilder = builder.addResource();
        buildLookup(abstractResourceBuilder, lookup);

        FileResource.Type type = FileResource.Type.valueOf(lookup.getType());
        builder.withFileType(type);

        return builder.build();
    }
}
