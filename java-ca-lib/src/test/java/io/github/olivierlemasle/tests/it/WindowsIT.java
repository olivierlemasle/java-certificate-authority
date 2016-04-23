package io.github.olivierlemasle.tests.it;

import static io.github.olivierlemasle.ca.CA.createSelfSignedCertificate;
import static io.github.olivierlemasle.ca.CA.dn;
import static io.github.olivierlemasle.ca.CA.loadCsr;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.UUID;

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

public class WindowsIT {

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
    assumeTrue(TestUtils.isWindows());
    assumeTrue(TestUtils.isWindowsAdministrator());
  }

  @Test
  public void completeTest() throws IOException, InterruptedException, NoSuchAlgorithmException,
      KeyStoreException, KeyManagementException, CertificateException {
    // Create a self-signed root certificate
    System.out.println("Generate ..");
    final DistinguishedName rootDn = dn("CN=CA-Test");
    final RootCertificate root = createSelfSignedCertificate(rootDn).build();
    // Export the root certificate
    root.save("ca.cer");
    System.out.println("CA ready. CA certificate saved to \"ca.cer\".");

    // Generate CSR using Windows utilities
    System.out.println("Generate CSR with \"cert.req\"...");
    generateCsr();

    // Load the generated CSR, sign it and export the resulting certificate
    System.out.println("Sign CSR...");
    final CSR csr = loadCsr("cert.req").getCsr();
    final Certificate cert = root.signCsr(csr)
        .setRandomSerialNumber()
        .sign();
    cert.save("cert.cer");
    System.out.println("CSR signed. Certificate saved to \"cert.cer\".");

    // On Windows, install the CA certificate as a trusted certificate
    System.out.println("Install \"ca.cer\" as a trusted certificate authority.");
    installTrustedCaCert();
    // On Windows, install the "cert.cer" certificate, with its private key
    System.out.println("Install \"cert.cer\" from the CSR (with private key).");
    acceptCert();

    // Configure SSL
    System.out.println("Configure SSL");
    final String certThumbprint = getThumbPrint(cert.getX509Certificate());
    configureSsl(certThumbprint, UUID.randomUUID().toString());
    // NB: https binding has been set in appveyor.yml

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
      assertTrue(content.contains("<title>IIS Windows Server</title>"));
    }

  }

  /**
   * {@code certutil -enterprise -addstore ROOT ca.cer}
   *
   * @throws IOException
   * @throws InterruptedException
   */
  private void installTrustedCaCert() throws IOException, InterruptedException {
    final Process process = new ProcessBuilder("certutil", "-enterprise", "-addstore", "ROOT",
        "ca.cer")
            .redirectError(Redirect.INHERIT)
            .redirectOutput(Redirect.INHERIT)
            .start();

    process.waitFor();
  }

  /**
   * {@code certreq -new src\test\resources\csr_template.inf cert.req}
   *
   * @throws IOException
   * @throws InterruptedException
   */
  private void generateCsr() throws IOException, InterruptedException {
    final Process process = new ProcessBuilder("certreq", "-new",
        "src\\test\\resources\\csr_template.inf", "cert.req")
            .redirectError(Redirect.INHERIT)
            .redirectOutput(Redirect.INHERIT)
            .start();

    process.waitFor();
  }

  /**
   * {@code certreq -accept cert.cer}
   *
   * @throws IOException
   * @throws InterruptedException
   */
  private void acceptCert() throws IOException, InterruptedException {
    final Process process = new ProcessBuilder("certreq", "-accept", "cert.cer")
        .redirectError(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT)
        .start();

    process.waitFor();
  }

  /**
   * {@code netsh http add sslcert ipport=0.0.0.0:443 certhash=... appid=...}
   *
   * @param certHash
   * @param appId
   * @throws IOException
   * @throws InterruptedException
   */
  private void configureSsl(final String certHash, final String appId) throws IOException,
      InterruptedException {
    final String certhashParam = "certhash=" + certHash;
    final String appidParam = "appid={" + appId + "}";
    final Process process = new ProcessBuilder("netsh", "http", "add", "sslcert",
        "ipport=0.0.0.0:443", "certstorename=MY", certhashParam, appidParam)
            .redirectError(Redirect.INHERIT)
            .redirectOutput(Redirect.INHERIT)
            .start();

    process.waitFor();
  }

  private static String getThumbPrint(final X509Certificate cert) throws NoSuchAlgorithmException,
      CertificateEncodingException {
    final MessageDigest md = MessageDigest.getInstance("SHA-1");
    final byte[] der = cert.getEncoded();
    md.update(der);
    final byte[] digest = md.digest();
    return hexify(digest);
  }

  private static String hexify(final byte bytes[]) {
    final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
        'd', 'e', 'f' };

    final StringBuilder buf = new StringBuilder(bytes.length * 2);
    for (final byte b : bytes) {
      buf.append(hexDigits[(b & 0xf0) >> 4]);
      buf.append(hexDigits[b & 0x0f]);
    }
    return buf.toString();
  }

}
