package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;

public interface CertificateAuthorityCreator {

  public KeyStore createCertificateAuthority(char[] caPrivateKeyPassword);

  public CertificateAuthority loadCertificateAuthority(final String keystorePath,
      final char[] password, char[] caPrivateKeyPassword);

  public CertificateAuthority loadCertificateAuthority(final File keystoreFile,
      final char[] password, char[] caPrivateKeyPassword);

  public CertificateAuthority loadCertificateAuthority(final KeyStore keystore,
      char[] caPrivateKeyPassword);
}
