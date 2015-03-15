package io.github.olivierlemasle.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import io.github.olivierlemasle.ca.CA;
import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.CaException;
import io.github.olivierlemasle.ca.CertificateAuthority;
import io.github.olivierlemasle.ca.DistinguishedName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class KeystoreExportTest {
  private static CertificateAuthority ca;
  private static CSR csr;
  private static X509Certificate cert;

  @BeforeClass
  public static void setup() {
    final Provider bc = new BouncyCastleProvider();
    Security.insertProviderAt(bc, 1);

    final DistinguishedName caName = CA.dn("CN=CA-Test");
    ca = CA.init().setName(caName).build();
    csr = ca.generateRequest();
    cert = ca.sign(csr);
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

    final CertificateAuthority ca2 = CA.loadCertificateAuthority("test.p12",
        "password".toCharArray(),
        "ca");
    final X509Certificate cert2 = ca2.sign(csr);
    assertEquals(cert, cert2);
  }

  @Test
  public void testInvalidKeystorePath() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    try {
      CA.loadCertificateAuthority("invalid", "password".toCharArray(), "ca");
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

  @Test
  public void testInvalidKeystorePassword() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    try {
      CA.loadCertificateAuthority("test.p12", "incorrect".toCharArray(), "ca");
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

  @Test
  public void testInvalidAlias() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    try {
      CA.loadCertificateAuthority("test.p12", "password".toCharArray(), "incorrect");
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

}
