package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public interface CertificateAuthority {

  public KeyStore saveInPkcs12Keystore(String alias);

  public void exportPkcs12(final String keystorePath, final char[] keystorePassword,
      final String alias);

  public void exportPkcs12(final File keystoreFile, final char[] keystorePassword,
      final String alias);

  public CSR generateRequest();

  public X509Certificate sign(final CSR request);
}
