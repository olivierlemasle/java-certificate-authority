# java-ca-lib #

## Setup ##

*java-ca-lib* has not been released yet. You can test it using Sonatype's
SNAPSHOT repository.

Using Maven, configure Sonatype's SNAPSHOT repository:

```xml
<repositories>
  <repository>
    <id>sonatype-nexus-snapshots</id>
    <name>Sonatype Nexus Snapshots</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
  </repository>
</repositories>
```

And add the java-ca-lib dependency:

```xml
<dependencies>
  <dependency>
    <groupId>io.github.olivierlemasle</groupId>
    <artifactId>java-ca-lib</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
</dependencies>
```

## Usage ##

```java

```
