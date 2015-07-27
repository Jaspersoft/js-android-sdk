/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.android.sdk.client.oxm;

import android.util.Log;

import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a resource descriptor entity for convenient XML serialization process.
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */

@Root(strict=false)
public class ResourceDescriptor {

    /**
     * These constants are copied here from DataType for facility
     */
    public static final byte DT_TYPE_TEXT = 1;
    public static final byte DT_TYPE_NUMBER = 2;
    public static final byte DT_TYPE_DATE = 3;
    public static final byte DT_TYPE_DATE_TIME = 4;

    /**
     * These constants are copied here from InputControl for facility
     */
    public static final byte IC_TYPE_BOOLEAN = 1;
    public static final byte IC_TYPE_SINGLE_VALUE = 2;
    public static final byte IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES = 3;
    public static final byte IC_TYPE_SINGLE_SELECT_QUERY = 4;
    public static final byte IC_TYPE_MULTI_VALUE = 5;
    public static final byte IC_TYPE_MULTI_SELECT_LIST_OF_VALUES = 6;
    public static final byte IC_TYPE_MULTI_SELECT_QUERY = 7;

    public static final byte IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO = 8;
    public static final byte IC_TYPE_SINGLE_SELECT_QUERY_RADIO = 9;
    public static final byte IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX = 10;
    public static final byte IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX = 11;

    public static final String PROP_VERSION = "PROP_VERSION";
    public static final String PROP_PARENT_FOLDER = "PROP_PARENT_FOLDER";
    public static final String PROP_RESOURCE_TYPE = "PROP_RESOURCE_TYPE";
    public static final String PROP_CREATION_DATE = "PROP_CREATION_DATE";

    // File resource properties
    public static final String PROP_FILERESOURCE_HAS_DATA = "PROP_HAS_DATA";
    public static final String PROP_FILERESOURCE_IS_REFERENCE = "PROP_IS_REFERENCE";
    public static final String PROP_FILERESOURCE_REFERENCE_URI = "PROP_REFERENCE_URI";
    public static final String PROP_FILERESOURCE_WSTYPE = "PROP_WSTYPE";
    public static final String PROP_DATA = "PROP_DATA";
    public static final String PROP_DATASOURCE_MAPPING = "DATASOURCE_MAPPING";

    // Datasource properties
    public static final String PROP_DATASOURCE_DRIVER_CLASS = "PROP_DATASOURCE_DRIVER_CLASS";
    public static final String PROP_DATASOURCE_CONNECTION_URL = "PROP_DATASOURCE_CONNECTION_URL";
    public static final String PROP_DATASOURCE_USERNAME = "PROP_DATASOURCE_USERNAME";
    public static final String PROP_DATASOURCE_PASSWORD = "PROP_DATASOURCE_PASSWORD";
    public static final String PROP_DATASOURCE_JNDI_NAME = "PROP_DATASOURCE_JNDI_NAME";
    public static final String PROP_DATASOURCE_BEAN_NAME = "PROP_DATASOURCE_BEAN_NAME";
    public static final String PROP_DATASOURCE_BEAN_METHOD = "PROP_DATASOURCE_BEAN_METHOD";
    //
    public static final String PROP_DATASOURCE_CUSTOM_SERVICE_CLASS = "PROP_DATASOURCE_CUSTOM_SERVICE_CLASS";
    public static final String PROP_DATASOURCE_CUSTOM_PROPERTY_MAP = "PROP_DATASOURCE_CUSTOM_PROPERTY_MAP";


    // ReportUnit resource properties
    public static final String PROP_RU_DATASOURCE_TYPE = "PROP_RU_DATASOURCE_TYPE";
    public static final String PROP_RU_IS_MAIN_REPORT = "PROP_RU_IS_MAIN_REPORT";
    public static final String PROP_RU_INPUTCONTROL_RENDERING_VIEW = "PROP_RU_INPUTCONTROL_RENDERING_VIEW";
    public static final String PROP_RU_REPORT_RENDERING_VIEW = "PROP_RU_REPORT_RENDERING_VIEW";
    public static final String PROP_RU_ALWAYS_PROPMT_CONTROLS = "PROP_RU_ALWAYS_PROPMT_CONTROLS";
    public static final String PROP_RU_CONTROLS_LAYOUT = "PROP_RU_CONTROLS_LAYOUT";

    public static final byte RU_CONTROLS_LAYOUT_POPUP_SCREEN = 1;
    public static final byte RU_CONTROLS_LAYOUT_SEPARATE_PAGE = 2;
    public static final byte RU_CONTROLS_LAYOUT_TOP_OF_PAGE = 3;
    public static final byte RU_CONTROLS_LAYOUT_IN_PAGE = 4;

    // DataType resource properties
    public static final String PROP_DATATYPE_STRICT_MAX = "PROP_DATATYPE_STRICT_MAX";
    public static final String PROP_DATATYPE_STRICT_MIN = "PROP_DATATYPE_STRICT_MIN";
    public static final String PROP_DATATYPE_MIN_VALUE = "PROP_DATATYPE_MIN_VALUE";
    public static final String PROP_DATATYPE_MAX_VALUE = "PROP_DATATYPE_MAX_VALUE";
    public static final String PROP_DATATYPE_PATTERN = "PROP_DATATYPE_PATTERN";
    public static final String PROP_DATATYPE_TYPE = "PROP_DATATYPE_TYPE";

    // ListOfValues resource properties
    public static final String PROP_LOV = "PROP_LOV";
    public static final String PROP_LOV_LABEL = "PROP_LOV_LABEL";
    public static final String PROP_LOV_VALUE = "PROP_LOV_VALUE";


    // InputControl resource properties
    public static final String PROP_INPUTCONTROL_TYPE = "PROP_INPUTCONTROL_TYPE";
    public static final String PROP_INPUTCONTROL_IS_MANDATORY = "PROP_INPUTCONTROL_IS_MANDATORY";
    public static final String PROP_INPUTCONTROL_IS_READONLY = "PROP_INPUTCONTROL_IS_READONLY";
    public static final String PROP_INPUTCONTROL_IS_VISIBLE = "PROP_INPUTCONTROL_IS_VISIBLE";

    // SQL resource properties
    public static final String PROP_QUERY = "PROP_QUERY";
    public static final String PROP_QUERY_VISIBLE_COLUMNS = "PROP_QUERY_VISIBLE_COLUMNS";
    public static final String PROP_QUERY_VISIBLE_COLUMN_NAME = "PROP_QUERY_VISIBLE_COLUMN_NAME";
    public static final String PROP_QUERY_VALUE_COLUMN = "PROP_QUERY_VALUE_COLUMN";
    public static final String PROP_QUERY_LANGUAGE = "PROP_QUERY_LANGUAGE";


    // SQL resource properties
    public static final String PROP_QUERY_DATA = "PROP_QUERY_DATA";
    public static final String PROP_QUERY_DATA_ROW = "PROP_QUERY_DATA_ROW";
    public static final String PROP_QUERY_DATA_ROW_COLUMN = "PROP_QUERY_DATA_ROW_COLUMN";


    // OLAP XMLA Connection
    public static final String PROP_XMLA_URI = "PROP_XMLA_URI";
    public static final String PROP_XMLA_CATALOG = "PROP_XMLA_CATALOG";
    public static final String PROP_XMLA_DATASOURCE = "PROP_XMLA_DATASOURCE";
    public static final String PROP_XMLA_USERNAME = "PROP_XMLA_USERNAME";
    public static final String PROP_XMLA_PASSWORD = "PROP_XMLA_PASSWORD";

    // OLAP Unit
    public static final String PROP_MDX_QUERY = "PROP_MDX_QUERY";

    public enum WsType {
        accessGrantSchema,
        adhocDataView,
        adhocReport,
        aws,
        bean,
        contentResource,
        css,
        custom,
        datasource,
        dataType,
        dashboard,
        dashboardState,
        domain,
        domainTopic,
        folder,
        font,
        img,
        inputControl,
        jar,
        jdbc,
        jndi,
        jrxml,
        lov,
        olapMondrianCon,
        olapMondrianSchema,
        olapUnit,
        olapXmlaCon,
        prop,
        query,
        reference,
        reportOptions,
        reportUnit,
        virtual,
        xml,
        xmlaConnection,
        unknow
    }

    // Convenient TAG for logging purposes
    private static final String TAG = "ResourceDescriptor";

    @Expose
    @Attribute(required=false)
    private String name;
    @Expose
    @Attribute
    private String wsType;
    @Expose
    @Attribute(required=false)
    private String uriString;
    @Expose
    @Attribute(required=false)
    private Boolean isNew;

    @Expose
    @Element(required=false)
    private String label;
    @Element(required=false)
    @Expose
    private String description;
    @Expose
    @Element(required=false)
    private String creationDate;

    @Expose
    @ElementList(entry="resourceProperty", inline=true, required=false, empty=false)
    private List<ResourceProperty> properties = new ArrayList<ResourceProperty>();

    @Expose
    @ElementList(entry="resourceDescriptor", inline=true, required=false, empty=false)
    private List<ResourceDescriptor> internalResources = new ArrayList<ResourceDescriptor>();

    @Expose
    @ElementList(entry="parameter", inline=true, required=false, empty=false)
    private List<ResourceParameter> parameters = new ArrayList<ResourceParameter>();


    /**
     * Looks the wsType of the resource descriptor and return true
     * if it is one of the following: datasource, jdbc, jndi, bean, custom.
     * @return <code>true</code> if it is data source, <code>false</code> otherwise.
     */
    public boolean isDataSource() {
        switch (getWsType()) {
            case datasource:
            case jdbc:
            case jndi:
            case bean:
            case custom:
                return true;
            default:
                return false;
        }
    }

    /**
     * Gets a valid data source resource from a resource descriptor
     * @return the data source resource, or <code>null</code> if no data source is found.
     */
    public ResourceDescriptor getDataSource() {
        for (ResourceDescriptor internalResource : getInternalResources()) {
            if (internalResource.isDataSource()) {
                return internalResource;
            }
        }
        return null;
    }

    /**
     * Gets a valid data source uri from a resource descriptor
     * @return the data source uri, or <code>null</code> if no data source is found.
     */
    public String getDataSourceUri() {
        for (ResourceDescriptor internalResource : getInternalResources()) {
            switch (internalResource.getWsType()) {
                case datasource:
                    return internalResource.getPropertyByName(PROP_FILERESOURCE_REFERENCE_URI).getValue();
                case jdbc:
                case jndi:
                case bean:
                case custom:
                    return internalResource.getUriString();
            }
        }
        // TODO: data source references
        return null;
    }

    /**
     * Gets the property with the specified name
     * @param name the property name
     * @return property with the specified name, or <code>null</code> if the property is not found
     */
    public ResourceProperty getPropertyByName(String name) {
        for (ResourceProperty property : getProperties()) {
            if (name.equals(property.getName())) {
                return property;
            }
        }
        return null;
    }

    /**
     * Gets the list of internal resources with the specified type
     * @param type the resource type
     * @return the list of resources that have the specified type
     */
    public List<ResourceDescriptor> getInternalResourcesByType(WsType type) {
        List<ResourceDescriptor> resourcesByType = new ArrayList<ResourceDescriptor>();
        for(ResourceDescriptor resource : this.getInternalResources()) {
            if(this.getWsType() == type) {
                resourcesByType.add(resource);
            }
        }
        return resourcesByType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WsType getWsType() {
        try {
            return WsType.valueOf(wsType);
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, wsType + " is not a constant in WsType enum");
            return WsType.unknow;
        }
    }

    public void setWsType(WsType wsType) {
        this.wsType = wsType.toString();
    }

    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<ResourceProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ResourceProperty> properties) {
        this.properties = properties;
    }

    public List<ResourceDescriptor> getInternalResources() {
        return internalResources;
    }

    public void setInternalResources(List<ResourceDescriptor> internalResources) {
        this.internalResources = internalResources;
    }

    public List<ResourceParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ResourceParameter> parameters) {
        this.parameters = parameters;
    }
}