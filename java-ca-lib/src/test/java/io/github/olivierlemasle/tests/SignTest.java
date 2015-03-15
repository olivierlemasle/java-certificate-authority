package io.github.olivierlemasle.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import io.github.olivierlemasle.ca.CA;
import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.CertificateAuthority;
import io.github.olivierlemasle.ca.DistinguishedName;

import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.BeforeClass;
import org.junit.Test;

public class SignTest {
  @BeforeClass
  public static void setup() {
    final Provider bc = new BouncyCastleProvider();
    Security.insertProviderAt(bc, 1);
  }

  @Test
  public void sign() {
    final DistinguishedName caName = CA.dn("CN=CA-Test");
    final CertificateAuthority ca = CA.init().setName(caName).build();
    final CSR csr = CA.newCsr().generateRequest(CA.dn("CN=test"));
    final X509Certificate cert = ca.sign(csr);

    try {
      cert.checkValidity();
    } catch (CertificateExpiredException | CertificateNotYetValidException e) {
      fail("Invalid certificate: " + e.toString());
    }
    assertEquals("CN=CA-Test", cert.getIssuerX500Principal().getName());
    assertEquals("CN=test", cert.getSubjectX500Principal().getName());
  }

}
