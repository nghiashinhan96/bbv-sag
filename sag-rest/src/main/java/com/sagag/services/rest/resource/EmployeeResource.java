package com.sagag.services.rest.resource;

import com.sagag.services.domain.sag.external.Employee;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

/**
 * Employee response class.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmployeeResource extends ResourceSupport {

  private Employee employee;
}
