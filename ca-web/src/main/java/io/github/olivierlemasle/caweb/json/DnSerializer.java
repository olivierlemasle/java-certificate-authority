package io.github.olivierlemasle.caweb.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.github.olivierlemasle.ca.DistinguishedName;

class DnSerializer extends JsonSerializer<DistinguishedName> {

  @Override
  public void serialize(final DistinguishedName dn, final JsonGenerator gen,
      final SerializerProvider serializers) throws IOException {
    gen.writeString(dn.toString());
  }

}
