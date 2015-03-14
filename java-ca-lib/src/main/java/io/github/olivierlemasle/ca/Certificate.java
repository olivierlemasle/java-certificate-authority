package io.github.olivierlemasle.ca;

import java.security.cert.X509Certificate;

public interface Certificate {
  X509Certificate getX509Certificate();
}
