# java-ca-lib #

## Requirements ##

This library requires Java 8 or higher.

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

The best is to browse the [Javadoc](http://olivierlemasle.github.io/java-certificate-authority/javadoc/).

Example:

```java
import static io.github.olivierlemasle.ca.CA.*;
import io.github.olivierlemasle.ca.*;
import java.security.cert.X509Certificate;

public class Main {

  public static void main(final String[] args) {
    final DistinguishedName root = dn("CN=Root-Test, O=My Org");
    final RootCertificate ca = createSelfSignedCertificate(root)
        .validDuringYears(10)
        .build();

    final CSR csr = createCsr().generateRequest(
        dn()
            .setCn("John Doe")
            .setO("World Company")
            .setOu("IT dep.")
            .setSt("CA")
            .setC("US")
            .build());

    final Certificate cert = ca.signCsr(csr)
        .setRandomSerialNumber()
        .validDuringYears(2)
        .sign();

    System.out.println("Subject: " + cert.getX509Certificate().getSubjectDN());
    System.out.println("Issuer: " + cert.getX509Certificate().getIssuerDN());

    System.out.println(cert.print());
  }
}

```
