package io.github.olivierlemasle.ca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.cert.X509CertificateHolder;

class CertificateAuthorityImpl implements CertificateAuthority {
  static final String KEYSTORE_TYPE = "PKCS12";
  private static final int SERIAL_LENGTH = 128;

  private final X509Certificate caCertificate;
  private final X509CertificateHolder caCertificateHolder;
  private final PrivateKey caPrivateKey;
  private final SecureRandom random = new SecureRandom();

  CertificateAuthorityImpl(final X509Certificate caCertificate, final PrivateKey caPrivateKey) {
    this.caPrivateKey = caPrivateKey;
    this.caCertificate = caCertificate;
    try {
      this.caCertificateHolder = new X509CertificateHolder(caCertificate.getEncoded());
    } catch (CertificateEncodingException | IOException e) {
      throw new CaException(e);
    }
  }

  @Override
  public KeyStore saveInPkcs12Keystore(final String alias) {
    try {
      // init keystore
      final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
      keyStore.load(null, null);

      final Certificate[] chain = new Certificate[] { caCertificate };
      keyStore.setKeyEntry(alias, caPrivateKey, null, chain);

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

  @Override
  public X509Certificate getCaCertificate() {
    return caCertificate;
  }

  @Override
  public BigInteger generateRandomSerialNumber() {
    return new BigInteger(SERIAL_LENGTH, random);
  }

  @Override
  public Signer signCsr(final CSR request) {
    return new SignerImpl(this, caCertificate, caCertificateHolder, caPrivateKey,
        request.getPublicKey(), request.getSubject());
  }

}
