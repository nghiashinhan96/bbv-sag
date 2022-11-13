package com.sagag.services.tools.domain.ax;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the list of Ax customer addresses response from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxAddressResourceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = -6879423939155523755L;

  private List<AxAddress> addresses;

  @JsonIgnore
  public boolean hasAddresses() {
    return CollectionUtils.isNotEmpty(this.addresses);
  }

}
