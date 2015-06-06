package com.jaspersoft.android.sdk.integration.utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class ServerUnderTest {
    private final String alias;
    private final String organization;
    private final String serverUrl;
    private final String username;
    private final String password;

    private ServerUnderTest(Builder builder) {
        this.alias = builder.alias;
        this.organization = builder.organization;
        this.serverUrl = builder.serverUrl;
        this.username = builder.username;
        this.password = builder.password;
    }

    public static ServerUnderTest createDefault() {
        return builder()
                .withDefaultAlias()
                .withDefaultOrganization()
                .withDefaultServerUrl()
                .withDefaultUsername()
                .withDefaultPassword()
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getAlias() {
        return alias;
    }

    public String getOrganization() {
        return organization;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder {
        private String alias;
        private String organization;
        private String serverUrl;
        private String username;
        private String password;

        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder setOrganization(String organization) {
            this.organization = organization;
            return this;
        }

        public Builder setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String pass) {
            this.password = pass;
            return this;
        }

        public Builder withDefaultAlias() {
            this.alias = "Mobile Demo";
            return this;
        }

        public Builder withDefaultOrganization() {
            this.organization = "organization_1";
            return this;
        }

        public Builder withDefaultServerUrl() {
            this.serverUrl = "http://mobiledemo.jaspersoft.com/jasperserver-pro";
            return this;
        }

        public Builder withDefaultUsername() {
            this.username = "phoneuser";
            return this;
        }

        public Builder withDefaultPassword() {
            this.password = "phoneuser";
            return this;
        }

        public ServerUnderTest build() {
            checkValues();
            return new ServerUnderTest(this);
        }

        private void checkValues() {
            assertProperty(alias, "alias");
            assertProperty(organization, "organization");
            assertProperty(serverUrl, "serverUrl");
            assertProperty(username, "username");
            assertProperty(password, "password");
            serverUrl = trimUrl(serverUrl);
            try {
                new URL(serverUrl);
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        }

        private void assertProperty(String property, String propertyName) {
            if (isEmpty(property)) {
                throw new IllegalStateException(
                        propertyName + " invalid should not be: " + String.valueOf(property));
            }
        }

        private static String trimUrl(String url) {
            if (!isEmpty(url) && url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            return url;
        }

        private static boolean isEmpty(String str) {
            if (str == null || str.trim().length() == 0)
                return true;
            else
                return false;
        }
    }
}
