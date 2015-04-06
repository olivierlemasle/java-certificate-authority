package io.github.olivierlemasle.tests;

import static io.github.olivierlemasle.ca.CA.createCertificateAuthority;
import static io.github.olivierlemasle.ca.CA.dn;
import static io.github.olivierlemasle.ca.CA.loadCertificateAuthority;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import io.github.olivierlemasle.ca.CaException;
import io.github.olivierlemasle.ca.CertificateAuthority;
import io.github.olivierlemasle.ca.DistinguishedName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class KeystoreExportTest {
  private static CertificateAuthority ca;

  @BeforeClass
  public static void setup() {
    final DistinguishedName caName = dn("CN=CA-Test");
    ca = createCertificateAuthority(caName).build();
  }

  @After
  public void clean() {
    try {
      Files.delete(Paths.get("test.p12"));
    } catch (final IOException e) {
      fail("Cannot delete keystore");
    }
  }

  @Test
  public void saveToKeystoreFileAndBack() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    final CertificateAuthority ca2 = loadCertificateAuthority("test.p12", "password".toCharArray(),
        "ca");
    assertEquals(ca.getCaCertificate(), ca2.getCaCertificate());
  }

  @Test
  public void testInvalidKeystorePath() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    try {
      loadCertificateAuthority("invalid", "password".toCharArray(), "ca");
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

  @Test
  public void testInvalidKeystorePassword() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    try {
      loadCertificateAuthority("test.p12", "incorrect".toCharArray(), "ca");
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

  @Test
  public void testInvalidAlias() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    try {
      loadCertificateAuthority("test.p12", "password".toCharArray(), "incorrect");
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

}
