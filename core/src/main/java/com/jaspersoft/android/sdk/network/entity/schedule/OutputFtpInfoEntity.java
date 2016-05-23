package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class OutputFtpInfoEntity {
    @Expose
    private String type;
    @Expose
    private String prot;
    @Expose
    private Integer pbsz;
    @Expose
    private Integer port;
    @Expose
    private String protocol;
    @Expose
    private Boolean implicit;
    @Expose
    private String password;
    @Expose
    private String userName;
    @Expose
    private String folderPath;
    @Expose
    private String serverName;

    public OutputFtpInfoEntity() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProt() {
        return prot;
    }

    public void setProt(String prot) {
        this.prot = prot;
    }

    public Integer getProtectionBufferSize() {
        return pbsz;
    }

    public void setProtectionBufferSize(Integer pbsz) {
        this.pbsz = pbsz;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Boolean getImplicit() {
        return implicit;
    }

    public void setImplicit(Boolean implicit) {
        this.implicit = implicit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
