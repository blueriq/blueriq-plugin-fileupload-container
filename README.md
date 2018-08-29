[![][logo]][website] 

# About

The plugin `blueriq-plugin-fileupload-container` used to be a part of the Blueriq Runtime but since the release of Blueriq 11, the support of this plugin is discontinued. 
Blueriq released the sources of this plugin with the intention of enabling customers to migrate the AQ_File_Upload at their own pace but without support from Blueriq. No rights reserved.

# Build from source

> The blueriq-plugin-fileupload-container has a dependency with the XSLT plugin. This plugin is also
not supported by Blueriq anymore but the sources can be found here: https://github.com/blueriq/blueriq-plugin-xslt-ui

To compile and build war use:

```bash
mvn clean verify
```

# Run example

Deploy `Runtime.war` to Tomcat container. Create a configuration folder and add Blueriq `license.aql` or package Blueriq `license.aql` by adding it to `src\main\resources`.
Start Tomcat container with the following parameters:

```bash
-Dspring.config.additional-location=file://path_to_conf/ # URI of the configuration folder which contains the Blueriq license.
-Dspring.profiles.active=native,development-tools
```

# Studio container
![][singlefileupload_definition]

[singlefileupload_definition]: images/singlefileupload_containertype.jpg

[logo]: https://www.blueriq.com/wp-content/uploads/2018/07/BLUERIQ-rgb-logo-kleur-gradient-PNG-300x111.png
[website]: http://www.blueriq.com