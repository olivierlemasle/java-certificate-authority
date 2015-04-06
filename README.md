# java-certificate-authority
A Java Certificate Authority (CA), with web GUI and CLI

[![Build Status](https://travis-ci.org/olivierlemasle/java-certificate-authority.svg?branch=master)](https://travis-ci.org/olivierlemasle/java-certificate-authority)
[![Build status](https://ci.appveyor.com/api/projects/status/nxcp20h4qlaf1bdl?svg=true)](https://ci.appveyor.com/project/olivierlemasle/java-certificate-authority)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/4528/badge.svg)](https://scan.coverity.com/projects/4528)

* [Home Page](http://olivierlemasle.github.io/java-certificate-authority/)
* [Javadoc](http://olivierlemasle.github.io/java-certificate-authority/javadoc/)


## Requirements ##

This project requires Java 7 or higher.

## Modules ##

### [java-ca-lib](https://github.com/olivierlemasle/java-certificate-authority/tree/master/java-ca-lib/) ###

This is a Java library providing a DSL to simplify the signing and manipulation
of SSL certificates.
It comes with its own abstraction layer, and uses both [Bouncy Castle](http://www.bouncycastle.org/) API and Java cryptographic APIs.

[More...](https://github.com/olivierlemasle/java-certificate-authority/blob/master/java-ca-lib/README.md)

### java-ca-web ###

This will be a web application frontend to manage a Certificate Authority.
It will use [Dropwizard](http://www.dropwizard.io/) on the server side, and
[AngularJS](https://angularjs.org/) on the client side.
