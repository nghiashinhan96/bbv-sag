package com.sagag.services.common.utils;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SagOptionalTest {

  @Test
  public void ifPresent_shouldApplyFunctionInsideIfPresent_givenNotEmptyOptional()
      throws Exception {
    List<String> test = new ArrayList<>();
    test.add("original");
    SagOptional.of(Optional.of(test)).ifPresent((item) -> {
      item.add("ifPresent");
    });
    assertThat(test.get(1), Matchers.is("ifPresent"));
  }

  @Test
  public void ifPresent_shouldApplyFunctionInsideIfNotPresent_givenEmptyOptional()
      throws Exception {
    List<String> test = new ArrayList<>();
    test.add("original");
    SagOptional.of(Optional.empty()).ifNotPresent(() -> {
      test.add("ifNotPresent");
    });
    assertThat(test.get(1), Matchers.is("ifNotPresent"));
  }

  @Test
  public void ifPresent_shouldApplyFunctionInsideOrElse_givenEmptyOptional() throws Exception {
    List<String> test = new ArrayList<>();
    test.add("original");
    SagOptional.of(Optional.empty()).orElse(() -> {
      test.add("orElse");
    });
    assertThat(test.get(1), Matchers.is("orElse"));
  }

  @Test
  public void ifPresentOrElse_shouldApplyFunctionInsideIfPresent_givenNotEmptyOptional()
      throws Exception {
    List<String> test = new ArrayList<>();
    test.add("original");
    SagOptional.of(Optional.of(test)).ifPresent((item) -> {
      item.add("ifPresent");
    }).orElse(() -> {
      test.add("orElse");
    });
    assertThat(test.get(1), Matchers.is("ifPresent"));
  }

  @Test
  public void ifPresentOrElse_shouldApplyFunctionInsideOrElse_givenEmptyOptional()
      throws Exception {
    List<String> test = new ArrayList<>();
    test.add("original");
    SagOptional.of(Optional.empty()).ifPresent((item) -> {
      test.add("ifPresent");
    }).orElse(() -> {
      test.add("orElse");
    });
    assertThat(test.get(1), Matchers.is("orElse"));
  }
}
