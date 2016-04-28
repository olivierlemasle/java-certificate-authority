package io.github.olivierlemasle.caweb.api;

import static io.github.olivierlemasle.ca.CA.dn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.ca.RootCertificate;

public class CertificateAuthority {
  private RootCertificate caCertificate;

  public CertificateAuthority() {
    // Jackson
  }

  public CertificateAuthority(final RootCertificate caCertificate) {
    this.caCertificate = caCertificate;
  }

  @JsonIgnore
  public RootCertificate getCaCertificate() {
    return caCertificate;
  }

  @JsonProperty
  public DistinguishedName getSubject() {
    return dn(caCertificate.getX509Certificate().getSubjectX500Principal());
  }

}
