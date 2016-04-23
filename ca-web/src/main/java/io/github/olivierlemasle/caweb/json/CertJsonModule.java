package io.github.olivierlemasle.caweb.json;

import java.security.cert.X509Certificate;

import com.fasterxml.jackson.databind.module.SimpleModule;

import io.github.olivierlemasle.ca.DistinguishedName;

public class CertJsonModule extends SimpleModule {
  private static final long serialVersionUID = 5830481586565293705L;

  public CertJsonModule() {
    addSerializer(DistinguishedName.class, new DnSerializer());
    addDeserializer(DistinguishedName.class, new DnDeserializer());

    addSerializer(X509Certificate.class, new CertSerializer());

  }

}
