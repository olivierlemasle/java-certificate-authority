package io.github.olivierlemasle.ca;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.joda.time.DateTime;

public interface Signer {
  public SignerWithSerial setSerialNumber(final BigInteger serialNumber);

  public SignerWithSerial setRandomSerialNumber();

  public static interface SignerWithSerial {
    public X509Certificate sign();

    public SignerWithSerial setNotBefore(final DateTime notBefore);

    public SignerWithSerial setNotAfter(final DateTime notAfter);

    public SignerWithSerial validDuringYears(final int years);

    public SignerWithSerial addExtension(final CertExtension extension);

    public SignerWithSerial addExtension(ASN1ObjectIdentifier oid, boolean isCritical,
        ASN1Encodable value);
  }

  public static class CertExtension {
    private final ASN1ObjectIdentifier oid;
    private final boolean isCritical;
    private final ASN1Encodable value;

    public CertExtension(final ASN1ObjectIdentifier oid, final boolean isCritical,
        final ASN1Encodable value) {
      this.oid = oid;
      this.isCritical = isCritical;
      this.value = value;
    }

    public ASN1ObjectIdentifier getOid() {
      return oid;
    }

    public boolean isCritical() {
      return isCritical;
    }

    public ASN1Encodable getValue() {
      return value;
    }
  }
}
