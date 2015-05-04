package io.github.olivierlemasle.ca;

import io.github.olivierlemasle.ca.Signer.SignerWithSerial;
import io.github.olivierlemasle.ca.ext.CrlDistPointExtension;
import io.github.olivierlemasle.ca.ext.ExtKeyUsageExtension;
import io.github.olivierlemasle.ca.ext.KeyUsageExtension;
import io.github.olivierlemasle.ca.ext.KeyUsageExtension.KeyUsage;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.joda.time.DateTime;

class CaBuilderImpl implements CaBuilder, SerialNumberGenerator {
  private static final int SERIAL_LENGTH = 128;

  private String crlUri = null;

  private final KeyPair pair;
  private final SignerWithSerial signer;
  private final SecureRandom random = new SecureRandom();

  CaBuilderImpl(final DistinguishedName caName) {
    pair = KeysUtil.generateKeyPair();
    signer = new SignerImpl(this, pair, caName, pair.getPublic(), caName)
        .setRandomSerialNumber();
  }

  @Override
  public CaBuilder setNotBefore(final DateTime notBefore) {
    signer.setNotBefore(notBefore);
    return this;
  }

  @Override
  public CaBuilder setNotAfter(final DateTime notAfter) {
    signer.setNotAfter(notAfter);
    return this;
  }

  @Override
  public CaBuilder validDuringYears(final int years) {
    signer.validDuringYears(years);
    return this;
  }

  @Override
  public CaBuilder setCrlUri(final String crlUri) {
    this.crlUri = crlUri;
    return this;
  }

  @Override
  public BigInteger generateRandomSerialNumber() {
    return new BigInteger(SERIAL_LENGTH, random);
  }

  @Override
  public CertificateAuthority build() {
    signer.addExtension(KeyUsageExtension.create(
        KeyUsage.KEY_CERT_SIGN,
        KeyUsage.DIGITAL_SIGNATURE,
        KeyUsage.CRL_SIGN,
        KeyUsage.KEY_ENCIPHERMENT,
        KeyUsage.KEY_AGREEMENT,
        KeyUsage.DATA_ENCIPHERMENT,
        KeyUsage.NON_REPUDIATION)
        );

    if (crlUri != null) {
      signer.addExtension(CrlDistPointExtension.create(crlUri));
    }

    signer.addExtension(ExtKeyUsageExtension.create(
        KeyPurposeId.id_kp_clientAuth,
        KeyPurposeId.id_kp_codeSigning,
        KeyPurposeId.id_kp_serverAuth,
        KeyPurposeId.id_kp_emailProtection)
        );

    // This is a CA
    signer.addExtension(Extension.basicConstraints, false, new BasicConstraints(true));

    final X509Certificate caCertificate = signer.sign();

    return new CertificateAuthorityImpl(caCertificate, pair.getPrivate());
  }

}
