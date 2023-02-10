# Hidden Parameter Plugin

[![Build Status](https://ci.jenkins.io/job/Plugins/job/hidden-parameter-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/hidden-parameter-plugin/job/master/)
[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/hidden-parameter.svg)](https://plugins.jenkins.io/hidden-parameter)
[![GitHub release](https://img.shields.io/github/release/jenkinsci/hidden-parameter-plugin.svg?label=changelog)](https://github.com/jenkinsci/hidden-parameter-plugin/releases/latest)
[![GitHub license](https://img.shields.io/github/license/jenkinsci/hidden-parameter-plugin)](https://github.com/jenkinsci/hidden-parameter-plugin/blob/master/LICENSE)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/hidden-parameter.svg?color=blue)](https://plugins.jenkins.io/hidden-parameter)

Provide `hidden` parameter to Jenkins Jobs. Similar to `string` parameter but not displayed on the UI when triggering the jobs. 

Useful when administrators want to enforce some parameters/variables to jobs

## Examples

In freestyle jobs

![](images/JobConfiguration1.png)

Parameter is not visible on the UI

![](images/JobConfiguration2.png)

Parameter is set for the job

![](images/JobConfiguration3.png)

## Pipeline support

```
parameters {
    hidden(name: 'hidden_param', defaultValue: 'hidden_value', description: 'Hidden parameter')
}
```

## JobDSL support

```
parameters {
    hidden {
        name('param_hidden')
        defaultValue('hidden_value')
        description('Hidden parameter')
    }
}
```
