package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class JobOwner {
    private static final String SEPARATOR = "|";

    @NotNull
    private final String mUsername;
    @Nullable
    private final String mOrganization;

    @TestOnly
    JobOwner(@NotNull String username, String organization) {
        mUsername = username;
        mOrganization = organization;
    }

    public static JobOwner newOwner(@NotNull String username, @Nullable String organization) {
        Preconditions.checkNotNull(username, "Username should not be null");
        return new JobOwner(username, organization);
    }

    @NotNull
    public String getUsername() {
        return mUsername;
    }

    @Nullable
    public String getOrganization() {
        return mOrganization;
    }

    @Override
    public String toString() {
        if (mOrganization == null) {
            return mUsername;
        }
        return mUsername + SEPARATOR + mOrganization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobOwner jobOwner = (JobOwner) o;

        if (mOrganization != null ? !mOrganization.equals(jobOwner.mOrganization) : jobOwner.mOrganization != null)
            return false;
        if (!mUsername.equals(jobOwner.mUsername)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mUsername.hashCode();
        result = 31 * result + (mOrganization != null ? mOrganization.hashCode() : 0);
        return result;
    }
}
