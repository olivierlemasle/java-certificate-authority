# java-certificate-authority
A Java Certificate Authority (CA) with web GUI

[![Build Status](https://travis-ci.org/olivierlemasle/java-certificate-authority.svg?branch=master)](https://travis-ci.org/olivierlemasle/java-certificate-authority)
[![Build status](https://ci.appveyor.com/api/projects/status/nxcp20h4qlaf1bdl?svg=true)](https://ci.appveyor.com/project/olivierlemasle/java-certificate-authority)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/4528/badge.svg)](https://scan.coverity.com/projects/4528)

* [Home Page](https://olivierlemasle.github.io/java-certificate-authority/)
* [Javadoc](https://olivierlemasle.github.io/java-certificate-authority/javadoc/)

__This is a work-in-progress!__

## Components ##

This work-in-progress app will be a web application to manage a Certificate Authority.

This application is composed of:
- [ca-api](https://github.com/olivierlemasle/java-certificate-authority/tree/master/ca-api/),
  a [Dropwizard](http://www.dropwizard.io/) application on the server side;
- [ui](https://github.com/olivierlemasle/java-certificate-authority/tree/master/ui/),
  an [Angular 2](https://angular.io/) frontend on the client side;
- [java-ca-lib](https://github.com/olivierlemasle/java-certificate-authority/tree/master/java-ca-lib/),
  a Java library providing a DSL to simplify the signing and manipulation of SSL
  certificates.

This last component can be used as a standalone Java library. It comes with its
own abstraction layer, and uses both [Bouncy Castle](http://www.bouncycastle.org/)
API and Java cryptographic APIs.

[More on java-ca-lib...](https://github.com/olivierlemasle/java-certificate-authority/blob/master/java-ca-lib/README.md)

## Using it ##

### Testing it with Docker and docker-compose###

This application comes with three Docker containers:
- a [Redis](http://redis.io/) instance,
- an API instance (backend),
- a frontend instance (based on [Nginx](http://nginx.org/)).

Using [docker-compose](https://docs.docker.com/compose/), you can create the
containers with:

    docker-compose up

Your web application will be listening on port 80.
