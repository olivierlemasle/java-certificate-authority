package io.github.olivierlemasle.tests;

import static io.github.olivierlemasle.ca.CA.createCsr;
import static io.github.olivierlemasle.ca.CA.createSelfSignedCertificate;
import static io.github.olivierlemasle.ca.CA.dn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import org.junit.Test;

import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.ca.RootCertificate;

public class SignTest {

  @Test
  public void sign() {
    final DistinguishedName caName = dn("CN=CA-Test");
    final RootCertificate ca = createSelfSignedCertificate(caName).build();
    final CSR csr = createCsr().generateRequest(dn("CN=test"));
    final X509Certificate cert = ca.signCsr(csr)
        .setRandomSerialNumber()
        .sign()
        .getX509Certificate();

    try {
      cert.checkValidity();
    } catch (CertificateExpiredException | CertificateNotYetValidException e) {
      fail("Invalid certificate: " + e.toString());
    }
    assertEquals("CN=CA-Test", cert.getIssuerX500Principal().getName());
    assertEquals("CN=test", cert.getSubjectX500Principal().getName());
    assertEquals(csr.getPublicKey(), cert.getPublicKey());
  }

}
