package com.jaspersoft.android.sdk.service.data.schedule;

import org.jetbrains.annotations.Nullable;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class OutputFtpInfo {
    private final Type mType;
    private final Protocol mProtocol;
    private final Integer mProtectionBufferSize;
    private final Integer mPort;
    private final Boolean mImplicit;
    private final String mPassword;
    private final String mUserName;
    private final String mFolderPath;
    private final String mServerName;

    OutputFtpInfo(Builder builder) {
        mType = builder.type;
        mProtocol = builder.protocol;
        mProtectionBufferSize = builder.protectionBufferSize;
        mPort = builder.port;
        mImplicit = builder.implicit;
        mPassword = builder.password;
        mUserName = builder.userName;
        mFolderPath = builder.folderPath;
        mServerName = builder.serverName;
    }

    enum Type {
        FTP, FTPS
    }

    enum Protocol {
        CLEAR("C"), SAFE("S"), CONFIDENTIAL("E"), PRIVATE("P");

        private final String value;

        Protocol(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public Type getType() {
        return mType;
    }

    public Protocol getProtocol() {
        return mProtocol;
    }

    public Integer getProtectionBufferSize() {
        return mProtectionBufferSize;
    }

    public Integer getPort() {
        return mPort;
    }

    public Boolean getImplicit() {
        return mImplicit;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getFolderPath() {
        return mFolderPath;
    }

    public String getServerName() {
        return mServerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutputFtpInfo that = (OutputFtpInfo) o;

        if (mFolderPath != null ? !mFolderPath.equals(that.mFolderPath) : that.mFolderPath != null) return false;
        if (mImplicit != null ? !mImplicit.equals(that.mImplicit) : that.mImplicit != null) return false;
        if (mPassword != null ? !mPassword.equals(that.mPassword) : that.mPassword != null) return false;
        if (mPort != null ? !mPort.equals(that.mPort) : that.mPort != null) return false;
        if (mProtectionBufferSize != null ? !mProtectionBufferSize.equals(that.mProtectionBufferSize) : that.mProtectionBufferSize != null)
            return false;
        if (mProtocol != that.mProtocol) return false;
        if (mServerName != null ? !mServerName.equals(that.mServerName) : that.mServerName != null) return false;
        if (mType != that.mType) return false;
        if (mUserName != null ? !mUserName.equals(that.mUserName) : that.mUserName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mType != null ? mType.hashCode() : 0;
        result = 31 * result + (mProtocol != null ? mProtocol.hashCode() : 0);
        result = 31 * result + (mProtectionBufferSize != null ? mProtectionBufferSize.hashCode() : 0);
        result = 31 * result + (mPort != null ? mPort.hashCode() : 0);
        result = 31 * result + (mImplicit != null ? mImplicit.hashCode() : 0);
        result = 31 * result + (mPassword != null ? mPassword.hashCode() : 0);
        result = 31 * result + (mUserName != null ? mUserName.hashCode() : 0);
        result = 31 * result + (mFolderPath != null ? mFolderPath.hashCode() : 0);
        result = 31 * result + (mServerName != null ? mServerName.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private Type type;
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

        Builder(OutputFtpInfo info) {
            type = info.mType;
            protocol = info.mProtocol;
            protectionBufferSize = info.mProtectionBufferSize;
            port = info.mPort;
            implicit = info.mImplicit;
            password = info.mPassword;
            userName = info.mUserName;
            folderPath = info.mFolderPath;
            serverName = info.mServerName;
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

        public OutputFtpInfo build() {
            return new OutputFtpInfo(this);
        }
    }
}
