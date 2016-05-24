/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.Nullable;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class JobOutputFtpInfo {
    private final Type type;
    private final Prot prot;
    private final Protocol protocol;
    private final Integer protectionBufferSize;
    private final Integer port;
    private final Boolean implicit;
    private final String password;
    private final String userName;
    private final String folderPath;
    private final String serverName;

    JobOutputFtpInfo(Builder builder) {
        type = builder.type;
        prot = builder.prot;
        protocol = builder.protocol;
        protectionBufferSize = builder.protectionBufferSize;
        port = builder.port;
        implicit = builder.implicit;
        password = builder.password;
        userName = builder.userName;
        folderPath = builder.folderPath;
        serverName = builder.serverName;
    }

    public enum Type {
        FTP, FTPS, SFTP
    }

    public enum Protocol {
        SSL, TlS;
    }

    public enum Prot {
        CLEAR("C"), SAFE("S"), CONFIDENTIAL("E"), PRIVATE("P");

        private final String value;

        Prot(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Prot valueOfEntity(String stringProt) {
            for (Prot prot : values()) {
                boolean equals = prot.getValue().equals(stringProt);
                if (equals) {
                    return prot;
                }

            }
            throw new IllegalArgumentException("There is no PROT association with value: " + stringProt);
        }
    }

    public Prot getProt() {
        return prot;
    }

    public Type getType() {
        return type;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Integer getProtectionBufferSize() {
        return protectionBufferSize;
    }

    public Integer getPort() {
        return port;
    }

    public Boolean getImplicit() {
        return implicit;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobOutputFtpInfo that = (JobOutputFtpInfo) o;

        if (folderPath != null ? !folderPath.equals(that.folderPath) : that.folderPath != null) return false;
        if (implicit != null ? !implicit.equals(that.implicit) : that.implicit != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (prot != that.prot) return false;
        if (protectionBufferSize != null ? !protectionBufferSize.equals(that.protectionBufferSize) : that.protectionBufferSize != null)
            return false;
        if (protocol != that.protocol) return false;
        if (serverName != null ? !serverName.equals(that.serverName) : that.serverName != null) return false;
        if (type != that.type) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (prot != null ? prot.hashCode() : 0);
        result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
        result = 31 * result + (protectionBufferSize != null ? protectionBufferSize.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (implicit != null ? implicit.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (folderPath != null ? folderPath.hashCode() : 0);
        result = 31 * result + (serverName != null ? serverName.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private Type type;
        private Prot prot;
        private Protocol protocol;
        private Integer protectionBufferSize;
        private Integer port;
        private Boolean implicit;
        private String password;
        private String userName;
        private String folderPath;
        private String serverName;

        public Builder() {
        }

        Builder(JobOutputFtpInfo info) {
            type = info.type;
            prot = info.prot;
            protocol = info.protocol;
            protectionBufferSize = info.protectionBufferSize;
            port = info.port;
            implicit = info.implicit;
            password = info.password;
            userName = info.userName;
            folderPath = info.folderPath;
            serverName = info.serverName;
        }

        /**
         * FTP type
         *
         * @param type Supported values: "ftp", "ftps". Defalut: "ftps"
         * @return builder for convenient configuration
         */
        public Builder withType(@Nullable Type type) {
            this.type = type;
            return this;
        }

        /**
         * Return PROT command.
         *
         * @param prot Supported values: C,S,E,P
         * @return builder for convenient configuration
         */
        public Builder withProt(@Nullable Prot prot) {
            this.prot = prot;
            return this;
        }

        /**
         * Specifies the protocol of the ftp site. The secure socket protocol to be used, e.g. SSL/TLS.
         *
         * @param protocol Supported values: SSL/TLS.
         * @return builder for convenient configuration
         */
        public Builder withProtocol(@Nullable Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        /**
         * Specifies Protection Buffer Size value
         *
         * @param bufferSize Supported values: 0 to (2^32)-1 decimal integer. Default: 0
         * @return builder for convenient configuration
         */
        public Builder withProtectionBufferSize(@Nullable Integer bufferSize) {
            this.protectionBufferSize = bufferSize;
            return this;
        }

        /**
         * Specifies the port number of the ftp site
         *
         * @param port Supported values: 0 to (2^32)-1 decimal integer. Default: if type == ftps, then 990, else - 21
         * @return builder for convenient configuration
         */
        public Builder withPort(@Nullable Integer port) {
            this.port = port;
            return this;
        }

        /**
         * Specifies the security mode for FTPS (Implicit/ Explicit) If isImplicit is true, the default port is set to 990.
         *
         * @param flag Supported values: true, false. Default: true
         * @return builder for convenient configuration
         */
        public Builder withImplicit(@Nullable Boolean flag) {
            this.implicit = flag;
            return this;
        }

        /**
         * The login password for the FTP server
         *
         * @param password secret
         * @return builder for convenient configuration
         */
        public Builder withPassword(@Nullable String password) {
            this.password = password;
            return this;
        }

        /**
         * The login user name for the FTP server
         *
         * @param userName secret
         * @return builder for convenient configuration
         */
        public Builder withUserName(@Nullable String userName) {
            this.userName = userName;
            return this;
        }

        /**
         * The path of the folder under which job output resources would be created
         *
         * @param path uri of folder resource
         * @return builder for convenient configuration
         */
        public Builder withFolderPath(@Nullable String path) {
            this.folderPath = path;
            return this;
        }

        /**
         * The server name for the ftp site. I.e. host name
         * @param serverName I.e. host name
         * @return builder for convenient configuration
         */
        public Builder withServerName(@Nullable String serverName) {
            this.serverName = serverName;
            return this;
        }

        public JobOutputFtpInfo build() {
            if (port != null) {
                Preconditions.checkArgument(port >= 0, "Port should be in range of 0 and 2^32 - 1");
            }
            return new JobOutputFtpInfo(this);
        }
    }
}
