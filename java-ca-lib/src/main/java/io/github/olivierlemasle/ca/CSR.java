package io.github.olivierlemasle.ca;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public interface CSR {

  public DistinguishedName getSubject();

  public SubjectPublicKeyInfo getSubjectPublicKeyInfo();

}
