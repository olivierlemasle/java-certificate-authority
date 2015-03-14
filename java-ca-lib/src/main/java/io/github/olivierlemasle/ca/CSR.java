package io.github.olivierlemasle.ca;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public interface CSR {

  PKCS10CertificationRequest getPKCS10CertificationRequest();
}
