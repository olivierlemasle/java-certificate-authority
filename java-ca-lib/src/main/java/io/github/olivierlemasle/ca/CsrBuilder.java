package io.github.olivierlemasle.ca;

public interface CsrBuilder {

  public CSR generateRequest(DistinguishedName name);

}
