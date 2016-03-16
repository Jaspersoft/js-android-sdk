package com.jaspersoft.android.sdk.service.data.report;

import com.jaspersoft.android.sdk.service.data.repository.PermissionMask;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class AbstractResourceBuilder<Parent> {
    private final Resource.Builder mBuilder = new Resource.Builder();
    private final Parent mParent;

    public AbstractResourceBuilder(Parent parent) {
        mParent = parent;
    }

    public AbstractResourceBuilder withCreationDate(@Nullable Date creationDate) {
        mBuilder.withCreationDate(creationDate);
        return this;
    }

    public AbstractResourceBuilder withUpdateDate(@Nullable Date updateDate) {
        mBuilder.withUpdateDate(updateDate);
        return this;
    }

    public AbstractResourceBuilder withResourceType(@NotNull ResourceType resourceType) {
        mBuilder.withResourceType(resourceType);
        return this;
    }

    public AbstractResourceBuilder withLabel(@NotNull String label) {
        mBuilder.withLabel(label);
        return this;
    }

    public AbstractResourceBuilder withDescription(@Nullable String description) {
        mBuilder.withDescription(description);
        return this;
    }

    public AbstractResourceBuilder withUri(@NotNull String uri) {
        mBuilder.withUri(uri);
        return this;
    }

    public AbstractResourceBuilder withPermissionMask(@NotNull PermissionMask permissionMask) {
        mBuilder.withPermissionMask(permissionMask);
        return this;
    }

    public AbstractResourceBuilder withVersion(int version) {
        mBuilder.withVersion(version);
        return this;
    }

    public Resource.Builder getResourceBuilder() {
        return mBuilder;
    }

    public Parent done() {
        return mParent;
    }
}
