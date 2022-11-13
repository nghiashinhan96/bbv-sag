package com.sagag.services.rest.theme;

import static org.junit.Assert.assertThat;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.rest.app.RestApplication;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Integration tests class for non-secured public REST APIs.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest
public class CssThemeSettingsLoaderIT {

  @Autowired
  @Qualifier("defaultCssThemeSettingsLoaderImpl")
  private CssThemeSettingsLoader themeLoader;

  @Value("/public/css/matik-at.css")
  private String matikAtThemeFile;

  @Value("/public/css/derendinger-at.css")
  private String ddatThemeFile;

  @Test
  public void shouldGenerateThemeForDerendingerAt() throws Exception {
    generateAndAssertThemeFilesContents(SupportedAffiliate.DERENDINGER_AT);
  }

  @Test
  public void shouldGenerateThemeForMatikAt() throws Exception {
    generateAndAssertThemeFilesContents(SupportedAffiliate.MATIK_AT);
  }

  private void generateAndAssertThemeFilesContents(SupportedAffiliate affiliate)
      throws IOException {
    themeLoader.initializeThemeSettings(affiliate.getAffiliate());
    final String contents = loadGeneratedThemeContents(affiliate);
    assertThat(contents, Matchers.not(Matchers.isEmptyOrNullString()));
    assertThat(contents.matches("\"[{]\\w+[}]\""), Matchers.not(true));
    assertBackground(contents, affiliate);
  }

  private void assertBackground(String contents, SupportedAffiliate affiliate) {
    if (affiliate.isDat()) {
      assertThat(contents, Matchers.stringContainsInOrder(
          Arrays.asList("background: url(../images/background/bg-der.jpg")));
    } else if (affiliate.isMatikAt()) {
      assertThat(contents, Matchers.stringContainsInOrder(
          Arrays.asList("background: url(../images/background/bg-matik.jpg")));
    }
  }

  private String loadGeneratedThemeContents(SupportedAffiliate affiliate) throws IOException {
    final File afterSyncThemeFile = new ClassPathResource(selectFile(affiliate)).getFile();
    return FileUtils.readFileToString(afterSyncThemeFile, StandardCharsets.UTF_8);
  }

  private String selectFile(SupportedAffiliate affiliate) {
    if (affiliate.isDat()) {
      return ddatThemeFile;
    } else if (affiliate.isMatikAt()) {
      return matikAtThemeFile;
    }
    throw new IllegalArgumentException("Unsupported theme file for input affiliate.");
  }

}
