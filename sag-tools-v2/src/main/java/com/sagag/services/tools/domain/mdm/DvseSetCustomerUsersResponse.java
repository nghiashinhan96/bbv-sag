package com.sagag.services.tools.domain.mdm;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to receive DVSE XML create customer user response.
 *
 */
@XmlRootElement(name = "ResponseDataOfSetCustomerUsers_V1")
public class DvseSetCustomerUsersResponse {
  @XmlElement(name = "Data")
  private DvseSetCustomerUsersData data;

  public List<DvseCustomerUserInfo> getUsersInfos() {
      return data.getUsersInfos();
  }
}

@XmlRootElement(name = "Data")
class DvseSetCustomerUsersData {
  private List<DvseCustomerUserInfo> usersInfos;

  @XmlElementWrapper(name = "CustomerUsersArray")
  @XmlElement(name = "CustomerUserShortInfo_V1")
  public List<DvseCustomerUserInfo> getUsersInfos() {
      return usersInfos;
  }

  public void setUsersInfos(List<DvseCustomerUserInfo> usersInfos) {
      this.usersInfos = usersInfos;
  }
}
