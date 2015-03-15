package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;

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

  public static DnBuilder dn() {
    return new DnBuilderImpl();
  }

  public static DistinguishedName dn(final String dirName) {
    return new BcX500NameDnImpl(dirName);
  }

  public static DistinguishedName dn(final X500Name name) {
    return new BcX500NameDnImpl(name);
  }

  public static DistinguishedName dn(final X500Principal principal) {
    return new X500PrincipalDnImpl(principal);
  }
}
