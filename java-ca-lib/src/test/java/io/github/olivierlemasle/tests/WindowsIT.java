package io.github.olivierlemasle.tests;

import io.github.olivierlemasle.ca.CA;
import io.github.olivierlemasle.ca.CSR;
import io.github.olivierlemasle.ca.CertificateAuthority;
import io.github.olivierlemasle.ca.DistinguishedName;

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
import org.junit.BeforeClass;
import org.junit.Test;

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

  @Test
  public void test() throws IOException, InterruptedException, NoSuchAlgorithmException,
      KeyStoreException, KeyManagementException, CertificateException {
    // Create a CA
    final DistinguishedName caName = CA.dn("CN=CA-Test");
    final CertificateAuthority ca = CA.init().setName(caName).build();
    // Export the CA certificate
    final X509Certificate caCert = ca.getCaCertificate();
    CA.export(caCert).saveCertificate("ca.cer");

    // On Windows, install the CA certificate as a trusted certificate
    // certutil -enterprise -addstore ROOT ca.cer
    installTrustedCert("ca.cer");

    // Generate CSR using Windows utilities
    // certreq -new src\test\resources\csr_template.inf cert.req
    generateCsr();

    // Load the generated CSR, sign it and export the resulting certificate
    final CSR csr = CA.loadCsr("cert.req").getCsr();
    final X509Certificate cert = ca.sign(csr);
    CA.export(cert).saveCertificate("cert.cer");

    // On Windows, install the resulting certificate, with its private key
    // certreq -accept cert.cer
    acceptCert("cert.cer");

    // Configure SSL
    final String certThumbprint = getThumbPrint(cert);
    // netsh http add sslcert ipport=0.0.0.0:443 certhash=... appid={...}
    configureSsl(certThumbprint, UUID.randomUUID().toString());

    // NB: https binding has been set in appveyor.yml

    // Test the HTTPS connection
    final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    keystore.load(null, null);
    keystore.setCertificateEntry("cert", caCert);
    final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keystore, null).build();
    try (CloseableHttpClient httpClient = HttpClients.custom().setSslcontext(sslContext).build();
        CloseableHttpResponse response = httpClient.execute(new HttpGet("https://localhost/"))) {
      final HttpEntity entity = response.getEntity();
      System.out.println(EntityUtils.toString(entity));
    }

  }

  private void installTrustedCert(final String certFileName) throws IOException,
      InterruptedException {
    final Process process = new ProcessBuilder("certutil", "-enterprise", "-addstore", "ROOT",
        certFileName)
        .redirectError(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT)
        .start();

    process.waitFor();
  }

  private void generateCsr() throws IOException, InterruptedException {
    final Process process = new ProcessBuilder("certreq", "-new",
        "src\\test\\resources\\csr_template.inf", "cert.req")
        .redirectError(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT)
        .start();

    process.waitFor();
  }

  private void acceptCert(final String certFileName) throws IOException,
      InterruptedException {
    final Process process = new ProcessBuilder("certreq", "-accept", certFileName)
        .redirectError(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT)
        .start();

    process.waitFor();
  }

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
    for (int i = 0; i < bytes.length; ++i) {
      buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
      buf.append(hexDigits[bytes[i] & 0x0f]);
    }
    return buf.toString();
  }

}
