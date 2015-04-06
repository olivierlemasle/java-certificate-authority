package io.github.olivierlemasle.ca;

import org.joda.time.DateTime;

public interface CaBuilder {

  public CaBuilder setNotBefore(final DateTime notBefore);

  public CaBuilder setNotAfter(final DateTime notAfter);

  public CaBuilder validDuringYears(final int years);

  public CaBuilder setCrlUri(final String crlUri);

  public CertificateAuthority build();

}
