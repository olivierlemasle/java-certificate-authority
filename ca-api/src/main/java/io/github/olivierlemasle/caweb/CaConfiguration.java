package io.github.olivierlemasle.caweb;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.github.olivierlemasle.caweb.api.User;

public class CaConfiguration extends Configuration {

  @NotEmpty
  @Valid
  private List<User> users;

  @JsonProperty
  public List<User> getUsers() {
    return users;
  }

  @JsonProperty
  public void setUsers(final List<User> users) {
    this.users = users;
  }

}
