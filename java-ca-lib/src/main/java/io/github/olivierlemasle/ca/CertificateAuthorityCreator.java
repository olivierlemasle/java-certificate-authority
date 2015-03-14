package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;

public interface CertificateAuthorityCreator {

  public KeyStore createCertificateAuthority(Name caName, char[] caPrivateKeyPassword);

  public CertificateAuthority loadCertificateAuthority(String keystorePath, char[] password,
      char[] caPrivateKeyPassword);

  public CertificateAuthority loadCertificateAuthority(File keystoreFile, char[] password,
      char[] caPrivateKeyPassword);

  public CertificateAuthority loadCertificateAuthority(KeyStore keystore,
      char[] caPrivateKeyPassword);
}
