package com.wangyin.parameter;

import hudson.model.StringParameterValue;
import org.kohsuke.stapler.DataBoundConstructor;

public class WHideParameterValue extends StringParameterValue {

    private static final long serialVersionUID = 6926027508686211675L;

    @DataBoundConstructor
    public WHideParameterValue(String name, String value) {
        super(name, value);
    }

    public WHideParameterValue(String name, String value, String description) {
        super(name, value, description);
    }

    @Override
    public String toString() {
        return "(HiddenParameterValue) " + getName() + "='" + value + "'";
    }
}
