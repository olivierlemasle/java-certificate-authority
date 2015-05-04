package io.github.olivierlemasle.ca.ext;

import io.github.olivierlemasle.ca.ext.Names.NameType;

import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralNames;

public class CrlDistPointExtension extends CertExtension {

  CrlDistPointExtension(final DistributionPoint... points) {
    super(Extension.cRLDistributionPoints, false, new CRLDistPoint(points));
  }

  public static CrlDistPointExtension create(final String uri) {
    return create(NameType.URI, uri);
  }

  public static CrlDistPointExtension create(final NameType type, final String name) {
    final GeneralNames crl = type.generalNames(name);
    final DistributionPoint p = new DistributionPoint(null, null, crl);
    return new CrlDistPointExtension(p);
  }

}
