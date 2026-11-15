/*
 * The MIT License
 *
 * Copyright (c) 2025
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wangyin.parameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.sonyericsson.rebuild.RebuildAction;
import hudson.model.Cause;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.ParametersDefinitionProperty;
import hudson.model.StringParameterDefinition;
import hudson.model.StringParameterValue;
import java.util.List;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
public class WHideParameterProviderTest {

    @Test
    public void testRebuild(JenkinsRule jenkins) throws Exception {

        final String defaultValue = "default_value_from_hidden_parameter";

        WHideParameterDefinition param =
                new WHideParameterDefinition("MY_HIDDEN_PARAMETER", defaultValue, "my hidden value");
        FreeStyleProject job = jenkins.createProject(FreeStyleProject.class, "test-project");
        job.addProperty(new ParametersDefinitionProperty(
                param, new StringParameterDefinition("VISIBLE_PARAMETER", "visible-default")));

        String previousValue = "value-from-run <&\"'>";
        FreeStyleBuild build1 = job.scheduleBuild2(
                        0,
                        new Cause.UserIdCause(),
                        List.of(new ParametersAction(
                                new WHideParameterValue("MY_HIDDEN_PARAMETER", previousValue, "description-from-run"),
                                new StringParameterValue("VISIBLE_PARAMETER", "visible-from-run"))))
                .get();
        jenkins.assertBuildStatusSuccess(build1);

        // now try to rebuild
        try (WebClient wc = jenkins.createWebClient()) {
            HtmlPage page = wc.getPage(jenkins.getURL()
                    + build1.getUrl()
                    + build1.getAction(RebuildAction.class).getTaskUrl());
            HtmlForm form = page.getFormByName("config");
            assertThat(page.asNormalizedText(), not(containsString("MY_HIDDEN_PARAMETER")));
            assertThat(page.asNormalizedText(), not(containsString(previousValue)));
            assertThat(page.asNormalizedText(), not(containsString("description-from-run")));
            assertThat(page.asNormalizedText(), containsString("VISIBLE_PARAMETER"));
            HtmlElement parameter = form.getFirstByXPath(
                    ".//div[@name='parameter' and input[@name='name' and @value='MY_HIDDEN_PARAMETER']]");
            HtmlInput value = parameter.getFirstByXPath("./input[@name='value']");
            assertThat(value.getTypeAttribute(), is("hidden"));
            assertThat(value.getValueAttribute(), is(previousValue));
            jenkins.submit(form);

            jenkins.waitUntilNoActivity();
        }
        FreeStyleBuild build2 = job.getLastBuild();
        jenkins.assertBuildStatusSuccess(build2);
        assertThat(build2.getNumber(), not(build1.getNumber()));
        ParameterValue parameterValueBuild2 = build2.getParameterValues().stream()
                .filter(WHideParameterValue.class::isInstance)
                .findFirst()
                .orElseThrow(AssertionError::new);
        assertThat(parameterValueBuild2.getName(), is("MY_HIDDEN_PARAMETER"));
        assertThat(parameterValueBuild2.getValue(), is(previousValue));
    }
}
