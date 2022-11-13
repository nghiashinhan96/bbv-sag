package com.sagag.services.rest.resource;

import com.sagag.services.incentive.response.IncentiveLinkResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

/**
 * REST Happy bonus link resource class.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IncentiveLinkResource extends ResourceSupport {

  private IncentiveLinkResponse data;

  public IncentiveLinkResource(final IncentiveLinkResponse res) {
    this.data = res;
  }
}
