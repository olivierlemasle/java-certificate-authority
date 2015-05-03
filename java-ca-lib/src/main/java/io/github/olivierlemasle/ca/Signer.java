package io.github.olivierlemasle.ca;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import org.joda.time.DateTime;

public interface Signer {
  public SignerWithSerial setSerialNumber(final BigInteger serialNumber);

  public SignerWithSerial setRandomSerialNumber();

  public static interface SignerWithSerial {
    public X509Certificate sign();

    public SignerWithSerial setNotBefore(final DateTime notBefore);

    public SignerWithSerial setNotAfter(final DateTime notAfter);

    public SignerWithSerial validDuringYears(final int years);
  }
}