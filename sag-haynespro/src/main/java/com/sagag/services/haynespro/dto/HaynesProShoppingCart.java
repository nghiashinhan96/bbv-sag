package com.sagag.services.haynespro.dto;

import lombok.Data;

import org.apache.commons.lang3.ArrayUtils;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "shoppingCart")
public class HaynesProShoppingCart {

  private HaynesProJobs jobs;

  private HaynesProVehicle vehicle;

  private HaynesProParts parts;

  private HaynesProCustomer customer;

  public boolean hasParts() {
    return parts != null && !ArrayUtils.isEmpty(parts.getPart());
  }

  public boolean hasJobs() {
    return jobs != null && !ArrayUtils.isEmpty(jobs.getJob());
  }
}
