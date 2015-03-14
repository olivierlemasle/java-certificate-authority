package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public interface CertificateAuthority {

  public KeyStore saveInKeystore(final char[] privateKeyPassword);

  public void saveToKeystoreFile(final String keystorePath, final char[] keystorePassword,
      final char[] privateKeyPassword);

  public void saveToKeystoreFile(final File keystoreFile, final char[] keystorePassword,
      final char[] privateKeyPassword);

  public CSR generateRequest();

  public X509Certificate sign(final CSR request);
}
