package io.github.olivierlemasle.caweb.health;

import com.codahale.metrics.health.HealthCheck;

public class MyHealthCheck extends HealthCheck {

  public MyHealthCheck() {
  }

  @Override
  protected Result check() throws Exception {
    return Result.healthy("foo");
  }
}
