package io.github.olivierlemasle.ca;

import com.google.inject.AbstractModule;

class CaModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CertificateAuthorityCreator.class).to(CertificateAuthorityCreatorImpl.class);
  }

}
