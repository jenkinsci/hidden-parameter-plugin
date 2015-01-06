hidden-parameter-plugin
=======================

##Characteristics
		1. Parameters once more, it looks bad user experience.this plugin can resolve the problem.
		2. Some key parameters, once modified, can lead to the risk.these parameters,only administrator can modify,reduce the risk.
		3. Hidden parameter,only hide in the build execution, in job configuration's page can modify.
		4. After the build, parameter list can display hidden parameters.


##Use
After the plugin is installed,in job configuration's page,you can see Hidden Parameter:<br>
![](https://github.com/wy-scm/hidden-parameter-plugin/raw/master/images/JobConfiguration1.png)

for example,you add a hidden parameter,is called svn_Root.<br>
![](https://github.com/wy-scm/hidden-parameter-plugin/raw/master/images/JobConfiguration2.png)

click 'Build With Parameters' link ,the parameter svn_Root of the set before, is hide,don't display in this page. <br>
![](https://github.com/wy-scm/hidden-parameter-plugin/raw/master/images/JobConfiguration3.png)

After the build , click 'Parameters' link, parameter list can display hidden parameters svn_Root.<br>
![](https://github.com/wy-scm/hidden-parameter-plugin/raw/master/images/JobConfiguration4.png)


