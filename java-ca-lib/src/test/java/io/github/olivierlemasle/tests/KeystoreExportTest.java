package io.github.olivierlemasle.tests;

import static io.github.olivierlemasle.ca.CA.createCsr;
import static io.github.olivierlemasle.ca.CA.createSelfSignedCertificate;
import static io.github.olivierlemasle.ca.CA.dn;
import static io.github.olivierlemasle.ca.CA.generateRandomSerialNumber;
import static io.github.olivierlemasle.ca.CA.loadRootCertificate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.CaException;
import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.ca.RootCertificate;

public class KeystoreExportTest {
  private static RootCertificate ca;
  private static ZonedDateTime time = ZonedDateTime.now();
  private static CSR csr;
  private static BigInteger serialNumber;
  private static X509Certificate cert;

  @BeforeClass
  public static void setup() {
    final DistinguishedName caName = dn("CN=CA-Test");
    ca = createSelfSignedCertificate(caName).build();
    csr = createCsr().generateRequest(dn("CN=Test"));
    serialNumber = generateRandomSerialNumber();
    cert = ca.signCsr(csr)
        .setSerialNumber(serialNumber)
        .setNotBefore(time)
        .validDuringYears(1)
        .sign()
        .getX509Certificate();
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

    final RootCertificate ca2 = loadRootCertificate("test.p12",
        "password".toCharArray(), "ca");
    assertEquals(ca.getX509Certificate(), ca2.getX509Certificate());

    final X509Certificate cert2 = ca2.signCsr(csr)
        .setSerialNumber(serialNumber)
        .setNotBefore(time)
        .validDuringYears(1)
        .sign()
        .getX509Certificate();
    assertEquals(cert, cert2);
  }

  @Test
  public void testInvalidKeystorePath() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    try {
      loadRootCertificate("invalid", "password".toCharArray(), "ca");
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

  @Test
  public void testInvalidKeystorePassword() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    try {
      loadRootCertificate("test.p12", "incorrect".toCharArray(), "ca");
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

  @Test
  public void testInvalidAlias() {
    ca.exportPkcs12("test.p12", "password".toCharArray(), "ca");

    try {
      loadRootCertificate("test.p12", "password".toCharArray(), "incorrect");
      fail("CaException expected");
    } catch (final CaException expected) {
    }
  }

}
