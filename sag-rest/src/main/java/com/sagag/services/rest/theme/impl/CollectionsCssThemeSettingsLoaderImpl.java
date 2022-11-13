package com.sagag.services.rest.theme.impl;

import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.services.common.aspect.LogExecutionTime;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
public class CollectionsCssThemeSettingsLoaderImpl extends AbstractCssThemeSettingsLoader
    implements InitializingBean {

  private static final String THEME_FILE_URL = "/public/css/theme.css";

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  private String cssThemeValue;

  @Override
  public void afterPropertiesSet() throws Exception {
    if (!StringUtils.isBlank(cssThemeValue)) {
      return;
    }

    try (FileInputStream fis = new FileInputStream(new ClassPathResource(
        THEME_FILE_URL).getFile())) {
      this.cssThemeValue = IOUtils.toString(fis, StandardCharsets.UTF_8);
    } catch (FileNotFoundException ex) {
      log.warn("Synchronize theme got error", ex);
      throw ex;
    }
  }

  @Override
  @LogExecutionTime
  public byte[] initializeThemeSettings(String collectionShortname) throws IOException {
    log.debug("Initializing theme settings with collectionId = {}", collectionShortname);
    final String after = syncAffiliateSettings(getCollectionSettings(collectionShortname),
        cssThemeValue);
    return after.getBytes();
  }

  private Map<String, String> getCollectionSettings(String collectionShortname) {
    return orgCollectionService.findSettingsByCollectionShortname(collectionShortname);
  }

}
