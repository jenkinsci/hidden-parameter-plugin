package com.wangyin.parameter;

import hudson.Extension;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
import java.io.Serial;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest2;

public class WHideParameterDefinition extends ParameterDefinition {

    @Serial
    private static final long serialVersionUID = 8296806777255584941L;

    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @DataBoundConstructor
    public WHideParameterDefinition(String name, String defaultValue, String description) {
        super(name);
        this.defaultValue = defaultValue;
        setDescription(description);
    }

    @Extension
    @Symbol({"hidden", "hiddenParam"})
    public static class DescriptorImpl extends ParameterDescriptor {
        @Override
        public String getDisplayName() {
            return "Hidden Parameter";
        }
    }

    @Override
    public WHideParameterValue getDefaultParameterValue() {
        WHideParameterValue v = new WHideParameterValue(getName(), defaultValue, getDescription());
        return v;
    }

    public ParameterValue createValue(StaplerRequest2 req) {
        String[] value = req.getParameterValues(getName());
        if (value == null) {
            return getDefaultParameterValue();
        } else if (value.length != 1) {
            throw new IllegalArgumentException(
                    "Illegal number of parameter values for " + getName() + ": " + value.length);
        } else {
            return new WHideParameterValue(getName(), value[0], getDescription());
        }
    }

    public ParameterValue createValue(StaplerRequest2 req, JSONObject jo) {
        WHideParameterValue value = req.bindJSON(WHideParameterValue.class, jo);
        value.setDescription(getDescription());
        return value;
    }
}
