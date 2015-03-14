package io.github.olivierlemasle.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import io.github.olivierlemasle.ca.CA;
import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.CaException;
import io.github.olivierlemasle.ca.CertificateAuthority;
import io.github.olivierlemasle.ca.Name;

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

    final Name caName = CA.getName("CN=CA-Test");
    ca = CA.init().setName(caName).build();
    csr = ca.generateRequest();
    cert = ca.sign(csr);
  }

  @After
  public void clean() {
    try {
      Files.delete(Paths.get("test"));
    } catch (final IOException e) {
      fail("Cannot delete keystore");
    }
  }

  @Test
  public void saveToKeystoreFileAndBack() {
    ca.saveToKeystoreFile("test", "password".toCharArray(), "password2".toCharArray());

    final CertificateAuthority ca2 = CA.loadCertificateAuthority("test",
        "password".toCharArray(),
        "password2".toCharArray());
    final X509Certificate cert2 = ca2.sign(csr);
    assertEquals(cert, cert2);
  }

  @Test
  public void testInvalidKeystorePath() {
    ca.saveToKeystoreFile("test", "password".toCharArray(), "password2".toCharArray());

    try {
      CA.loadCertificateAuthority("invalid", "password".toCharArray(), "password2".toCharArray());
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

  @Test
  public void testInvalidKeystorePassword() {
    ca.saveToKeystoreFile("test", "password".toCharArray(), "password2".toCharArray());

    try {
      CA.loadCertificateAuthority("test", "incorrect".toCharArray(), "password2".toCharArray());
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

  @Test
  public void testInvalidPrivateKeyPassword() {
    ca.saveToKeystoreFile("test", "password".toCharArray(), "password2".toCharArray());

    try {
      CA.loadCertificateAuthority("test", "password".toCharArray(), "incorrect".toCharArray());
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

}
