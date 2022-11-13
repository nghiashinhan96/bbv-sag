package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.elasticsearch.enums.VehicleDescTokenType;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class VehicleDescTokenCollectorTest {

  List<String> alphabetToken = Arrays.asList("abc", "dang", "120ou", "bmw");
  List<String> numericToken = Arrays.asList("100", "010", "300", "123");
  List<String> alphaNumericPs = Arrays.asList("100PS", "200ps", "150pS", "120Ps");
  List<String> numericPs = Arrays.asList("100", "200", "150", "120");
  List<String> alphaNumericKw = Arrays.asList("100KW", "200kw", "150kW", "120Kw");
  List<String> numericKw = Arrays.asList("100", "200", "150", "120");

  @Test
  public void shouldSplitToCorrectTokens() {
    Map<VehicleDescTokenType, List<String>> nonParallelResult
        = Stream.of(alphabetToken, numericToken, alphaNumericPs, alphaNumericKw).flatMap(Collection::stream)
            .collect(VehicleDescTokenCollector.toMap());

    Map<VehicleDescTokenType, List<String>> parallelResult
        = Stream.of(alphabetToken, numericToken, alphaNumericPs, alphaNumericKw)
            .flatMap(Collection::stream).parallel().collect(VehicleDescTokenCollector.toMap());

    shouldSplitToCorrectTokens_Verifier(nonParallelResult);
    shouldSplitToCorrectTokens_Verifier(parallelResult);
  }

  private void shouldSplitToCorrectTokens_Verifier(Map<VehicleDescTokenType, List<String>> input) {
    input.get(VehicleDescTokenType.ALPHABET).stream().map(alphabetToken::contains)
        .forEach(Assert::assertTrue);
    input.get(VehicleDescTokenType.NUMERIC).stream().map(numericToken::contains)
        .forEach(Assert::assertTrue);
    input.get(VehicleDescTokenType.NUMERIC_PS).stream().map(numericPs::contains)
        .forEach(Assert::assertTrue);
    input.get(VehicleDescTokenType.NUMERIC_KW).stream().map(numericKw::contains)
        .forEach(Assert::assertTrue);
    alphaNumericPs.stream().map(String::toUpperCase)
        .map(input.get(VehicleDescTokenType.ALPHANUMERIC_PS)::contains).forEach(Assert::assertTrue);
    alphaNumericKw.stream().map(String::toUpperCase)
        .map(input.get(VehicleDescTokenType.ALPHANUMERIC_KW)::contains).forEach(Assert::assertTrue);
  }

}
