package com.sagag.services.mdm.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sagag.services.mdm.dto.DvseCustomer;

/**
 * Class to receive DVSE XML get customer response.
 *
 */
@XmlRootElement(name = "ResponseDataOfGetCustomer_V1")
public class DvseGetCustomerResponse {
  @XmlElement(name = "Data")
  private GetCustomerData data;

  public DvseCustomer getCustomer() {
      return data.getCustomer();
  }
}

@XmlRootElement(name = "Data")
class GetCustomerData {
  private DvseCustomer customer;

  @XmlElement(name = "Customer")
  DvseCustomer getCustomer() {
      return customer;
  }

  public void setCustomer(DvseCustomer customer) {
      this.customer = customer;
  }
}
