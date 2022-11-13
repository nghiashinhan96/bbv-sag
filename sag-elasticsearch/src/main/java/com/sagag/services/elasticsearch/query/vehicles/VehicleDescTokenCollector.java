package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.elasticsearch.enums.VehicleDescTokenType;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class VehicleDescTokenCollector {

  private List<String> numericPsTokens = new ArrayList<>();

  private List<String> alphaNumericPsTokens = new ArrayList<>();

  private List<String> numericTokens = new ArrayList<>();

  private List<String> alphabetTokens = new ArrayList<>();

  private List<String> numericKwTokens = new ArrayList<>();

  private List<String> alphaNumericKwTokens = new ArrayList<>();

  public static Collector<String, VehicleDescTokenCollector,
    Map<VehicleDescTokenType, List<String>>> toMap() {
    return Collector.of(VehicleDescTokenCollector::new,
        VehicleDescTokenCollector::add,
        VehicleDescTokenCollector::merge,
        VehicleDescTokenCollector::finish);
  }

  private void add(String element) {
    if (isNumericPsToken(element)) {
      StringBuilder normalizeToken = new StringBuilder(normalize(element, VehicleConstants.HORSE_POWER_SUFFIX));
      numericPsTokens.add(normalizeToken.toString());
      alphaNumericPsTokens.add(normalizeToken.append(VehicleConstants.HORSE_POWER_SUFFIX).toString());
      return;
    }
    if (isNumericKwToken(element)) {
      StringBuilder normalizeToken = new StringBuilder(normalize(element, VehicleConstants.HORSE_POWER_SUFFIX));
      numericKwTokens.add(normalizeToken.toString());
      alphaNumericKwTokens.add(normalizeToken.append(VehicleConstants.KW_POWER_SUFFIX).toString());
      return;
    }
    if (StringUtils.isNumeric(element)) {
      numericTokens.add(element);
      return;
    }
    alphabetTokens.add(element);
  }

  private VehicleDescTokenCollector merge(VehicleDescTokenCollector other) {
    numericPsTokens.addAll(other.numericPsTokens);
    alphaNumericPsTokens.addAll(other.alphaNumericPsTokens);
    numericTokens.addAll(other.numericTokens);
    alphabetTokens.addAll(other.alphabetTokens);
    numericKwTokens.addAll(other.numericKwTokens);
    alphaNumericKwTokens.addAll(other.alphaNumericKwTokens);
    return this;
  }

  private Map<VehicleDescTokenType, List<String>> finish() {
    EnumMap<VehicleDescTokenType, List<String>> result = new EnumMap<>(VehicleDescTokenType.class);
    result.put(VehicleDescTokenType.NUMERIC_PS, numericPsTokens);
    result.put(VehicleDescTokenType.ALPHANUMERIC_PS, alphaNumericPsTokens);
    result.put(VehicleDescTokenType.NUMERIC, numericTokens);
    result.put(VehicleDescTokenType.ALPHABET, alphabetTokens);
    result.put(VehicleDescTokenType.NUMERIC_KW, numericKwTokens);
    result.put(VehicleDescTokenType.ALPHANUMERIC_KW, alphaNumericKwTokens);
    return result;
  }

  private String normalize(String token, String suffix) {
    return new StringBuilder(token).substring(0, token.length() - suffix.length());
  }

  private boolean isNumericPsToken(String token) {
    return token.matches(VehicleConstants.HORSE_POWER_REGEX);
  }

  private boolean isNumericKwToken(String token) {
    return token.matches(VehicleConstants.KW_POWER_REGEX);
  }
}
