package io.github.olivierlemasle.ca;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

final class CaLoader {
  private CaLoader() {
  }

  static CertificateAuthorityImpl loadCertificateAuthority(final String keystorePath,
      final char[] password, final String alias) {
    final File file = new File(keystorePath);
    return loadCertificateAuthority(file, password, alias);
  }

  static CertificateAuthorityImpl loadCertificateAuthority(final File keystoreFile,
      final char[] password, final String alias) {
    try {
      final KeyStore keystore = KeyStore.getInstance(CertificateAuthorityImpl.KEYSTORE_TYPE);
      try (InputStream stream = new FileInputStream(keystoreFile)) {
        keystore.load(stream, password);
        return loadCertificateAuthority(keystore, alias);
      }
    } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
      throw new CaException(e);
    }
  }

  static CertificateAuthorityImpl loadCertificateAuthority(final KeyStore keystore,
      final String alias) {
    try {
      final Certificate caCertificate = keystore.getCertificate(alias);
      final PrivateKey caPrivateKey = (PrivateKey) keystore.getKey(alias, null);
      if (caCertificate == null || caPrivateKey == null)
        throw new CaException("Keystore does not contain certificate and key for alias " + alias);
      return new CertificateAuthorityImpl((X509Certificate) caCertificate, caPrivateKey);
    } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
      throw new CaException(e);
    }
  }

}
