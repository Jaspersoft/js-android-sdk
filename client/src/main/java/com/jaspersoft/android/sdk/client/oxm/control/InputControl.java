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

package com.jaspersoft.android.sdk.client.oxm.control;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.jaspersoft.android.sdk.client.oxm.control.validation.ValidationRule;
import com.jaspersoft.android.sdk.client.oxm.control.validation.ValidationRulesList;
import com.jaspersoft.android.sdk.client.oxm.report.adapter.ValidationRulesListTypeAdapter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ivan Gadzhega
 * @since 1.4
 */

@Root(name="inputControl", strict=false)
public class InputControl implements Parcelable {

    public enum Type {
        bool,
        singleValueText,
        singleValueNumber,
        singleValueTime,
        singleValueDate,
        singleValueDatetime,
        singleSelect,
        singleSelectRadio,
        multiSelect,
        multiSelectCheckbox,
    }

    @Expose
    @Element
    private String id;
    @Expose
    @Element
    private String label;
    @Expose
    @Element
    private String uri;

    @Expose
    @Element
    private boolean mandatory;
    @Expose
    @Element
    private boolean readOnly;
    @Expose
    @Element
    private boolean visible;

    @Expose
    @Element
    private Type type;

    @Expose
    @Element
    private InputControlState state;

    @Element(required=false)
    @Expose
    @JsonAdapter(ValidationRulesListTypeAdapter.class)
    private ValidationRulesList validationRules;

    @Expose
    @ElementList(entry="controlId", empty=false)
    private List<String> masterDependencies = new ArrayList<String>();

    @Expose
    @ElementList(entry="controlId", empty=false)
    private List<String> slaveDependencies = new ArrayList<String>();

    private View inputView;
    private View errorView;

    public InputControl() {}

    //---------------------------------------------------------------------
    // Parcelable
    //---------------------------------------------------------------------

    public InputControl(Parcel source) {
        this.id = source.readString();
        this.label = source.readString();
        this.uri = source.readString();

        this.mandatory = source.readByte() != 0;
        this.readOnly = source.readByte() != 0;
        this.visible = source.readByte() != 0;

        this.type = Type.values()[source.readInt()];
        this.state = source.readParcelable(InputControlState.class.getClassLoader());
        this.validationRules = source.readParcelable(ValidationRulesList.class.getClassLoader());

        this.masterDependencies= source.createStringArrayList();
        this.slaveDependencies = source.createStringArrayList();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public InputControl createFromParcel(Parcel source) {
            return new InputControl(source);
        }

        public InputControl[] newArray(int size) {
            return new InputControl[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(label);
        dest.writeString(uri);

        dest.writeByte((byte) (mandatory ? 1 : 0));
        dest.writeByte((byte) (readOnly ? 1 : 0));
        dest.writeByte((byte) (visible ? 1 : 0));

        dest.writeInt(type.ordinal());
        dest.writeParcelable(state, flags);
        dest.writeParcelable(validationRules, flags);

        dest.writeStringList(masterDependencies);
        dest.writeStringList(slaveDependencies);
    }

    //---------------------------------------------------------------------
    // Public
    //---------------------------------------------------------------------

    public Set<String> getSelectedValues() {
        Set<String> values = new HashSet<String>();
        switch (getType()) {
            case bool:
            case singleValueText:
            case singleValueNumber:
            case singleValueTime:
            case singleValueDate:
            case singleValueDatetime:
                if (getState().getValue() != null) {
                    values.add(getState().getValue());
                }
                break;
            case singleSelect:
            case singleSelectRadio:
                for (InputControlOption option : getState().getOptions()) {
                    if (option.isSelected()) {
                        values.add(option.getValue());
                        break;
                    }
                }
            case multiSelect:
            case multiSelectCheckbox:
                for (InputControlOption option : getState().getOptions()) {
                    if (option.isSelected()) {
                        values.add(option.getValue());
                    }
                }
                break;
        }
        return values;
    }

    public void setValidationRules(List<ValidationRule> rulesList) {
        if (validationRules == null) {
            validationRules = new ValidationRulesList();
        }
        validationRules.setValidationRules(rulesList);
    }

    public List<ValidationRule> getValidationRules() {
        if (validationRules == null) {
            List<ValidationRule> rules = new ArrayList<ValidationRule>();
            validationRules = new ValidationRulesList(rules);
        }
        return validationRules.getValidationRules();
    }

    @SuppressWarnings("unchecked")
    public <T extends ValidationRule> List<T> getValidationRules(Class<T> concreteRuleType){
        List<ValidationRule> rules = getValidationRules();
        List<T> result = new ArrayList<T>();
        if(!rules.isEmpty()){
            for(ValidationRule currentRule : rules){
                if(concreteRuleType.isAssignableFrom(currentRule.getClass())){
                    result.add((T) currentRule);
                }
            }
        }
        return result;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public List<String> getMasterDependencies() {
        return masterDependencies;
    }

    public void setMasterDependencies(List<String> masterDependencies) {
        this.masterDependencies = masterDependencies;
    }

    public List<String> getSlaveDependencies() {
        return slaveDependencies;
    }

    public void setSlaveDependencies(List<String> slaveDependencies) {
        this.slaveDependencies = slaveDependencies;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public InputControlState getState() {
        return state;
    }

    public void setState(InputControlState state) {
        this.state = state;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
