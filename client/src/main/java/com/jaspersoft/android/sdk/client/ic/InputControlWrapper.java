/*
 * Copyright (C) 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jasperforge.org/projects/androidsdk
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.ic;

import android.view.View;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;
import com.jaspersoft.android.sdk.client.oxm.ResourceParameter;
import com.jaspersoft.android.sdk.client.oxm.ResourceProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a helper class for working with input control entities, independent of type and UI appearance.
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class InputControlWrapper {

    public static final String NULL_SUBSTITUTE = "~NULL~";
    public static final String NULL_SUBSTITUTE_LABEL = "[Null]";
    public static final String NOTHING_SUBSTITUTE = "~NOTHING~";
    public static final String NOTHING_SUBSTITUTE_LABEL = "---";

    private String name;
    private String label;
    private String uri;
    private byte type;
    private byte dataType;
    private boolean isMandatory = false;
    private boolean isReadOnly = false;
    private boolean isVisible= true;

    private String query;
    private String dataSourceUri;
    private List<String> parameterDependencies = new ArrayList<String>();

    private List<InputControlWrapper> masterDependencies = new ArrayList<InputControlWrapper>();
    private List<InputControlWrapper> slaveDependencies = new ArrayList<InputControlWrapper>();

    private List<ResourceProperty> listOfValues = new ArrayList<ResourceProperty>();
    private List<ResourceParameter> listOfSelectedValues = new ArrayList<ResourceParameter>();
    
    private View inputView;
    private View errorView;


    public InputControlWrapper(ResourceDescriptor resourceDescriptor){
        name = resourceDescriptor.getName();
        label = resourceDescriptor.getLabel();
        uri = resourceDescriptor.getUriString();

        // init fields from resource properties
        for (ResourceProperty property : resourceDescriptor.getProperties()){
            String name = property.getName();
            if (ResourceDescriptor.PROP_INPUTCONTROL_TYPE.equals(name)) {
                type = Byte.parseByte(property.getValue());
            } else if (ResourceDescriptor.PROP_INPUTCONTROL_IS_MANDATORY.equals(name)) {
                isMandatory = Boolean.parseBoolean(property.getValue());
            } else if (ResourceDescriptor.PROP_INPUTCONTROL_IS_READONLY.equals(name)) {
                isReadOnly = Boolean.parseBoolean(property.getValue());
            } else if (ResourceDescriptor.PROP_INPUTCONTROL_IS_VISIBLE.equals(name)) {
                isVisible = Boolean.parseBoolean(property.getValue());
            }
        }

        // get query and datasource uri if exist
        for(ResourceDescriptor internalResource : resourceDescriptor.getInternalResources()) {
            if(internalResource.getWsType() == ResourceDescriptor.WsType.query) {
                ResourceProperty queryProperty = internalResource.getPropertyByName(ResourceDescriptor.PROP_QUERY);
                this.query = queryProperty.getValue();
                this.dataSourceUri = internalResource.getDataSourceUri();
                break;
            }
        }

        // resolve data type for single value input control
        if (this.getType() == ResourceDescriptor.IC_TYPE_SINGLE_VALUE) {
            for(ResourceDescriptor internalResource : resourceDescriptor.getInternalResources()) {
                if(internalResource.getWsType() == ResourceDescriptor.WsType.dataType) {
                    ResourceProperty dataTypeProperty = internalResource.getPropertyByName(ResourceDescriptor.PROP_DATATYPE_TYPE);
                    this.dataType = Byte.parseByte(dataTypeProperty.getValue());
                    break;
                }
            }
        }

        // get parameters that input control depends on
        if (query != null) {
            // standard parameter
            Pattern sp = Pattern.compile("\\$P\\{\\s*([\\w]*)\\s*\\}");

            Matcher m = sp.matcher(query);
            while (m.find()) parameterDependencies.add(m.group(1));

            // include parameter
            Pattern ip = Pattern.compile("\\$P!\\{\\s*([\\w]*)\\s*\\}");

            m = ip.matcher(query);
            while (m.find()) parameterDependencies.add(m.group(1));

            // dynamic query parameter
            Pattern dqp = Pattern.compile("\\$X\\{[^{}]*,\\s*([\\w]*)\\s*\\}");

            m = dqp.matcher(query);
            while (m.find()) parameterDependencies.add(m.group(1));
        }

        // init input control values list
        switch (getType()) {
            case ResourceDescriptor.IC_TYPE_BOOLEAN:
            case ResourceDescriptor.IC_TYPE_SINGLE_VALUE:
                break;

            case ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES:
            case ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO:
            case ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES:
            case ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX:

                for (ResourceDescriptor  internalResource : resourceDescriptor.getInternalResources()) {
                    if (internalResource.getWsType() == ResourceDescriptor.WsType.lov) {
                        ResourceProperty propLov = internalResource.getPropertyByName(ResourceDescriptor.PROP_LOV);
                        listOfValues = propLov.getProperties();
                        break;
                    }
                }
                break;

            case ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY:
            case ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO:
            case ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY:
            case ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX:

                ResourceProperty queryDataProperty = resourceDescriptor.getPropertyByName(ResourceDescriptor.PROP_QUERY_DATA);

                if (queryDataProperty != null) {
                    List<ResourceProperty> queryData = queryDataProperty.getProperties();
                    // rows
                    for (ResourceProperty queryDataRow : queryData) {
                        ResourceProperty property = new ResourceProperty();
                        property.setName(queryDataRow.getValue());

                        //cols
                        StringBuilder value = new StringBuilder();
                        for(ResourceProperty queryDataCol : queryDataRow.getProperties()) {
                            if(ResourceDescriptor.PROP_QUERY_DATA_ROW_COLUMN.equals(queryDataCol.getName())) {
                                if (value.length() > 0) value.append(" | ");
                                value.append(queryDataCol.getValue());
                            }
                        }

                        property.setValue(value.toString());
                        listOfValues.add(property);
                    }
                }
                break;
        }

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getDataType() {
        return dataType;
    }

    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDataSourceUri() {
        return dataSourceUri;
    }

    public void setDataSourceUri(String dataSourceUri) {
        this.dataSourceUri = dataSourceUri;
    }

    public List<String> getParameterDependencies() {
        return parameterDependencies;
    }

    public void setParameterDependencies(List<String> parameterDependencies) {
        this.parameterDependencies = parameterDependencies;
    }

    public List<InputControlWrapper> getMasterDependencies() {
        return masterDependencies;
    }

    public void setMasterDependencies(List<InputControlWrapper> masterDependencies) {
        this.masterDependencies = masterDependencies;
    }

    public List<InputControlWrapper> getSlaveDependencies() {
        return slaveDependencies;
    }

    public void setSlaveDependencies(List<InputControlWrapper> slaveDependencies) {
        this.slaveDependencies = slaveDependencies;
    }

    public List<ResourceProperty> getListOfValues() {
        return listOfValues;
    }

    public void setListOfValues(List<ResourceProperty> listOfValues) {
        this.listOfValues = listOfValues;
    }

    public List<ResourceParameter> getListOfSelectedValues() {
        return listOfSelectedValues;
    }

    public void setListOfSelectedValues(List<ResourceParameter> listOfSelectedValues) {
        this.listOfSelectedValues = listOfSelectedValues;
    }

    public View getInputView() {
        return inputView;
    }

    public void setInputView(View inputView) {
        this.inputView = inputView;
    }

    public View getErrorView() {
        return errorView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }
}