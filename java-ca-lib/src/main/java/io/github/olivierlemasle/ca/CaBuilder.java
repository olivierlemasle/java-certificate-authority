package io.github.olivierlemasle.ca;

public interface CaBuilder {

  public CaBuilder setName(final Name caName);

  public CertificateAuthority build();

}
