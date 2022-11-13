package com.sagag.services.mdm.utils;

import lombok.Builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
public class UnicatUriBuilder {
  private String uri;
  private String fs;
  private String customerNr;
  private String username;
  private String language;
  private String articleInfo;
  private String sid;
  private String katnr;
  private String katnrhash;

  public String getUri() throws URISyntaxException {
    URIBuilder uriBuilder = new URIBuilder(new URI(uri));
    List<NameValuePair> queryParams = new ArrayList<NameValuePair>();

    Optional.ofNullable(fs).filter(StringUtils::isNotBlank)
        .map(fs -> new BasicNameValuePair("fs", fs)).ifPresent(queryParams::add);
    Optional.ofNullable(customerNr).filter(StringUtils::isNotBlank)
        .map(fs -> new BasicNameValuePair("customerNr", customerNr)).ifPresent(queryParams::add);
    Optional.ofNullable(username).filter(StringUtils::isNotBlank)
        .map(fs -> new BasicNameValuePair("username", username)).ifPresent(queryParams::add);
    Optional.ofNullable(sid).filter(StringUtils::isNotBlank)
        .map(fs -> new BasicNameValuePair("sid", sid)).ifPresent(queryParams::add);
    Optional.ofNullable(language).filter(StringUtils::isNotBlank)
        .map(fs -> new BasicNameValuePair("language", language)).ifPresent(queryParams::add);
    Optional.ofNullable(articleInfo).filter(StringUtils::isNotBlank)
        .map(fs -> new BasicNameValuePair("articleInfo", articleInfo)).ifPresent(queryParams::add);
    Optional.ofNullable(katnr).filter(StringUtils::isNotBlank)
    .map(fs -> new BasicNameValuePair("katnr", katnr)).ifPresent(queryParams::add);
    Optional.ofNullable(katnrhash).filter(StringUtils::isNotBlank)
    .map(fs -> new BasicNameValuePair("katnrhash", katnrhash)).ifPresent(queryParams::add);
    uriBuilder.setParameters(queryParams);

    return uriBuilder.toString();
  }

}
