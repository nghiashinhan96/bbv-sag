package com.sagag.services.common.locale;

import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.enums.LanguageCode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class LocaleContextHelperTest {

  private static final String[] CH_AND_AT_LANGUAGES = { "de", "it", "fr" };

  private static final String[] CS_LANGUAGES = { "cs", "en", "de" };

  private static final String[] AUTONET_LANGUAGES = { "ro", "de", "it", "fr", "hu",
    "sk", "en", "sl", "bg" };

  @InjectMocks
  private LocaleContextHelper helper;

  @Mock
  private CountryConfiguration countryConfig;

  @Test
  public void shouldReturnSupportedLocaleForSwissAndAustria() {
    Arrays.asList("ch", "at").forEach(country -> {
      final Map<String, String> testLanguages = new HashMap<>();
      Arrays.stream(CH_AND_AT_LANGUAGES).forEach(l -> testLanguages.put(l, l));
      testLanguages.put(LanguageCode.EN.getCode(), CH_AND_AT_LANGUAGES[0]);
      executeTestSuite(country, testLanguages);
    });
  }

  @Test
  public void shouldReturnSupportedLocaleForAutonet() {
    final Map<String, String> testLanguages = new HashMap<>();
    Arrays.stream(AUTONET_LANGUAGES).forEach(l -> testLanguages.put(l, l));
    executeTestSuite("ro", testLanguages);
  }

  @Test
  public void shouldReturnSupportedLocaleForCzech() {
    final Map<String, String> testLanguages = new HashMap<>();
    Arrays.stream(CS_LANGUAGES).forEach(l -> testLanguages.put(l, l));
    executeTestSuite("cz", testLanguages);
  }

  private void executeTestSuite(String countryCode, Map<String, String> testLanguages) {
    final String[] languages = testLanguages.keySet().toArray(new String[0]);
    mockWhen(countryCode, languages[0], languages);
    testLanguages.forEach((lang, expectedLang) -> {
      final Locale actual = helper.toSupportedLocale(lang);
      Assert.assertThat(actual.getLanguage(), Matchers.equalTo(expectedLang));
    });
  }

  private void mockWhen(String country, String defaultLange, String... languages) {
    Mockito.when(countryConfig.defaultLanguage()).thenReturn(defaultLange);
    Mockito.when(countryConfig.getCode()).thenReturn(country);
    Mockito.when(countryConfig.languages()).thenReturn(languages);
  }
}
