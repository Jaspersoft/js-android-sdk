package com.jaspersoft.android.sdk.service.data.report.option;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class ReportOption {
    @NotNull
    private final String id;
    @NotNull
    private final String uri;
    @NotNull
    private final String label;

    private ReportOption(@NotNull String id, @NotNull String uri, @NotNull String label) {
        this.id = id;
        this.uri = uri;
        this.label = label;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getUri() {
        return uri;
    }

    @NotNull
    public String getLabel() {
        return label;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportOption)) return false;

        ReportOption that = (ReportOption) o;

        if (!id.equals(that.id)) return false;
        if (!label.equals(that.label)) return false;
        if (!uri.equals(that.uri)) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = id.hashCode();
        result = 31 * result + uri.hashCode();
        result = 31 * result + label.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ReportOption{" +
                "id='" + id + '\'' +
                ", uri='" + uri + '\'' +
                ", label='" + label + '\'' +
                '}';
    }

    public static class Builder {
        private String mId;
        private String mUri;
        private String mLabel;

        public Builder withId(String id) {
            mId = Preconditions.checkNotNull(id, "Id should not be null");
            return this;
        }

        public Builder withUri(String uri) {
            mUri = Preconditions.checkNotNull(uri, "Uri should not be null");
            return this;
        }

        public Builder withLabel(String label) {
            mLabel = Preconditions.checkNotNull(label, "Label should not be null");
            return this;
        }

        public ReportOption build() {
            Preconditions.checkNotNull(mId, "Report option can not be created without id");
            Preconditions.checkNotNull(mUri, "Report option can not be created without uri");
            Preconditions.checkNotNull(mLabel, "Report option can not be created without label");
            return new ReportOption(mId, mUri, mLabel);
        }
    }
}
