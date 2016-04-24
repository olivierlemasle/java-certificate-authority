package io.github.olivierlemasle.caweb.json;

import static io.github.olivierlemasle.ca.CA.dn;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.ca.DnBuilder;

class DnDeserializer extends StdDeserializer<DistinguishedName> {

  private static final long serialVersionUID = 9192735092617403295L;

  public DnDeserializer() {
    super(DistinguishedName.class);
  }

  @Override
  public DistinguishedName deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
    if (p.getCurrentToken() == JsonToken.START_OBJECT) {

      DnBuilder dnBuilder = dn();
      for (JsonToken t = p.nextToken(); t != JsonToken.END_OBJECT; t = p.nextToken()) {
        if (t != JsonToken.FIELD_NAME)
          throw JsonMappingException.from(p, "Expecting " + JsonToken.FIELD_NAME);

        final String fieldName = p.getCurrentName();

        switch (fieldName) {
        case "cn":
          dnBuilder = dnBuilder.setCn(p.nextTextValue());
          break;
        case "ou":
          dnBuilder = dnBuilder.setOu(p.nextTextValue());
          break;
        case "c":
          dnBuilder = dnBuilder.setC(p.nextTextValue());
          break;
        case "l":
          dnBuilder = dnBuilder.setL(p.nextTextValue());
          break;
        case "o":
          dnBuilder = dnBuilder.setO(p.nextTextValue());
          break;
        case "st":
          dnBuilder = dnBuilder.setSt(p.nextTextValue());
          break;
        default:
          p.nextToken();
        }

      }
      return dnBuilder.build();
    } else
      return dn(p.getValueAsString());
  }

}
