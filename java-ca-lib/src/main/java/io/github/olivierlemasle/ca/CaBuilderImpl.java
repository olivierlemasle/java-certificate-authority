package io.github.olivierlemasle.ca;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.joda.time.DateTime;

class CaBuilderImpl implements CaBuilder {
  private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
  private DistinguishedName caName;
  private DateTime notBefore;
  private DateTime notAfter;

  CaBuilderImpl() {
    notBefore = DateTime.now();
    notAfter = notBefore.plusYears(1);
  }

  @Override
  public CaBuilderImpl setName(final DistinguishedName caName) {
    this.caName = caName;
    return this;
  }

  @Override
  public CaBuilder setNotBefore(final DateTime notBefore) {
    this.notBefore = notBefore;
    return this;
  }

  @Override
  public CaBuilder setNotAfter(final DateTime notAfter) {
    this.notAfter = notAfter;
    return this;
  }

  @Override
  public CaBuilder validDuringYears(final int years) {
    notAfter = notBefore.plusYears(years);
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

      final X500Name x500Name = caName.getX500Name();
      final X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
          x500Name,
          BigInteger.ONE,
          notBefore.toDate(),
          notAfter.toDate(),
          x500Name,
          subPubKeyInfo)
          .addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.keyCertSign |
              KeyUsage.digitalSignature |
              KeyUsage.cRLSign |
              KeyUsage.keyEncipherment |
              KeyUsage.dataEncipherment |
              KeyUsage.nonRepudiation |
              KeyUsage.keyAgreement))
          .addExtension(
              Extension.cRLDistributionPoints,
              false,
              new CRLDistPoint(
                  new DistributionPoint[] { new DistributionPoint(null, null,
                      new GeneralNames(
                          new GeneralName(GeneralName.uniformResourceIdentifier,
                              "http://test.com/crl"))
                      ) }
              ))
          .addExtension(Extension.extendedKeyUsage, false, new ExtendedKeyUsage(
              new KeyPurposeId[] {
                  KeyPurposeId.id_kp_clientAuth,
                  KeyPurposeId.id_kp_codeSigning,
                  KeyPurposeId.id_kp_serverAuth,
                  KeyPurposeId.id_kp_emailProtection
              }))
          .addExtension(Extension.basicConstraints, false, new BasicConstraints(true));

      final X509CertificateHolder certHolder = certBuilder.build(sigGen);
      final X509Certificate caCertificate = new JcaX509CertificateConverter()
          .setProvider(CA.PROVIDER_NAME)
          .getCertificate(certHolder);

      return new CertificateAuthorityImpl(caCertificate, privateKey);
    } catch (OperatorCreationException | CertificateException | CertIOException e) {
      throw new CaException(e);
    }
  }

}
