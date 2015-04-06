package io.github.olivierlemasle.ca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.joda.time.DateTime;

class CertificateAuthorityImpl implements CertificateAuthority {
  private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
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
  public X509Certificate sign(final CSR csr) {
    try {
      final ContentSigner sigGen = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
          .build(caPrivateKey);

      final PublicKey publicKey = csr.getPublicKey();
      final SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(
          publicKey.getEncoded());

      final BigInteger serial = new BigInteger(SERIAL_LENGTH, random);

      final JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
      final DateTime today = DateTime.now().withTimeAtStartOfDay();
      final X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder(
          caCertificateHolder.getSubject(),
          serial,
          today.toDate(),
          today.plusYears(10).toDate(),
          csr.getSubject().getX500Name(),
          subPubKeyInfo)
          .addExtension(Extension.authorityKeyIdentifier, false,
              extUtils.createAuthorityKeyIdentifier(caCertificate.getPublicKey()))
          .addExtension(Extension.subjectKeyIdentifier, false,
              extUtils.createSubjectKeyIdentifier(publicKey));

      final X509CertificateHolder holder = myCertificateGenerator.build(sigGen);
      final X509Certificate cert = new JcaX509CertificateConverter()
          .getCertificate(holder);

      cert.checkValidity();
      cert.verify(caCertificate.getPublicKey());

      return cert;
    } catch (final OperatorCreationException | CertificateException | InvalidKeyException
        | NoSuchAlgorithmException | NoSuchProviderException | SignatureException | CertIOException e) {
      throw new CaException(e);
    }
  }

}
