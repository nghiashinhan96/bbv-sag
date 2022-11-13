package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the list of employees from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxEmployeesResourceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = 2024003041380192272L;

  private List<AxEmployee> employees;

  @JsonIgnore
  public boolean hasEmployees() {
    return CollectionUtils.isNotEmpty(employees);
  }

}
