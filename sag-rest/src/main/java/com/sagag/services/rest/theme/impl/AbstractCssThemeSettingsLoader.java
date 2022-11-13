package com.sagag.services.rest.theme.impl;

import com.sagag.services.rest.theme.CssThemeSettingsLoader;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class AbstractCssThemeSettingsLoader implements CssThemeSettingsLoader {

  private static final Pattern CSS_PATTERN = Pattern.compile("\"[{]\\w+[}]\"");

  protected static String syncAffiliateSettings(final Map<String, String> settings,
      final String contents) {

    // synchronize the theme settings of the organisation to the theme.css file
    final Matcher matcher = CSS_PATTERN.matcher(contents);
    final int prefixPatternLength = 2;
    String after = contents;
    final List<String> variables = new ArrayList<>();
    while (matcher.find()) {
      final String group = matcher.group();
      final String variable =
          StringUtils.substring(group, prefixPatternLength, group.length() - prefixPatternLength);
      if (!variables.contains(variable)) {
        after = after.replaceAll("\"[{]" + variable + "[}]\"", settings.get(variable));
        variables.add(variable); // to avoid running multiple times for the same variable.
      }
    }
    return after;
  }

  protected byte[] doSafelyCssFileByteArray(File file) throws IOException {
    try (FileInputStream fis = new FileInputStream(file)) {
      return IOUtils.toByteArray(fis);
    } catch (FileNotFoundException ex) {
      log.warn("Not found css file", ex);
      throw ex;
    }
  }
}
