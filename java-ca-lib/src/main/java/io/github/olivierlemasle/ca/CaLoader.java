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

final class CaLoader {
  private CaLoader() {
  }

  static CertificateAuthorityImpl loadCertificateAuthority(final String keystorePath,
      final char[] password, final char[] privateKeyPassword) {
    final File file = new File(keystorePath);
    return loadCertificateAuthority(file, password, privateKeyPassword);
  }

  static CertificateAuthorityImpl loadCertificateAuthority(final File keystoreFile,
      final char[] password, final char[] privateKeyPassword) {
    try {
      final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
      try (InputStream stream = new FileInputStream(keystoreFile)) {
        keystore.load(stream, password);
        return loadCertificateAuthority(keystore, privateKeyPassword);
      }
    } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
      throw new CaException(e);
    }
  }

  static CertificateAuthorityImpl loadCertificateAuthority(final KeyStore keystore,
      final char[] privateKeyPassword) {
    try {
      final Certificate caCertificate = keystore
          .getCertificate(CertificateAuthorityImpl.CERTIFICATE_ALIAS);
      final PrivateKey caPrivateKey = (PrivateKey) keystore.getKey(
          CertificateAuthorityImpl.PRIVATE_KEY_ALIAS, privateKeyPassword);
      return new CertificateAuthorityImpl(caCertificate, caPrivateKey);
    } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
      throw new CaException(e);
    }
  }

}
