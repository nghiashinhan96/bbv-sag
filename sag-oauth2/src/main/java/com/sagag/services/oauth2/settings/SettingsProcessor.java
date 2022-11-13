package com.sagag.services.oauth2.settings;

import java.util.Optional;

@FunctionalInterface
public interface SettingsProcessor<T, R> {

  /**
   * Processes change settings by input.
   *
   * @param input
   * @return the changed settings object
   */
  Optional<R> process(T input);

}
