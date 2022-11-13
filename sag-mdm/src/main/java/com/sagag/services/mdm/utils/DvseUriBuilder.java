package com.sagag.services.mdm.utils;

import lombok.Builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import java.util.Optional;

@Builder
public class DvseUriBuilder {

  private static final String SALES_SESSION_ID_PARAM = "&ESID=";

  private String uri;

  private String sid;

  private String username;

  private String password;

  private String saleId;

  public String getUri() {
    final StrBuilder uriBuilder = new StrBuilder(uri);

    Optional.ofNullable(sid).filter(StringUtils::isNotBlank)
    .ifPresent(uriBuilder::append);

    Optional.ofNullable(username).filter(StringUtils::isNotBlank)
    .ifPresent(item -> uriBuilder.append("&user=").append(item));

    Optional.ofNullable(password).filter(StringUtils::isNotBlank)
    .ifPresent(item -> uriBuilder.append("&pw=").append(item));

    Optional<String> saleIdOpt = Optional.ofNullable(saleId);
    if (!StringUtils.containsIgnoreCase(uri, SALES_SESSION_ID_PARAM)) {
      saleIdOpt.filter(StringUtils::isNotBlank)
      .ifPresent(item -> uriBuilder.append(SALES_SESSION_ID_PARAM).append(item));
    } else {
      saleIdOpt
      .filter(item -> uriBuilder.indexOf(item) != 0)
      .ifPresent(item -> uriBuilder.replaceFirst(
          uriBuilder.substring(uriBuilder.indexOf(SALES_SESSION_ID_PARAM)),
          SALES_SESSION_ID_PARAM + StringUtils.trim(item)));
    }

    return uriBuilder.toString();
  }

}
