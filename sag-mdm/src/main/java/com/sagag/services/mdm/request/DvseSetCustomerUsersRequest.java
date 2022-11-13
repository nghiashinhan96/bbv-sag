package com.sagag.services.mdm.request;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.sagag.services.mdm.dto.DvseCustomerUser;

/**
 * Class to provide DVSE XML create customer user request.
 *
 */
@XmlRootElement(name = "SetCustomerUsers_V1")
public class DvseSetCustomerUsersRequest extends DvseCustomerRequest {

  private List<DvseCustomerUser> customerUsers;

  @XmlElementWrapper(name = "CustomerUsers")
  @XmlElement(name = "CustomerUser_V1")
  public List<DvseCustomerUser> getCustomerUsers() {
      return customerUsers;
  }

  public void setCustomerUsers(List<DvseCustomerUser> customerUsers) {
      this.customerUsers = customerUsers;
  }

}
