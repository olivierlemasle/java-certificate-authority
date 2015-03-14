package io.github.olivierlemasle.ca;

public interface CertificateAuthority {

  public CSR generateRequest();

  public Certificate sign(CSR request);
}
