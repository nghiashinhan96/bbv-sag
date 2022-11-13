package com.sagag.services.tools.domain.mdm;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to provide DVSE XML create customer request.
 *
 */
@XmlRootElement(name = "SetCustomer_V1")
public class DvseSetCustomerRequest extends DvseRequest {

  private DvseCustomer customer;

  @XmlElement(name = "SkipValidation")
  private String skipValidation = "false";

  @XmlElement(name = "Customer")
  public DvseCustomer getCustomer() {
      return customer;
  }

  public void setCustomer(DvseCustomer customer) {
      this.customer = customer;
  }

}
