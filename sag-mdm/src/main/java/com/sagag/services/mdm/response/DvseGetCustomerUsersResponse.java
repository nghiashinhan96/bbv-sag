package com.sagag.services.mdm.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.sagag.services.mdm.dto.DvseCustomerUser;

/**
 * Class to receive DVSE XML get the list of customer user response.
 *
 */
@XmlRootElement(name = "ResponseDataOfGetCustomerUsers_V1")
public class DvseGetCustomerUsersResponse {
  @XmlElement(name = "Data")
  private DvseGetCustomerUsersData data;

  public List<DvseCustomerUser> getCustomerUsers() {
      return data.getCustomerUsers();
  }
}

@XmlRootElement(name = "Data")
class DvseGetCustomerUsersData {
  private List<DvseCustomerUser> customerUsers;

  @XmlElementWrapper(name = "CustomerUsersArray")
  @XmlElement(name = "CustomerUser_V1")
  List<DvseCustomerUser> getCustomerUsers() {
      return customerUsers;
  }

  public void setCustomerUsers(List<DvseCustomerUser> customerUsers) {
      this.customerUsers = customerUsers;
  }
}
