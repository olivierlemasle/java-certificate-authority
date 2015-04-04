package io.github.olivierlemasle.tests.it;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import io.github.olivierlemasle.ca.CA;
import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.CertificateAuthority;
import io.github.olivierlemasle.ca.DistinguishedName;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class OpenSslIT {

  @BeforeClass
  @AfterClass
  public static void clean() {
    final File caCertPath = new File("ca.cer");
    caCertPath.delete();
    final File reqPath = new File("cert.req");
    reqPath.delete();
    final File certPath = new File("cert.cer");
    certPath.delete();
  }

  @Before
  public void checkPlatform() {
    assumeFalse(TestUtils.isWindows());
    assumeTrue(TestUtils.opensslExists());
  }

  @Test
  public void completeTest() throws IOException, InterruptedException, NoSuchAlgorithmException,
      KeyStoreException, KeyManagementException, CertificateException {
    // Create a CA
    System.out.println("Generate CA...");
    final DistinguishedName caName = CA.dn("CN=CA-Test");
    final CertificateAuthority ca = CA.init().setName(caName).build();
    // Export the CA certificate
    final X509Certificate caCert = ca.getCaCertificate();
    CA.export(caCert).saveCertificate("ca.cer");
    System.out.println("CA ready. CA certificate saved to \"ca.cer\".");

    // Generate CSR using OpenSSL
    System.out.println("Generate CSR with \"CSR.csr\"...");
    generateCsr();

    // Load the generated CSR, sign it and export the resulting certificate
    System.out.println("Sign CSR...");
    final CSR csr = CA.loadCsr("CSR.csr").getCsr();
    final X509Certificate cert = ca.sign(csr);
    CA.export(cert).saveCertificate("cert.cer");
    System.out.println("CSR signed. Certificate saved to \"cert.cer\".");

    // Reload Apache2 server
    System.out.println("Reload Apache2 server");
    apache2ServerReload();
  }

  /**
   * {@code openssl req -nodes -newkey rsa:2048 -keyout private.key -out CSR.csr -subj "/CN=localhost"}
   * 
   * @throws IOException
   * @throws InterruptedException
   */
  private void generateCsr() throws IOException, InterruptedException {
    final Process process = new ProcessBuilder("openssl", "req", "-nodes", "-newkey", "rsa:2048",
        "-keyout", "private.key", "-out", "CSR.csr", "-subj", "/CN=localhost")
        .redirectError(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT)
        .start();

    process.waitFor();
  }

  /**
   * {@code sudo service apache2 reload}
   * 
   * @throws IOException
   * @throws InterruptedException
   */
  private void apache2ServerReload() throws IOException, InterruptedException {
    final Process process = new ProcessBuilder("sudo", "service", "apache2", "reload")
        .redirectError(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT)
        .start();

    process.waitFor();
  }

}
