package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class RepositoryDestination {
    private final String mFolderUri;

    RepositoryDestination(String folderUri) {
        mFolderUri = folderUri;
    }

    @NotNull
    public String getFolderUri() {
        return mFolderUri;
    }

    public static class Builder extends NestedBuilder<JobForm.Builder, RepositoryDestination> {
        private final JobForm.Builder mParent;
        private String mFolderUri;

        public Builder(JobForm.Builder parent) {
            mParent = parent;
        }

        public Builder withFolderUri(@NotNull String folderUri) {
            mFolderUri = Preconditions.checkNotNull(folderUri, "Repository folder uri should not be null");
            return this;
        }

        @Override
        public JobForm.Builder done() {
            return mParent;
        }

        @Override
        public RepositoryDestination build() {
            return new RepositoryDestination(mFolderUri);
        }

        void assertState() {
            Preconditions.checkNotNull(mFolderUri, "Job can not be scheduled without folder uri");
        }
    }
}
