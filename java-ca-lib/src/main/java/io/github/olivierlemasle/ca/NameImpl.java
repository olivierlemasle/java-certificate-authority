package io.github.olivierlemasle.ca;

import org.bouncycastle.asn1.x500.X500Name;

class NameImpl implements Name {
  private final X500Name name;

  NameImpl(final X500Name name) {
    this.name = name;
  }

  NameImpl(final String dirName) {
    this.name = new X500Name(dirName);
  }

  @Override
  public X500Name getX500Name() {
    return name;
  }

}
