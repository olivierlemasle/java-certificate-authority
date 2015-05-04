package io.github.olivierlemasle.ca;

import io.github.olivierlemasle.ca.ext.CertExtension;

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
}
