package com.sagag.services.haynespro.builder;

import com.sagag.services.common.utils.VehicleUtils;
import com.sagag.services.haynespro.request.HaynesProAccessUrlRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class HaynesProAccessUrlRequestBuilder {

  private static final String USER_TYPE_ATTR = "userType";

  @NonNull
  private HaynesProAccessUrlRequest request;

  public Map<String, String> build() {
    final Map<String, String> props = new LinkedHashMap<>();

    // Parameters are added as constants
    props.putIfAbsent("systemOfMeasurement", "0");
    props.putIfAbsent("currencyCode", request.getCurrencyCode());
    props.putIfAbsent("interface", "TOUCH");

    Optional.ofNullable(request.getLanguage())
      .filter(Objects::nonNull)
      .ifPresent(item -> props.putIfAbsent("languageCode", StringUtils.lowerCase(item)));

    Optional.ofNullable(request.getSubject())
      .filter(StringUtils::isNotBlank)
      .ifPresent(item -> props.putIfAbsent("subject", item));

    Optional.ofNullable(request.getOptionalParameterByKeyOrDefault(USER_TYPE_ATTR))
      .ifPresent(item -> props.putIfAbsent(USER_TYPE_ATTR, item));

    // #1089 : hourlyRate && VAT
    Optional.ofNullable(request.getHourlyRate())
      .filter(Objects::nonNull)
      .map(String::valueOf)
      .ifPresent(item -> {
        props.putIfAbsent("labourrate_mechanicalValue", item);
        props.putIfAbsent("labourrate_bodyworksValue", item);
        props.putIfAbsent("labourrate_electricalValue", item);
      });

    Optional.ofNullable(request.getVatRate())
      .filter(Objects::nonNull)
      .map(String::valueOf)
      .ifPresent(item -> {
        props.putIfAbsent("vatRate_mechanicalValue", item);
        props.putIfAbsent("vatRate_bodyworksValue", item);
        props.putIfAbsent("vatRate_electricalValue", item);
      });

    Optional.ofNullable(request.getKType())
      .filter(StringUtils::isNotBlank)
      .ifPresent(item -> props.putIfAbsent("tecdocKTypNr", item));

    Optional.ofNullable(request.getMotorId())
      .filter(StringUtils::isNotBlank)
      .ifPresent(item -> props.putIfAbsent("motnr", item));

    Optional.ofNullable(request.getCallbackUrl())
      .filter(StringUtils::isNotBlank)
      .map(item -> new StringBuilder(item)
        .append("?uuid=")
        .append(StringUtils.defaultString(request.getUuid()))
        .append("&vehId=")
        .append(VehicleUtils.buildVehicleId(request.getKType(), request.getMotorId()))
        .toString())
      .ifPresent(item -> props.putIfAbsent("estimateButtonPostbackUrl", item));

    Optional.ofNullable(request.getCallbackBtnText())
      .filter(StringUtils::isNotBlank)
      .ifPresent(item -> props.putIfAbsent("estimateButtonText", item));
    return props;
  }

}
