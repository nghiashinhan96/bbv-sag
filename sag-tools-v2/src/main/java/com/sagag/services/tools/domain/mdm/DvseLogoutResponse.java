package com.sagag.services.tools.domain.mdm;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to receive DVSE XML logout response.
 *
 */
@XmlRootElement(name = "ResponseDataOfLogoutCustomerUser_V1")
public class DvseLogoutResponse {
  @XmlElement(name = "Data")
  private LogoutData data;

  public boolean isSuccess() {
      return Objects.equals(data.getSuccess(), "true");
  }
}

@XmlRootElement(name = "Data")
class LogoutData {
  private String success;

  @XmlElement(name = "Success")
  String getSuccess() {
      return success;
  }

  public void setSuccess(String success) {
      this.success = success;
  }
}
