package io.github.olivierlemasle.ca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

class CertificateWithPrivateKeyImpl extends CertificateImpl implements CertificateWithPrivateKey {
  static final String KEYSTORE_TYPE = "PKCS12";

  private final PrivateKey privateKey;

  CertificateWithPrivateKeyImpl(final X509Certificate certificate, final PrivateKey privateKey) {
    super(certificate);
    this.privateKey = privateKey;
  }

  protected PrivateKey getPrivateKey() {
    return privateKey;
  }

  @Override
  public KeyStore saveInPkcs12Keystore(final String alias) {
    try {
      // init keystore
      final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
      keyStore.load(null, null);

      final X509Certificate certificate = getX509Certificate();
      final Certificate[] chain = new Certificate[] { certificate };
      keyStore.setKeyEntry(alias, privateKey, null, chain);

      return keyStore;
    } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
      throw new CaException(e);
    }
  }

  @Override
  public void exportPkcs12(final String keystorePath, final char[] keystorePassword,
      final String alias) {
    final File file = new File(keystorePath);
    exportPkcs12(file, keystorePassword, alias);
  }

  @Override
  public void exportPkcs12(final File keystoreFile, final char[] keystorePassword,
      final String alias) {
    final KeyStore keystore = saveInPkcs12Keystore(alias);
    try {
      try (OutputStream stream = new FileOutputStream(keystoreFile)) {
        keystore.store(stream, keystorePassword);
      }
    } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e) {
      throw new CaException(e);
    }
  }

}
