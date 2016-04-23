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

  @Valid
  private Keystore keystore;

  @JsonProperty
  public List<User> getUsers() {
    return users;
  }

  @JsonProperty
  public void setUsers(final List<User> users) {
    this.users = users;
  }

  @JsonProperty
  public Keystore getKeystore() {
    return keystore;
  }

  @JsonProperty
  public void setKeystore(Keystore keystore) {
    this.keystore = keystore;
  }

  public static class Keystore {
    @NotEmpty
    private String path;
    @NotEmpty
    private String password;

    @JsonProperty
    public String getPath() {
      return path;
    }

    @JsonProperty
    public void setPath(String path) {
      this.path = path;
    }

    @JsonProperty
    public String getPassword() {
      return password;
    }

    @JsonProperty
    public void setPassword(String password) {
      this.password = password;
    }

  }

}
