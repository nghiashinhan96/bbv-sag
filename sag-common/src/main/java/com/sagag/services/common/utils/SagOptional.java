package com.sagag.services.common.utils;

import lombok.Data;

import java.util.Optional;
import java.util.function.Consumer;

@Data(staticConstructor = "of")
public class SagOptional<T> {
  private final Optional<T> optional;

  public SagOptional<T> ifPresent(Consumer<T> c) {
    optional.ifPresent(c);
    return this;
  }

  public void ifNotPresent(Runnable r) {
    if (!optional.isPresent()) {
      r.run();
    }
  }

  public void orElse(Runnable r) {
    ifNotPresent(r);
  }
}
