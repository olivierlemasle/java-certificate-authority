package io.github.olivierlemasle.tests;

import io.github.olivierlemasle.ca.CA;
import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.CertificateAuthority;
import io.github.olivierlemasle.ca.DistinguishedName;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class WindowsIT {
  @BeforeClass
  public static void setup() {
    final Provider bc = new BouncyCastleProvider();
    Security.insertProviderAt(bc, 1);
  }

  @BeforeClass
  @AfterClass
  public static void clean() {
    final File caCertPath = new File("ca.cer");
    caCertPath.delete();
    final File certPath = new File("cert.cer");
    certPath.delete();
  }

  @Test
  public void test() throws IOException, InterruptedException {
    // Create a CA
    final DistinguishedName caName = CA.dn("CN=CA-Test");
    final CertificateAuthority ca = CA.init().setName(caName).build();
    // Export the CA certificate
    CA.export(ca.getCaCertificate()).saveCertificate("ca.cer");

    installCaCert();

    final CSR csr = CA.newCsr().generateRequest(CA.dn("CN=test"));
    final X509Certificate cert = ca.sign(csr);

    CA.export(cert).saveCertificate("cert.cer");
  }

  private void installCaCert() throws IOException, InterruptedException {
    final Process process = new ProcessBuilder("certutil", "-addstore", "ROOT", "ca.cer")
        .redirectError(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT)
        .start();

    process.waitFor();
  }

}
