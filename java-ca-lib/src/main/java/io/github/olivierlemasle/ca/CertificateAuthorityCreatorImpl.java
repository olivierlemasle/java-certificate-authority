package io.github.olivierlemasle.ca;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.joda.time.DateTime;

class CertificateAuthorityCreatorImpl implements CertificateAuthorityCreator {

  @Override
  public KeyStore createCertificateAuthority(final Name caName, final char[] caPrivateKeyPassword) {
    try {
      final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null, null);

      final KeyPair pair = KeysUtil.generateKeyPair();

      final ContentSigner sigGen = new JcaContentSignerBuilder("SHA256withRSA").build(
          pair.getPrivate());
      final SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(
          pair.getPublic().getEncoded());

      final DateTime today = DateTime.now().withTimeAtStartOfDay();
      final X500Name x500Name = caName.getX500Name();
      final X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
          x500Name,
          BigInteger.ONE,
          today.toDate(),
          today.plusYears(1).toDate(),
          x500Name,
          subPubKeyInfo);

      final X509CertificateHolder certHolder = certBuilder.build(sigGen);
      final X509Certificate cert = new JcaX509CertificateConverter()
          .setProvider("BC")
          .getCertificate(certHolder);

      keyStore.setCertificateEntry("cert", cert);
      final Certificate[] chain = new Certificate[] { cert };
      final Entry entry = new KeyStore.PrivateKeyEntry(pair.getPrivate(), chain);
      final ProtectionParameter protParam = new KeyStore.PasswordProtection(caPrivateKeyPassword);
      keyStore.setEntry("key", entry, protParam);

      return keyStore;

    } catch (KeyStoreException | OperatorCreationException | CertificateException
        | NoSuchAlgorithmException | IOException e) {
      throw new CaException(e);
    }
  }

  @Override
  public CertificateAuthority loadCertificateAuthority(final String keystorePath,
      final char[] password, final char[] caPrivateKeyPassword) {
    final File file = new File(keystorePath);
    return loadCertificateAuthority(file, password, caPrivateKeyPassword);
  }

  @Override
  public CertificateAuthority loadCertificateAuthority(final File keystoreFile,
      final char[] password, final char[] caPrivateKeyPassword) {
    try {
      final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
      try (InputStream stream = new FileInputStream(keystoreFile)) {
        keystore.load(stream, password);
        return loadCertificateAuthority(keystore, caPrivateKeyPassword);
      }
    } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
      throw new CaException(e);
    }
  }

  @Override
  public CertificateAuthority loadCertificateAuthority(final KeyStore keystore,
      final char[] caPrivateKeyPassword) {
    return new CertificateAuthorityImpl(keystore, caPrivateKeyPassword);
  }

}
