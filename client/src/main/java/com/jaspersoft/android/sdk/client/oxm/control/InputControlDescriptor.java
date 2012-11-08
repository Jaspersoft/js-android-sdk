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

package com.jaspersoft.android.sdk.client.oxm.control;

import android.view.View;
import com.jaspersoft.android.sdk.client.oxm.control.validation.DateTimeFormatValidationRule;
import com.jaspersoft.android.sdk.client.oxm.control.validation.MandatoryValidationRule;
import com.jaspersoft.android.sdk.client.oxm.control.validation.ValidationRule;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.4
 */

@Root(name="inputControl", strict=false)
public class InputControlDescriptor {

    public enum Type {
        bool,
        singleValueText,
        singleValueNumber,
        singleValueDate,
        singleValueDatetime,
        singleSelect,
        singleSelectRadio,
        multiSelect,
        multiSelectCheckbox,
    }

    @Element
    private String id;
    @Element
    private String label;
    @Element
    private boolean mandatory;

    @ElementList(entry="controlId", empty=false)
    private List<String> masterDependencies;

    @ElementList(entry="controlId", empty=false)
    private List<String> slaveDependencies;

    @Element
    private InputControlState state;
    @Element
    private boolean readOnly;
    @Element
    private Type type;
    @Element
    private String uri;

    @Element(required=false)
    private ValidationRulesList validationRules;

    @Element
    private boolean visible;

    private View inputView;
    private View errorView;


    public Set<String> getSelectedValues() {
        Set<String> values = new HashSet<String>();
        switch (getType()) {
            case bool:
            case singleValueText:
            case singleValueNumber:
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

    //---------------------------------------------------------------------
    // Inner classes
    //---------------------------------------------------------------------

    @Root(name="validationRules")
    public static class ValidationRulesList {

        @ElementListUnion({
                @ElementList(entry="dateTimeFormatValidationRule", inline=true, type=DateTimeFormatValidationRule.class),
                @ElementList(entry="mandatoryValidationRule", inline=true, type=MandatoryValidationRule.class)
                // @ElementList(entry="rangeValidationRule", type=RangeValidationRule.class)
                // @ElementList(entry="regexpValidationRule", type=RegexpValidationRule.class)
        })
        private List<ValidationRule> validationRules;

        public ValidationRulesList() { }

        public ValidationRulesList(List<ValidationRule> validationRules) {
            this.validationRules = validationRules;
        }

        public List<ValidationRule> getValidationRules() {
            return validationRules;
        }

        public void setValidationRules(List<ValidationRule> validationRules) {
            this.validationRules = validationRules;
        }
    }

}
