package io.github.olivierlemasle.ca;

import java.time.ZonedDateTime;

public interface CaBuilder {

  public CaBuilder setNotBefore(final ZonedDateTime notBefore);

  public CaBuilder setNotAfter(final ZonedDateTime notAfter);

  public CaBuilder validDuringYears(final int years);

  public CaBuilder setCrlUri(final String crlUri);

  public CertificateAuthority build();

}
