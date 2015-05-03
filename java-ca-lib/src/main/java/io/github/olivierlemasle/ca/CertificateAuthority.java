package io.github.olivierlemasle.ca;

import java.io.File;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public interface CertificateAuthority {

  public KeyStore saveInPkcs12Keystore(String alias);

  public void exportPkcs12(final String keystorePath, final char[] keystorePassword,
      final String alias);

  public void exportPkcs12(final File keystoreFile, final char[] keystorePassword,
      final String alias);

  public X509Certificate getCaCertificate();

  public BigInteger generateRandomSerialNumber();

  public Signer signCsr(final CSR request);

  public static interface Signer {
    public SignerWithSerial setSerialNumber(final BigInteger serialNumber);

    public SignerWithSerial setRandomSerialNumber();
  }

  public static interface SignerWithSerial {
    public X509Certificate sign();
  }
}
