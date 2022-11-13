package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the list of article availabilities from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxAvailabilityResourceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = -5919822289578279800L;

  private List<AxAvailability> availabilities;

  @JsonIgnore
  public boolean hasAvailability() {
    return CollectionUtils.isNotEmpty(availabilities);
  }

}
