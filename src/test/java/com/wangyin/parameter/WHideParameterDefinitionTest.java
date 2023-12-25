package com.wangyin.parameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Label;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.ParametersDefinitionProperty;
import java.nio.charset.StandardCharsets;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import org.kohsuke.stapler.StaplerRequest;

@WithJenkins
public class WHideParameterDefinitionTest {

    @Test
    public void shouldWork(JenkinsRule jenkins) throws Exception {
        final String defaultValue = "default_value_from_hidden_parameter";
        WHideParameterDefinition param =
                new WHideParameterDefinition("MY_HIDDEN_PARAMETER", defaultValue, "my hidden value");
        assertThat(param.getDefaultParameterValue().getValue(), is(defaultValue));
        param.setDefaultValue("new_default_value");
        assertThat(param.getDefaultParameterValue().getValue(), is("new_default_value"));
        assertThat(param.getDefaultValue(), is("new_default_value"));
    }

    @Test
    public void shouldCreateValue(JenkinsRule jenkinsRule) {
        final String defaultValue = "default_value_from_hidden_parameter";
        WHideParameterDefinition param =
                new WHideParameterDefinition("MY_HIDDEN_PARAMETER", defaultValue, "my hidden value");

        // Without JSON and not value
        assertThat(
                param.createValue(mock(StaplerRequest.class)),
                is(new WHideParameterValue("MY_HIDDEN_PARAMETER", defaultValue, "my hidden value")));

        // Without JSON and with value
        StaplerRequest req = mock(StaplerRequest.class);
        doReturn(new String[] {"the new value"}).when(req).getParameterValues("MY_HIDDEN_PARAMETER");
        assertThat(
                param.createValue(req),
                is(new WHideParameterValue("MY_HIDDEN_PARAMETER", "the new value", "my hidden value")));

        // With illegal number of value
        req = mock(StaplerRequest.class);
        doReturn(new String[] {"the new value", "the new value"}).when(req).getParameterValues("MY_HIDDEN_PARAMETER");
        try {
            param.createValue(req);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Illegal number of parameter values for MY_HIDDEN_PARAMETER: 2"));
        }

        // With JSON
        JSONObject json = mock(JSONObject.class);
        req = mock(StaplerRequest.class);
        WHideParameterValue expectedValue = new WHideParameterValue("the name", "the value");
        doReturn(expectedValue).when(req).bindJSON(WHideParameterValue.class, json);
        assertThat(param.createValue(req, json), is(new WHideParameterValue("the name", "the value", "the value")));
        assertThat(expectedValue.toString(), is("(HiddenParameterValue) the name='the value'"));
    }

    @Test
    public void testFreestyle(JenkinsRule jenkins) throws Exception {

        final String defaultValue = "default_value_from_hidden_parameter";

        WHideParameterDefinition param =
                new WHideParameterDefinition("MY_HIDDEN_PARAMETER", defaultValue, "my hidden value");
        FreeStyleProject job = jenkins.createProject(FreeStyleProject.class, "test-projet");
        job.addProperty(new ParametersDefinitionProperty(param));

        FreeStyleBuild completedBuild = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
        ParametersAction parameterAction = completedBuild.getAction(ParametersAction.class);

        ParameterValue paramValue = parameterAction.getParameter("MY_HIDDEN_PARAMETER");
        assertThat(paramValue.getValue(), is(defaultValue));
    }

    @Test
    public void testPipelineParameters(JenkinsRule jenkins) throws Exception {
        String pipeline = IOUtils.toString(
                WHideParameterDefinitionTest.class.getResourceAsStream("/pipelines/declarative.groovy"),
                StandardCharsets.UTF_8);
        jenkins.createSlave(Label.get("test-agent"));
        WorkflowJob workflowJob = jenkins.createProject(WorkflowJob.class);
        workflowJob.setDefinition(new CpsFlowDefinition(pipeline, true));
        WorkflowRun run1 = workflowJob.scheduleBuild2(0).waitForStart();
        jenkins.waitForCompletion(run1);
        assertThat(run1.getResult(), equalTo(hudson.model.Result.SUCCESS));
        assertThat(run1.getLog(), allOf(containsString("Hello World")));
    }
}
