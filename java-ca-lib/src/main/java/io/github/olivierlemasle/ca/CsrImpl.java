package io.github.olivierlemasle.ca;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
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
  public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
    return request.getSubjectPublicKeyInfo();
  }

}
