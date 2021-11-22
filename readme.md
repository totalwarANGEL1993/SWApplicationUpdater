# Update Manager

## Introduction

This is a simple programm that allows to update software via
repository. The user just needs to plug in to the internet and
can always update the application.

## Shipment

To deploy any application follow these steps:
1. Copy application-sample.properties
2. Name the new file like the profile you are using.
3. Create a new repository (see MapIconatorDeployment for reference).
4. Enter the application info as shown in the sample.
5. Create a runner (see runner in symfonia project)
and change the active profile to the name you given in step 2.
6. Build the runner and copy it in re folder above the repository.
7. Copy the JRE into the same directory.
8. Create a folder config and copy application-context.properties and your modified properties.
9. Create installer.