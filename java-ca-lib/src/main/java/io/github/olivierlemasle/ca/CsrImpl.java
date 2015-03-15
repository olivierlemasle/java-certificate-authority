package io.github.olivierlemasle.ca;

import java.security.PublicKey;

import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

class CsrImpl implements CSR {
  private final PKCS10CertificationRequest request;

  public CsrImpl(final PKCS10CertificationRequest request) {
    this.request = request;
  }

  @Override
  public DistinguishedName getSubject() {
    return new BcX500NameDnImpl(request.getSubject());
  }

  @Override
  public PublicKey getPublicKey() {
    try {
      return (new JcaPEMKeyConverter()).getPublicKey(request.getSubjectPublicKeyInfo());
    } catch (final PEMException e) {
      throw new CaException(e);
    }
  }

}
