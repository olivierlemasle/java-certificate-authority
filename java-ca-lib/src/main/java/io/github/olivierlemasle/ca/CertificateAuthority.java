package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public interface CertificateAuthority extends SerialNumberGenerator {

  public KeyStore saveInPkcs12Keystore(String alias);

  public void exportPkcs12(final String keystorePath, final char[] keystorePassword,
      final String alias);

  public void exportPkcs12(final File keystoreFile, final char[] keystorePassword,
      final String alias);

  public X509Certificate getCaCertificate();

  public Signer signCsr(final CSR request);

}
