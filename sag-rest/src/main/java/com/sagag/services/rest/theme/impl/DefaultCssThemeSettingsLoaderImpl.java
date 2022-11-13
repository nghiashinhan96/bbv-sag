package com.sagag.services.rest.theme.impl;

import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.services.common.enums.SupportedAffiliate;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
@Deprecated
public class DefaultCssThemeSettingsLoaderImpl extends AbstractCssThemeSettingsLoader {

  @Value("/public/css/theme.css")
  private String themeTemplateLocation;

  @Value("/public/css/technomag.css")
  private String technomagThemeLocation;

  @Value("/public/css/derendinger-ch.css")
  private String derendSwissThemeLocation;

  @Value("/public/css/derendinger-at.css")
  private String derendAustriaThemeLocation;

  @Value("/public/css/matik-at.css")
  private String matikAustriaThemeLocation;

  @Value("/public/css/matik-ch.css")
  private String matikSwissThemeLocation;

  @Value("/public/css/rbe.css")
  private String rbeThemeLocation;

  @Value("/public/css/wbb.css")
  private String wbbThemeLocation;

  @Autowired
  private OrganisationService orgService;

  /**
   * Initializes the theme settings for affiliate.
   *
   * @param affiliate the affiliate.
   * @throws IOException throws when no theme template file found.
   */
  @Override
  public byte[] initializeThemeSettings(final String affiliate) throws IOException {
    log.debug("Initializing theme settings with affiliate = {}", affiliate);
    final Map<String, String> orgSettings = orgService.findOrgSettingsByShortName(affiliate);
    if (MapUtils.isEmpty(orgSettings)) {
      log.error("Invalid affiliate {}", affiliate);
      throw new IllegalArgumentException("Invalid request, the affiliate must not be blank!");
    }
    final File themeFile = new ClassPathResource(themeTemplateLocation).getFile();
    final String contents =
        IOUtils.toString(new FileInputStream(themeFile), StandardCharsets.UTF_8);

    final String after = syncAffiliateSettings(orgSettings, contents);
    final File syncThemeFile = findSyncThemeFile(affiliate);
    FileUtils.writeStringToFile(syncThemeFile, after, StandardCharsets.UTF_8, false);
    return doSafelyCssFileByteArray(syncThemeFile);
  }

  private File findSyncThemeFile(final String affiliate) throws IOException {
    final SupportedAffiliate aff = SupportedAffiliate.fromDesc(affiliate);
    switch (aff) {
      case TECHNOMAG:
        return new ClassPathResource(technomagThemeLocation).getFile();
      case DERENDINGER_CH:
        return new ClassPathResource(derendSwissThemeLocation).getFile();
      case DERENDINGER_AT:
        return new ClassPathResource(derendAustriaThemeLocation).getFile();
      case MATIK_AT:
        return new ClassPathResource(matikAustriaThemeLocation).getFile();
      case MATIK_CH:
        return new ClassPathResource(matikSwissThemeLocation).getFile();
      case RBE:
        return new ClassPathResource(rbeThemeLocation).getFile();
      case WBB:
        return new ClassPathResource(wbbThemeLocation).getFile();
      default:
        throw new IllegalArgumentException("Invalid affiliate.");
    }
  }

}
