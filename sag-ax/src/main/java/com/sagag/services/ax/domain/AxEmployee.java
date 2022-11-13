package com.sagag.services.ax.domain;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.sag.external.Employee;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the employee info from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxEmployee extends AxBaseResponse {

  private static final long serialVersionUID = 2748322554152465969L;

  private String personalNumber;

  private String searchName;

  private String name;

  private List<AxContact> contacts;

  @JsonIgnore
  public Employee toEmployeeDto() {
    return Employee.builder().id(this.personalNumber).abbreviation(this.searchName).name(this.name)
        .contacts(contacts.stream().map(AxContact::toDto).collect(Collectors.toList())).build();
  }

}
