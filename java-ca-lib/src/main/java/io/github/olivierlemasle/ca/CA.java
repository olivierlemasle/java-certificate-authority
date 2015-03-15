package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;

public final class CA {
  /**
   * Bouncy Castle Provider
   */
  static final String PROVIDER_NAME = "BC";

  private CA() {
  }

  public static CaBuilder init() {
    return new CaBuilderImpl();
  }

  public static CertificateAuthority loadCertificateAuthority(final String keystorePath,
      final char[] password, final String alias) {
    return CaLoader.loadCertificateAuthority(keystorePath, password, alias);
  }

  public static CertificateAuthority loadCertificateAuthority(final File keystoreFile,
      final char[] password, final String alias) {
    return CaLoader.loadCertificateAuthority(keystoreFile, password, alias);
  }

  public static CertificateAuthority loadCertificateAuthority(final KeyStore keystore,
      final String alias) {
    return CaLoader.loadCertificateAuthority(keystore, alias);
  }

  public static Name getName(final String dirName) {
    return new NameImpl(dirName);
  }
}
