package io.github.olivierlemasle.tests;

import static io.github.olivierlemasle.ca.CA.createCertificateAuthority;
import static io.github.olivierlemasle.ca.CA.createCsr;
import static io.github.olivierlemasle.ca.CA.dn;
import static io.github.olivierlemasle.ca.CA.loadCertificateAuthority;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.CaException;
import io.github.olivierlemasle.ca.CertificateAuthority;
import io.github.olivierlemasle.ca.DistinguishedName;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class KeystoreExportTest {
  private static CertificateAuthority ca;
  private static ZonedDateTime time = ZonedDateTime.now();
  private static CSR csr;
  private static BigInteger serialNumber;
  private static X509Certificate cert;

  @BeforeClass
  public static void setup() {
    final DistinguishedName caName = dn("CN=CA-Test");
    ca = createCertificateAuthority(caName).build();
    csr = createCsr().generateRequest(dn("CN=Test"));
    serialNumber = ca.generateRandomSerialNumber();
    cert = ca.signCsr(csr)
        .setSerialNumber(serialNumber)
        .setNotBefore(time)
        .validDuringYears(1)
        .sign();
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

    final CertificateAuthority ca2 = loadCertificateAuthority("test.p12",
        "password".toCharArray(), "ca");
    assertEquals(ca.getCaCertificate(), ca2.getCaCertificate());

    final X509Certificate cert2 = ca2.signCsr(csr)
        .setSerialNumber(serialNumber)
        .setNotBefore(time)
        .validDuringYears(1)
        .sign();
    assertEquals(cert, cert2);
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
