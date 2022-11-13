package com.sagag.services.rest.resource;

import com.sagag.eshop.repo.entity.Organisation;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

/**
 * REST setting resource class.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SettingsResource extends ResourceSupport {

  private final Organisation organisation;

  /**
   * Constructs the {@link SettingsResource} instance.
   *
   * @param organisation the {@link Organisation}
   */
  public SettingsResource(Organisation organisation) {
    this.organisation = organisation;
  }
}
