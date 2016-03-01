package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class RepositoryDestination {
    private final String mFolderUri;

    RepositoryDestination(String folderUri) {
        mFolderUri = folderUri;
    }

    @NotNull
    public String getFolderUri() {
        return mFolderUri;
    }

    public static class Builder {
        private String mFolderUri;

        /**
         * Allows to specify location where generated resource will reside
         *
         * @param folderUri unique identifier of folder on JRS side
         * @return builder for convenient configuration
         */
        public Builder withFolderUri(@NotNull String folderUri) {
            mFolderUri = Preconditions.checkNotNull(folderUri, "Repository folder uri should not be null");
            return this;
        }

        public RepositoryDestination build() {
            assertState();
            return new RepositoryDestination(mFolderUri);
        }

        private void assertState() {
            Preconditions.checkNotNull(mFolderUri, "Repository destination can not be built without folder uri");
        }
    }
}
