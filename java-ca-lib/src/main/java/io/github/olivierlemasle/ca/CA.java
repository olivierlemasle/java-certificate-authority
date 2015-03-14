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

  public static CertificateAuthorityImpl loadCertificateAuthority(final String keystorePath,
      final char[] password, final char[] privateKeyPassword) {
    return CaLoader.loadCertificateAuthority(keystorePath, password, privateKeyPassword);
  }

  public static CertificateAuthorityImpl loadCertificateAuthority(final File keystoreFile,
      final char[] password, final char[] privateKeyPassword) {
    return CaLoader.loadCertificateAuthority(keystoreFile, password, privateKeyPassword);
  }

  public static CertificateAuthorityImpl loadCertificateAuthority(final KeyStore keystore,
      final char[] privateKeyPassword) {
    return CaLoader.loadCertificateAuthority(keystore, privateKeyPassword);
  }

  public static Name getName(final String dirName) {
    return new NameImpl(dirName);
  }
}
