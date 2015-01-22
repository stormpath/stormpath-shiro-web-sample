[![Build Status](https://travis-ci.org/stormpath/stormpath-shiro-web-sample.png?branch=master)](https://travis-ci.org/stormpath/stormpath-shiro-web-sample)

# Stormpath Shiro Web Sample

Fork of the Apache Shiro Web sample application that uses Stormpath for User Management. This sample application uses the [Apache Shiro plugin for Stormpath](https://github.com/stormpath/stormpath-shiro) to demonstrate how to integrate Apache Shiro and Stormpath.

Stormpath is a User Management API that reduces development time with instant-on, scalable user infrastructure. Stormpath's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users and roles in any application. The `stormpath-shiro` plugin allows a [Shiro](http://shiro.apache.org/)-enabled application to use [Stormpath](http://www.stormpath.com) for all authentication and access control needs.

## Documentation

Stormpath offers usage documentation and support for the Apache Shiro Plugin for Stormpath [in the wiki](https://github.com/stormpath/stormpath-shiro/wiki). Please email support@stormpath.com with any errors or issues with the documentation.

## Links

Below are some resources you might find useful!
- [The Apache Shiro Plugin for Stormpath](https://github.com/stormpath/stormpath-shiro)
- [Stormpath-Shiro Sample Webapp](https://github.com/stormpath/stormpath-shiro-web-sample)
- [User Permissions with Apache Shiro and Stormpath](https://stormpath.com/blog/user-permissions-apache-shiro-and-stormpath/)

**Stormpath Java Support**
- [Stormpath API Docs for Java](https://docs.stormpath.com/java/apidocs/)
- [Stormpath Java Product Guide](https://docs.stormpath.com/java/product-guide/)
- [Stormpath Java SDK](https://github.com/stormpath/stormpath-sdk-java)

## Contributing

Contributions, bug reports and issues are very welcome. Stormpath regularly maintains this repository, and are quick to review pull requests and accept changes!

You can make your own contributions by forking the develop branch of this
repository, making your changes, and issuing pull request on the develop branch.

### Build Instructions ###

This project requires Maven 3 to build. Run the following from a command prompt:

`mvn clean compile`

Release changes are viewable in the [change log](changelog.md)

## Running the Sample Application ##

Run it:

`mvn jetty:run`


## Copyright ##

Copyright &copy; 2013-2015 Stormpath, Inc. and contributors.

This project is open-source via the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).