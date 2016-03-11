package io.github.olivierlemasle.caweb.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.olivierlemasle.ca.DistinguishedName;

public class Certificate {
  private DistinguishedName subject;

  public Certificate() {
    // Jackson
  }

  @JsonCreator
  public Certificate(@JsonProperty("subject") final DistinguishedName subject) {
    this.subject = subject;
  }

  @JsonProperty
  public DistinguishedName getSubject() {
    return subject;
  }

}
