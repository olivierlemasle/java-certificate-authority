package io.github.olivierlemasle.ca;

import java.io.File;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;

/**
 * Root of the <i>Certification Authority</i> DSL.
 * 
 * <p>
 * All methods in this class are static. They should be statically imported:
 * </p>
 * <p>
 * {@code import static io.github.olivierlemasle.ca.CA.*;}
 * </p>
 */
public final class CA {

  /**
   * No instantiation
   */
  private CA() {
  }

  /**
   * Creates a builder object used to create a new {@link CertificateAuthority}.
   * 
   * @param caName
   *          Distinguished Name of the authority certificate
   * @return a builder object
   * @see CertificateAuthority
   */
  public static CaBuilder createCertificateAuthority(final DistinguishedName caName) {
    return new CaBuilderImpl(caName);
  }

  /**
   * Loads an existing {@link CertificateAuthority} from a {@code PKCS12}
   * keystore.
   * 
   * @param keystorePath
   *          path of the PKCS12 keystore
   * @param password
   *          password of the keystore
   * @param alias
   *          CA certificate alias in the keystore
   * @return the loaded {@link CertificateAuthority}
   */
  public static CertificateAuthority loadCertificateAuthority(final String keystorePath,
      final char[] password, final String alias) {
    return CaLoader.loadCertificateAuthority(keystorePath, password, alias);
  }

  /**
   * Loads an existing {@link CertificateAuthority} from a {@code PKCS12}
   * keystore.
   * 
   * @param keystoreFile
   *          PKCS12 keystore file
   * @param password
   *          password of the keystore
   * @param alias
   *          CA certificate alias in the keystore
   * @return the loaded {@link CertificateAuthority}
   */
  public static CertificateAuthority loadCertificateAuthority(final File keystoreFile,
      final char[] password, final String alias) {
    return CaLoader.loadCertificateAuthority(keystoreFile, password, alias);
  }

  /**
   * Loads an existing {@link CertificateAuthority} from a {@code PKCS12}
   * keystore.
   * 
   * @param keystoreFile
   *          PKCS12 keystore, already "loaded"
   * @param alias
   *          CA certificate alias in the keystore
   * @return the loaded {@link CertificateAuthority}
   */
  public static CertificateAuthority loadCertificateAuthority(final KeyStore keystore,
      final String alias) {
    return CaLoader.loadCertificateAuthority(keystore, alias);
  }

  /**
   * Creates a builder object used to create a new {@link CSR} (Certificate
   * Signing Request).
   * 
   * @return a builder object
   * @see CSR
   */
  public static CsrBuilder createCsr() {
    return new CsrBuilderImpl();
  }

  /**
   * Loads a {@link CSR} (Certificate Signing Request) from a file.
   * 
   * @param csrFile
   *          CSR file
   * @return the CSR object
   */
  public static CsrLoader loadCsr(final File csrFile) {
    return new CsrLoaderImpl(csrFile);
  }

  /**
   * Loads a {@link CSR} (Certificate Signing Request) from a file.
   * 
   * @param csrFileName
   *          CSR file path
   * @return the CSR object
   */
  public static CsrLoader loadCsr(final String csrFileName) {
    return new CsrLoaderImpl(csrFileName);
  }

  /**
   * Creates a builder object used to build a {@link DistinguishedName}.
   * 
   * @return a builder object
   * @see DistinguishedName
   */
  public static DnBuilder dn() {
    return new DnBuilderImpl();
  }

  /**
   * Builds a {@link DistinguishedName} from a {@link String}.
   * 
   * @param name
   * @return the {@link DistinguishedName} object
   */
  public static DistinguishedName dn(final String name) {
    return new BcX500NameDnImpl(name);
  }

  /**
   * Builds a {@link DistinguishedName} from a Bouncy Castle {@link X500Name}
   * object.
   * 
   * @param name
   * @return the {@link DistinguishedName} object
   */
  public static DistinguishedName dn(final X500Name name) {
    return new BcX500NameDnImpl(name);
  }

  /**
   * Builds a {@link DistinguishedName} from a
   * {@link javax.security.auth.x500.X500Principal}.
   * 
   * @param name
   * @return the {@link DistinguishedName} object
   */
  public static DistinguishedName dn(final X500Principal principal) {
    return new X500PrincipalDnImpl(principal);
  }

  /**
   * Returns a utility object used to export a {@link X509Certificate} to the
   * PEM format.
   * 
   * @param certificate
   * @return a utility object
   */
  public static CertificateExporter export(final X509Certificate certificate) {
    return new CertificateExporterImpl(certificate);
  }
}
