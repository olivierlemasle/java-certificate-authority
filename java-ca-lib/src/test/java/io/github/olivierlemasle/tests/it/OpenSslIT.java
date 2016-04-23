package io.github.olivierlemasle.tests.it;

import static io.github.olivierlemasle.ca.CA.createSelfSignedCertificate;
import static io.github.olivierlemasle.ca.CA.dn;
import static io.github.olivierlemasle.ca.CA.loadCsr;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.Certificate;
import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.ca.RootCertificate;

public class OpenSslIT {

  @BeforeClass
  @AfterClass
  public static void clean() {
    final File caCertPath = new File("ca.cer");
    caCertPath.delete();
    final File reqPath = new File("CSR.csr");
    reqPath.delete();
    final File keyPath = new File("private.key");
    keyPath.delete();
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
    // Create a self-signed root certificate
    System.out.println("Generate ..");
    final DistinguishedName rootDn = dn("CN=CA-Test");
    final RootCertificate root = createSelfSignedCertificate(rootDn).build();
    // Export the CA certificate
    root.save("ca.cer");
    System.out.println("CA ready. CA certificate saved to \"ca.cer\".");

    // Generate CSR using OpenSSL
    System.out.println("Generate CSR with \"CSR.csr\"...");
    generateCsr();

    // Load the generated CSR, sign it and export the resulting certificate
    System.out.println("Sign CSR...");
    final CSR csr = loadCsr("CSR.csr").getCsr();
    final Certificate cert = root.signCsr(csr)
        .setRandomSerialNumber()
        .sign();
    cert.save("cert.cer");
    System.out.println("CSR signed. Certificate saved to \"cert.cer\".");

    // Reload Apache2 server
    System.out.println("Configure Apache2 server");
    configureApache2Server();

    // Add the CA certificate to a truststore
    final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    keystore.load(null, null);
    keystore.setCertificateEntry("cert", root.getX509Certificate());
    final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keystore, null).build();
    // Test the HTTPS connection
    System.out.println("Test https://localhost/");
    try (CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        CloseableHttpResponse response = httpClient.execute(new HttpGet("https://localhost/"))) {
      final HttpEntity entity = response.getEntity();
      final String content = EntityUtils.toString(entity);
      assertTrue(content.contains("It works"));
    }
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
  private void configureApache2Server() throws IOException, InterruptedException {
    final Process process = new ProcessBuilder("sudo", "src/test/resources/configureApache2.sh")
        .redirectError(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT)
        .start();

    process.waitFor();
  }

}
