package com.sagag.services.mdm.request;

import javax.xml.bind.annotation.XmlElement;

/**
 * Class to provide DVSE XML get customer request.
 *
 */
public class DvseCustomerRequest extends DvseRequest {

  private String customerId;

  @XmlElement(name = "IKndNr")
  public String getCustomerId() {
      return customerId;
  }

  public void setCustomerId(String customerId) {
      this.customerId = customerId;
  }

}
