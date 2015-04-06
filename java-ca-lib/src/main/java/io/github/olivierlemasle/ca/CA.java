package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;

public final class CA {

  private CA() {
  }

  public static CaBuilder createCertificateAuthority(final DistinguishedName caName) {
    return new CaBuilderImpl(caName);
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

  public static CsrBuilder newCsr() {
    return new CsrBuilderImpl();
  }

  public static CsrLoader loadCsr(final File csrFile) {
    return new CsrLoaderImpl(csrFile);
  }

  public static CsrLoader loadCsr(final String csrFileName) {
    return new CsrLoaderImpl(csrFileName);
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

  public static CertificateExporter export(final X509Certificate certificate) {
    return new CertificateExporterImpl(certificate);
  }
}
