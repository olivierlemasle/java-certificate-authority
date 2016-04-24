package io.github.olivierlemasle.caweb;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.olivierlemasle.caweb.CaConfiguration.Keystore;
import io.github.olivierlemasle.caweb.cli.CreateCertificate;
import io.github.olivierlemasle.caweb.cli.CreateSelfSignedCertificate;
import io.github.olivierlemasle.caweb.health.MyHealthCheck;
import io.github.olivierlemasle.caweb.json.CertJsonModule;
import io.github.olivierlemasle.caweb.resources.CertificateAuthoritiesResource;
import io.github.olivierlemasle.caweb.resources.UsersResource;

public class CaApplication extends Application<CaConfiguration> {
  public static void main(final String[] args) throws Exception {
    new CaApplication().run(args);
  }

  @Override
  public String getName() {
    return "ca-web";
  }

  @Override
  public void initialize(final Bootstrap<CaConfiguration> bootstrap) {
    bootstrap.addCommand(new CreateSelfSignedCertificate());
    bootstrap.addCommand(new CreateCertificate());
  }

  @Override
  public void run(final CaConfiguration configuration,
      final Environment environment) {
    environment.getObjectMapper().registerModule(new CertJsonModule());

    // Users
    final UsersResource resource = new UsersResource(configuration.getUsers());
    environment.jersey().register(resource);

    // CA
    final Keystore keystore = configuration.getKeystore();
    final CertificateAuthoritiesResource caResource = new CertificateAuthoritiesResource(keystore);
    environment.jersey().register(caResource);

    // HealthCheck
    final MyHealthCheck healthCheck = new MyHealthCheck();
    environment.healthChecks().register("keystore", healthCheck);

    // Enable CORS
    final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    // Configure CORS parameters
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }

}
