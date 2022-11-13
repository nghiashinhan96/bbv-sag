package com.sagag.services.rest.controller.anonymous;

import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.hazelcast.provider.CacheDataProvider;
import com.sagag.services.rest.theme.CssThemeSettingsLoader;

import io.swagger.annotations.Api;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

/**
 * Settings controller class.
 */
@RestController
@RequestMapping(value = "/")
@Api(tags = "Anonymous Settings APIs")
@Slf4j
public class SettingsController {

  @Autowired
  private OrganisationCollectionService orgCollectionService;
  @Autowired
  private CacheDataProvider cacheDataProvider;
  @Autowired
  @Qualifier("collectionsCssThemeSettingsLoaderImpl")
  private CssThemeSettingsLoader cssThemeSettingsLoader;

  @GetMapping(value = "/settings/{collection}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, String> loadSettings(
      @PathVariable("collection") final String collectionShortName) throws ResultNotFoundException {
    final Map<String, String> orgSettings =
        orgCollectionService.findSettingsByCollectionShortname(collectionShortName);
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(orgSettings,
        "Invalid request, the affiliate is invalid!");
  }

  @PutMapping(value = "/cache/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasPermission(#hashPass, 'isSecretKey')")
  public ResponseEntity<String> refreshCache(@RequestParam("key") final String hashPass,
      @RequestParam(defaultValue = "postman") final String source) {
    log.debug("Refreshing cache from source = {}", source);
    cacheDataProvider.refreshCacheData();
    return ResponseEntity.ok().body("Refresh cache is sucessfull");
  }

  @GetMapping(value = "/css/theme", produces = MediaType.ALL_VALUE)
  public ResponseEntity<byte[]> getCollectionsTheme(
      @RequestParam("collectionId") final String collectionId) throws IOException {

    final HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, "text/css;charset=UTF-8");
    headers.set(HttpHeaders.DATE, Calendar.getInstance().getTime().toString());

    return ResponseEntity.ok().headers(headers)
        .body(cssThemeSettingsLoader.initializeThemeSettings(collectionId));
  }
}
