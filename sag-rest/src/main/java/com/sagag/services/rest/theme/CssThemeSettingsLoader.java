package com.sagag.services.rest.theme;

import java.io.IOException;

public interface CssThemeSettingsLoader {

  /**
   * Initializes the theme settings for input.
   *
   * @param input the input.
   * @return the css file
   * @throws IOException throws when no theme template file found.
   */
  byte[] initializeThemeSettings(String input) throws IOException;

}
