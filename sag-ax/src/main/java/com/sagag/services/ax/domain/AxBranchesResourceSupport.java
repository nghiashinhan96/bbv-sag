package com.sagag.services.ax.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Class to receive the list of braches from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxBranchesResourceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = -8801240970699414532L;

  private List<AxBranch> branches;

  @JsonIgnore
  public boolean hasBranches() {
    return CollectionUtils.isNotEmpty(branches);
  }

}
