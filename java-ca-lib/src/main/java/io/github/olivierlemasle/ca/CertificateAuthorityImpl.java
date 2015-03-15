package io.github.olivierlemasle.ca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.joda.time.DateTime;

class CertificateAuthorityImpl implements CertificateAuthority {
  private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
  static final String KEYSTORE_TYPE = "PKCS12";

  private final Certificate caCertificate;
  private final X509CertificateHolder caCertificateHolder;
  private final PrivateKey caPrivateKey;

  CertificateAuthorityImpl(final Certificate caCertificate, final PrivateKey caPrivateKey) {
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
      final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE, CA.PROVIDER_NAME);
      keyStore.load(null, null);

      final Certificate[] chain = new Certificate[] { caCertificate };
      keyStore.setKeyEntry(alias, caPrivateKey, null, chain);

      return keyStore;
    } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
        | NoSuchProviderException e) {
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
  public CSR generateRequest() {
    final KeyPair pair = KeysUtil.generateKeyPair();
    try {
      final PrivateKey privateKey = pair.getPrivate();
      final PublicKey publicKey = pair.getPublic();
      final X500Name name = new X500NameBuilder()
          .addRDN(BCStyle.CN, "test")
          .build();
      final ContentSigner signGen = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
          .build(privateKey);
      final PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(
          name, publicKey);
      final PKCS10CertificationRequest csr = builder.build(signGen);
      return new CSR() {

        @Override
        public PKCS10CertificationRequest getPKCS10CertificationRequest() {
          return csr;
        }
      };
    } catch (final OperatorCreationException e) {
      throw new CaException(e);
    }
  }

  @Override
  public X509Certificate sign(final CSR request) {
    final PKCS10CertificationRequest inputCSR = request.getPKCS10CertificationRequest();
    try {
      final ContentSigner sigGen = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
          .build(caPrivateKey);

      final SubjectPublicKeyInfo subPubKeyInfo = inputCSR.getSubjectPublicKeyInfo();

      final DateTime today = DateTime.now().withTimeAtStartOfDay();
      final X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder(
          caCertificateHolder.getSubject(),
          new BigInteger("1"),
          today.toDate(),
          today.plusYears(10).toDate(),
          inputCSR.getSubject(),
          subPubKeyInfo);

      final X509CertificateHolder holder = myCertificateGenerator.build(sigGen);
      final X509Certificate cert = new JcaX509CertificateConverter()
          .setProvider(CA.PROVIDER_NAME)
          .getCertificate(holder);

      cert.checkValidity();
      cert.verify(caCertificate.getPublicKey());

      return cert;
    } catch (final OperatorCreationException | CertificateException | InvalidKeyException
        | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
      throw new CaException(e);
    }
  }

}
