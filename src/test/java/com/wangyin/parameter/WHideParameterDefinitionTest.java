package com.wangyin.parameter;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.ParametersDefinitionProperty;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class WHideParameterDefinitionTest {
    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void testFreestylePipeline() throws Exception {

        final String defaultValue = "default_value_from_hidden_parameter";

        WHideParameterDefinition param = new WHideParameterDefinition("MY_HIDDEN_PARAMETER", defaultValue,
                "my hidden value");
        FreeStyleProject job = jenkins.createProject(FreeStyleProject.class, "test-projet");
        job.addProperty(new ParametersDefinitionProperty(param));

        FreeStyleBuild completedBuild = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
        ParametersAction parameterAction = completedBuild.getAction(ParametersAction.class);

        ParameterValue paramValue = parameterAction.getParameter("MY_HIDDEN_PARAMETER");
        assertEquals(paramValue.getValue(), defaultValue);
    }
}
