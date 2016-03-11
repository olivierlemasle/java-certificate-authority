package io.github.olivierlemasle.caweb;

import static io.github.olivierlemasle.ca.CA.dn;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.caweb.health.MyHealthCheck;
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
    // nothing to do yet
  }

  @Override
  public void run(final CaConfiguration configuration,
      final Environment environment) {
    final UsersResource resource = new UsersResource(configuration.getUsers());
    final MyHealthCheck healthCheck = new MyHealthCheck();
    final SimpleModule module = new SimpleModule();
    module.addSerializer(DistinguishedName.class, new DnSerializer());
    module.addDeserializer(DistinguishedName.class, new DnDeserializer());
    environment.getObjectMapper().registerModule(module);
    environment.healthChecks().register("template", healthCheck);
    environment.jersey().register(resource);
  }

  public static class DnSerializer extends JsonSerializer<DistinguishedName> {

    @Override
    public void serialize(final DistinguishedName dn, final JsonGenerator gen,
        final SerializerProvider serializers) throws IOException, JsonProcessingException {
      gen.writeString(dn.toString());
    }

  }

  public static class DnDeserializer extends JsonDeserializer<DistinguishedName> {

    @Override
    public DistinguishedName deserialize(final JsonParser p, final DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return dn(p.getValueAsString());
    }

  }

}
