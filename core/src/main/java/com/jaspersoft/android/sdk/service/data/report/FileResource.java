package com.jaspersoft.android.sdk.service.data.report;

import com.jaspersoft.android.sdk.service.data.repository.PermissionMask;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class FileResource extends Resource {
    private final Type mType;

    private FileResource(
            @Nullable Date creationDate,
            @Nullable Date updateDate,
            @NotNull ResourceType resourceType,
            @NotNull String label,
            @NotNull String description,
            @NotNull String uri,
            @NotNull PermissionMask permissionMask,
            int version,
            Type type
    ) {
        super(creationDate, updateDate, resourceType, label, description, uri, permissionMask, version);
        mType = type;
    }

    public Type getType() {
        return mType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FileResource that = (FileResource) o;

        if (mType != that.mType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (mType != null ? mType.hashCode() : 0);
        return result;
    }

    public enum Type {
        pdf,
        html,
        xls,
        rtf,
        csv,
        odt,
        txt,
        docx,
        ods,
        xlsx,
        font,
        img,
        jrxml,
        jar,
        prop,
        jrtx,
        xml,
        css,
        accessGrantSchema,
        olapMondrianSchema,
        json,
        pptx,
        unknown
    }

    public static class Builder {
        private final AbstractResourceBuilder<Builder> mResourceBuilder;
        private Type fileType;

        public Builder() {
            mResourceBuilder = new AbstractResourceBuilder<>(this);
        }

        public AbstractResourceBuilder addResource() {
            return mResourceBuilder;
        }

        public Builder withFileType(Type type) {
            this.fileType = Preconditions.checkNotNull(type, "Filer type == null");
            return this;
        }

        public FileResource build() {
            Preconditions.checkNotNull(fileType, "Can not create file resource without file type");
            Resource resource = mResourceBuilder.getResourceBuilder().build();
            return new FileResource(
                    resource.getCreationDate(),
                    resource.getUpdateDate(),
                    resource.getResourceType(),
                    resource.getLabel(),
                    resource.getDescription(),
                    resource.getUri(),
                    resource.getPermissionMask(),
                    resource.getVersion(),
                    fileType
            );
        }
    }
}
