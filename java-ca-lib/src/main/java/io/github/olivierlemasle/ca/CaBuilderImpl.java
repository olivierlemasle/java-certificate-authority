package io.github.olivierlemasle.ca;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
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

class CaBuilderImpl implements CaBuilder {
  private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
  private Name caName;

  @Override
  public CaBuilderImpl setName(final Name caName) {
    this.caName = caName;
    return this;
  }

  @Override
  public CertificateAuthority build() {
    try {
      final KeyPair pair = KeysUtil.generateKeyPair();
      final PublicKey publicKey = pair.getPublic();
      final PrivateKey privateKey = pair.getPrivate();

      final ContentSigner sigGen = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
          .build(privateKey);
      final SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo
          .getInstance(publicKey.getEncoded());

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
      final X509Certificate caCertificate = new JcaX509CertificateConverter()
          .setProvider(CA.PROVIDER_NAME)
          .getCertificate(certHolder);

      return new CertificateAuthorityImpl(caCertificate, privateKey);
    } catch (OperatorCreationException | CertificateException e) {
      throw new CaException(e);
    }
  }

}
