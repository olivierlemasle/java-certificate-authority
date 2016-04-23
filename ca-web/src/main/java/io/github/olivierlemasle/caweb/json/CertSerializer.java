package io.github.olivierlemasle.caweb.json;

import java.io.IOException;
import java.security.cert.X509Certificate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

class CertSerializer extends JsonSerializer<X509Certificate> {

  @Override
  public void serialize(final X509Certificate certificate, final JsonGenerator gen,
      final SerializerProvider serializers) throws IOException, JsonProcessingException {
    gen.writeNull();
  }

}
