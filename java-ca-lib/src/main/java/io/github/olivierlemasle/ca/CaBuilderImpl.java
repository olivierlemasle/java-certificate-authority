package io.github.olivierlemasle.ca;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.ZonedDateTime;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;

import io.github.olivierlemasle.ca.Signer.SignerWithSerial;
import io.github.olivierlemasle.ca.ext.CrlDistPointExtension;
import io.github.olivierlemasle.ca.ext.KeyUsageExtension;
import io.github.olivierlemasle.ca.ext.KeyUsageExtension.KeyUsage;

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
  public CaBuilder setNotBefore(final ZonedDateTime notBefore) {
    signer.setNotBefore(notBefore);
    return this;
  }

  @Override
  public CaBuilder setNotAfter(final ZonedDateTime notAfter) {
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
        KeyUsage.CRL_SIGN)
        );

    if (crlUri != null) {
      signer.addExtension(CrlDistPointExtension.create(crlUri));
    }

    // This is a CA
    signer.addExtension(Extension.basicConstraints, false, new BasicConstraints(true));

    final X509Certificate caCertificate = signer.sign();

    return new CertificateAuthorityImpl(caCertificate, pair.getPrivate());
  }

}
