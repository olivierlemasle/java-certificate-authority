package io.github.olivierlemasle.ca;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;

class X500PrincipalDnImpl implements DistinguishedName {
  private final X500Principal x500Principal;

  X500PrincipalDnImpl(final X500Principal principal) {
    this.x500Principal = principal;
  }

  X500PrincipalDnImpl(final String name) {
    this.x500Principal = new X500Principal(name);
  }

  @Override
  public X500Name getX500Name() {
    return X500Name.getInstance(x500Principal.getEncoded());
  }

  @Override
  public X500Principal getX500Principal() {
    return x500Principal;
  }

  @Override
  public byte[] getEncoded() {
    return x500Principal.getEncoded();
  }

  @Override
  public String getName() {
    return x500Principal.toString();
  }

  @Override
  public String toString() {
    return getName();
  }
}
