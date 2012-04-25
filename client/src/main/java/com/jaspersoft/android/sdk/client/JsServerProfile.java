/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.android.sdk.client;

/**
 * The <code>JsServerProfile</code> object represents an instance of a JasperReports Server
 * including authentication credentials.
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class JsServerProfile {
    private Long id;
    private String alias;
    private String serverUrl;
    private String organization;
    private String username;
    private String password;

    /**
     * Creates an empty JsServerProfile entity.
     */
    public JsServerProfile() {}

    /**
     * Creates a new JsServerProfile entity with the specified parameters.
     *
     * @param id           Unique identifier of this JsServerProfile. May be <code>null</code>.
     * @param alias        The name used to refer to this JsServerProfile. The alias is mainly used to display the name
     *                     of this JsServerProfile in UI (i.e. when displaying a list of available servers).
     * @param serverUrl    The URL of JasperReports Server. The url does not include the /core/ portion of the uri,
     *                     i.e. http://hostname:port/jasperserver
     * @param organization The name of an organization (used in JasperReport Server Professional which supports multi-tenancy).
     *                     May be <code>null</code> or empty.
     * @param username     The username, must be a valid account on JasperReports Server.
     * @param password     The account password
     */
    public JsServerProfile(Long id, String alias, String serverUrl, String organization, String username, String password) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
