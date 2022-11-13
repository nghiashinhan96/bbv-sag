package com.sagag.services.mdm.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to receive DVSE XML create customer response.
 *
 */
@XmlRootElement(name = "ResponseDataOfSetCustomer_V1")
public class DvseSetCustomerResponse {
  @XmlElement(name = "Data")
  private SetCustomerData data;

  public String getCustomerId() {
      return data.getCustomerId();
  }
}

@XmlRootElement(name = "Data")
class SetCustomerData {
  private String customerId;

  @XmlElement(name = "IKndNr")
  public String getCustomerId() {
      return customerId;
  }

  public void setCustomerId(String customerId) {
      this.customerId = customerId;
  }
}
