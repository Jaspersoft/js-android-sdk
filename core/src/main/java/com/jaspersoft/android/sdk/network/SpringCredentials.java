package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import static com.jaspersoft.android.sdk.service.internal.Preconditions.checkNotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class SpringCredentials extends Credentials {
    private final String mUsername;
    private final String mPassword;
    private final String mOrganization;
    private final Locale mLocale;
    private final TimeZone mTimeZone;

    @TestOnly
    SpringCredentials(
            @NotNull String username,
            @NotNull String password,
            @Nullable String organization,
            @NotNull Locale locale,
            @NotNull TimeZone timeZone) {
        mUsername = username;
        mPassword = password;
        mOrganization = organization;
        mLocale = locale;
        mTimeZone = timeZone;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public String getUsername() {
        return mUsername;
    }

    @NotNull
    public String getPassword() {
        return mPassword;
    }

    @Nullable
    public String getOrganization() {
        return mOrganization;
    }

    @NotNull
    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    @NotNull
    public Locale getLocale() {
        return mLocale;
    }

    @Override
    protected Cookies applyPolicy(AuthPolicy policy) throws IOException, HttpException {
        return policy.applyCredentials(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpringCredentials that = (SpringCredentials) o;

        if (mUsername != null ? !mUsername.equals(that.mUsername) : that.mUsername != null)
            return false;
        if (mPassword != null ? !mPassword.equals(that.mPassword) : that.mPassword != null)
            return false;
        if (mOrganization != null ? !mOrganization.equals(that.mOrganization) : that.mOrganization != null)
            return false;
        if (mLocale != null ? !mLocale.equals(that.mLocale) : that.mLocale != null) return false;
        return !(mTimeZone != null ? !mTimeZone.equals(that.mTimeZone) : that.mTimeZone != null);
    }

    @Override
    public int hashCode() {
        int result = mUsername != null ? mUsername.hashCode() : 0;
        result = 31 * result + (mPassword != null ? mPassword.hashCode() : 0);
        result = 31 * result + (mOrganization != null ? mOrganization.hashCode() : 0);
        result = 31 * result + (mLocale != null ? mLocale.hashCode() : 0);
        result = 31 * result + (mTimeZone != null ? mTimeZone.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpringCredentials{" +
                "mLocale=" + mLocale +
                ", mUsername='" + mUsername + '\'' +
                ", mOrganization='" + mOrganization + '\'' +
                ", mTimeZone=" + mTimeZone +
                '}';
    }

    public static class Builder {
        private String mUsername;
        private String mPassword;
        private String mOrganization;

        // Optional
        private Locale mLocale;
        private TimeZone mTimeZone;

        private Builder() {}

        public Builder username(@NotNull String username) {
            mUsername = checkNotNull(username, "username == null");
            return this;
        }

        public Builder password(@NotNull String password) {
            mPassword = checkNotNull(password, "password == null");
            return this;
        }

        public Builder organization(@Nullable String organization) {
            mOrganization = organization;
            return this;
        }

        public Builder timeZone(@NotNull TimeZone timeZone) {
            mTimeZone = checkNotNull(timeZone, "timeZone == null");
            return this;
        }

        public Builder locale(@NotNull Locale locale) {
            mLocale = checkNotNull(locale, "locale == null");
            return this;
        }

        @NotNull
        public SpringCredentials build() {
            ensureValidState();
            ensureDefaults();
            return new SpringCredentials(
                    mUsername,
                    mPassword,
                    mOrganization,
                    mLocale,
                    mTimeZone);
        }

        private void ensureDefaults() {
            if (mTimeZone == null) {
                mTimeZone = TimeZone.getDefault();
            }
            if (mLocale == null) {
                mLocale = Locale.getDefault();
            }
        }

        private void ensureValidState() {
            if (mUsername == null) {
                throw new IllegalStateException("Username should not be null");
            }
            if (mPassword == null) {
                throw new IllegalStateException("Password should not be null");
            }
        }
    }
}
