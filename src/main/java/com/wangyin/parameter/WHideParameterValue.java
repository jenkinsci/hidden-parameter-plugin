// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StringParameterValue.java

package com.wangyin.parameter;

import hudson.model.StringParameterValue;

import org.kohsuke.stapler.DataBoundConstructor;

/**
* @author wy-scm <wy-scm@jd.com>
*/
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