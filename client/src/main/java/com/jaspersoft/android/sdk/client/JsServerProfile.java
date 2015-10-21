/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.client;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The <code>JsServerProfile</code> object represents an instance of a JasperReports Server
 * including authentication credentials.
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class JsServerProfile implements Parcelable {
    private long id;
    private String alias;
    private String serverUrl;
    private String organization;
    private String username;
    private String password;
    private String versionCode;
    private String serverEdition;

    /**
     * Creates an empty JsServerProfile entity.
     */
    public JsServerProfile() {}

    /**
     * Creates a new JsServerProfile entity with the specified parameters.
     *
     * @param alias        The name used to refer to this JsServerProfile. The alias is mainly used to display the name
     *                     of this JsServerProfile in UI (e.g. when displaying a list of available servers).
     * @param serverUrl    The URL of JasperReports Server. The url does not include the /core/ portion of the uri,
     *                     e.g. http://hostname:port/jasperserver
     * @param organization The name of an organization (used in JasperReport Server Professional which supports multi-tenancy).
     *                     May be <code>null</code> or empty.
     * @param username     The username, must be a valid account on JasperReports Server.
     * @param password     The account password
     */
    public JsServerProfile(String alias, String serverUrl, String organization, String username, String password) {
        this(0, alias, serverUrl, organization, username, password);
    }

    /**
     * Creates a new JsServerProfile entity with the specified parameters.
     *
     * @param id           Unique identifier of this JsServerProfile. May be <code>null</code>.
     * @param alias        The name used to refer to this JsServerProfile. The alias is mainly used to display the name
     *                     of this JsServerProfile in UI (e.g. when displaying a list of available servers).
     * @param serverUrl    The URL of JasperReports Server. The url does not include the /core/ portion of the uri,
     *                     e.g. http://hostname:port/jasperserver
     * @param organization The name of an organization (used in JasperReport Server Professional which supports multi-tenancy).
     *                     May be <code>null</code> or empty.
     * @param username     The username, must be a valid account on JasperReports Server.
     * @param password     The account password
     */
    public JsServerProfile(long id, String alias, String serverUrl, String organization, String username, String password) {
        setId(id);
        setAlias(alias);
        setServerUrl(serverUrl);
        setOrganization(organization);
        setUsername(username);
        setPassword(password);
    }

    public String getUsernameWithOrgId() {
        if (organization != null && organization.length() > 0) {
            return username + "|" + organization;
        } else {
            return username;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerEdition() {
        return serverEdition;
    }

    public void setServerEdition(String serverEdition) {
        this.serverEdition = serverEdition;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsServerProfile that = (JsServerProfile) o;

        if (id != that.id) return false;
        if (!alias.equals(that.alias)) return false;
        if (!serverUrl.equals(that.serverUrl)) return false;
        if (organization != null ? !organization.equals(that.organization) : that.organization != null)
            return false;
        if (!username.equals(that.username)) return false;
        if (!password.equals(that.password)) return false;
        if (versionCode != null ? !versionCode.equals(that.versionCode) : that.versionCode != null)
            return false;
        return !(serverEdition != null ? !serverEdition.equals(that.serverEdition) : that.serverEdition != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + alias.hashCode();
        result = 31 * result + serverUrl.hashCode();
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (versionCode != null ? versionCode.hashCode() : 0);
        result = 31 * result + (serverEdition != null ? serverEdition.hashCode() : 0);
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.alias);
        dest.writeString(this.serverUrl);
        dest.writeString(this.organization);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.versionCode);
        dest.writeString(this.serverEdition);
    }

    protected JsServerProfile(Parcel in) {
        this.id = in.readLong();
        this.alias = in.readString();
        this.serverUrl = in.readString();
        this.organization = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.versionCode = in.readString();
        this.serverEdition = in.readString();
    }

    public static final Creator<JsServerProfile> CREATOR = new Creator<JsServerProfile>() {
        public JsServerProfile createFromParcel(Parcel source) {
            return new JsServerProfile(source);
        }

        public JsServerProfile[] newArray(int size) {
            return new JsServerProfile[size];
        }
    };
}
