package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.UnicatProfile;
import com.sagag.services.common.utils.HashUtils;
import com.sagag.services.domain.sag.external.ExternalUserSession;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.mdm.utils.UnicatUriBuilder;
import com.sagag.services.service.api.UnicatService;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

@Service
@Slf4j
@UnicatProfile
public class UnicatServiceImpl implements UnicatService {

  private static final String UNICAT_DATE_FORMAT_FOR_HASH = "yyyy-MM-dd'T'HH:00";

  @Value("${external.webservice.unicat.catalog_uri}")
  private String catalogUri;

  @Value("${unicat.callback_url}")
  private String callBackUrl;

  @Value("${external.webservice.unicat.company_password}")
  private String companyPassword;

  @Value("${external.webservice.unicat.salt}")
  private String salt;

  @Value("${external.webservice.unicat.katnr}")
  private String katnr;

  @Autowired
  private UserCacheService userCacheService;

  @Override
  public String getUnicatCatalogUri(UserInfo user) {
    Assert.notNull(user, "User info can not be null");
    String customerNr = user.getCustNr();
    String username = user.getUsername();

    String language = user.getUserLocale().getLanguage();
    String sid = UUID.randomUUID().toString();
    cacheSidForUser(user, sid);
    return buildUnicatURI(customerNr, sid, username, language);
  }

  private void cacheSidForUser(UserInfo user, String sid) {
    if (!Objects.isNull(user.getExternalUserSession())) {
      user.getExternalUserSession().setSid(sid);
    } else {
      user.setExternalUserSession(ExternalUserSession.builder().sid(sid).build());
    }
    userCacheService.put(user);
  }

  private String buildUnicatURI(String customerNr, String sid, String username, String language) {
    if (StringUtils.isBlank(catalogUri)) {
      return StringUtils.EMPTY;
    }
    try {
      StringBuilder fs = new StringBuilder();
      DateTime now = new LocalDateTime(DateTimeZone.forID("Europe/Belgrade")).toDateTime();
      if (Stream.of(customerNr, username, companyPassword).allMatch(StringUtils::isNotBlank)) {
        fs.append(customerNr).append(companyPassword).append(username).append(companyPassword)
            .append(now.toString());
      }

      StringBuilder katnrHashBuilder = new StringBuilder();
      String katnrHashMd5 = null;
      if (!Objects.isNull(katnr) && !Objects.isNull(salt)) {
        DateTimeFormatter df = DateTimeFormat.forPattern(UNICAT_DATE_FORMAT_FOR_HASH);
        katnrHashBuilder.append(katnr).append(now.toString(df)).append(salt);
        katnrHashMd5 = HashUtils.hashMD5(katnrHashBuilder.toString());
      }

      return UnicatUriBuilder.builder().uri(catalogUri).sid(sid)
          .fs(Base64Utils.encodeToString(fs.toString().getBytes())).username(username)
          .customerNr(customerNr).language(language).articleInfo(callBackUrl).katnr(katnr)
          .katnrhash(katnrHashMd5).build().getUri();
    } catch (URISyntaxException e) {
      log.error("Cannot generate UNICAT Url cause :", e);
    }
    return StringUtils.EMPTY;
  }

}
