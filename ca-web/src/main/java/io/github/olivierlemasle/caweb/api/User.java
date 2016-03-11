package io.github.olivierlemasle.caweb.api;

import java.util.List;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
  @NotEmpty
  private String name;

  @NotEmpty
  @Email
  private String email;

  @NotEmpty
  private List<String> roles;

  public User() {
    // Jackson
  }

  public User(final String name, final String email, final List<String> roles) {
    this.name = name;
    this.email = email;
    this.roles = roles;
  }

  @JsonProperty
  public String getName() {
    return name;
  }

  @JsonProperty
  public String getEmail() {
    return email;
  }

  @JsonProperty
  public List<String> getRoles() {
    return roles;
  }
}
