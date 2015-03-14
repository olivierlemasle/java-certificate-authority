package io.github.olivierlemasle.ca;

import static org.junit.Assert.assertEquals;

import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainTest {
  @BeforeClass
  public static void setup() {
    final Provider bc = new BouncyCastleProvider();
    Security.insertProviderAt(bc, 1);
  }

  @Test
  public void test() {
    final CertificateAuthorityCreator c = new CertificateAuthorityCreatorImpl();

    final Name caName = new NameImpl("CN=CA-Test");
    final KeyStore keyStore = c.createCertificateAuthority(caName, "password".toCharArray());
    final CertificateAuthority ca = c.loadCertificateAuthority(keyStore, "password".toCharArray());
    final CSR csr = ca.generateRequest();
    final Certificate cert = ca.sign(csr);
    assertEquals("CN=CA-Test", cert.getX509Certificate().getIssuerX500Principal().getName());
  }

}
