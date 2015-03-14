package io.github.olivierlemasle.ca;

import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

  public static void main(final String[] args) {
    final Provider bc = new BouncyCastleProvider();
    Security.insertProviderAt(bc, 1);

    final Injector injector = Guice.createInjector(new CaModule());
    final CertificateAuthorityCreator c = injector.getInstance(CertificateAuthorityCreator.class);
    final KeyStore keyStore = c.createCertificateAuthority("password".toCharArray());
    final CertificateAuthority ca = c.loadCertificateAuthority(keyStore, "password".toCharArray());
    final CSR csr = ca.generateRequest();
    final Certificate cert = ca.sign(csr);
  }

}
