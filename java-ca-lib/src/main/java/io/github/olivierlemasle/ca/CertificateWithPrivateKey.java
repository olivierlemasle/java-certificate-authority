package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;

public interface CertificateWithPrivateKey extends Certificate {

  public KeyStore saveInPkcs12Keystore(String alias);

  public void exportPkcs12(final String keystorePath, final char[] keystorePassword,
      final String alias);

  public void exportPkcs12(final File keystoreFile, final char[] keystorePassword,
      final String alias);
}
