[![Build Status](https://travis-ci.org/stormpath/stormpath-shiro-web-sample.png?branch=master)](https://travis-ci.org/stormpath/stormpath-shiro-web-sample)

# Stormpath Shiro Web Sample

Fork of the Apache Shiro Web sample application that uses Stormpath for Identity Management.

This sample application uses the [Stormpath Shiro](https://github.com/stormpath/stormpath-shiro) plugin to demonstrate how to achieve Stormpath and Shiro integration.

The `stormpath-shiro` plugin allows a [Shiro](http://shiro.apache.org/)-enabled application to use the [Stormpath](http://www.stormpath.com) User Management & Authentication service for all authentication and access control needs.

Usage documentation [is in the plugin's wiki](https://github.com/stormpath/stormpath-shiro/wiki).

### Build Instructions ###

This project requires Maven 3 to build. Run the following from a command prompt:

`mvn clean compile`

## Running the Sample Application ##

Run it:

`mvn jetty:run`

## Change Log

### 1.0.0

- Upgraded Shiro dependency to version 1.2.3
- Upgraded Stormpath SDK dependency to version 1.0.RC2
- Upgraded Stormpath Shiro dependency to version 0.6.0