package com.sagag.services.rest.resource;

import com.sagag.services.domain.sag.external.CustomerBranch;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Customer branches response class.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerBranchDataResource extends ResourceSupport {

  private List<CustomerBranch> branches;

  public CustomerBranchDataResource(List<CustomerBranch> branches) {
    this.branches = branches;
  }

}
