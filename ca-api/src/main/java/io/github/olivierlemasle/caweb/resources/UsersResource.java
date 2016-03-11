package io.github.olivierlemasle.caweb.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import io.github.olivierlemasle.caweb.api.User;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
  private final List<User> users;

  public UsersResource(final List<User> users) {
    this.users = users;
  }

  @GET
  @Timed
  public List<User> list() {
    return users;
  }
}
