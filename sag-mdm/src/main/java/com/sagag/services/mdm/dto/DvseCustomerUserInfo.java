package com.sagag.services.mdm.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.MoreObjects;

/**
 * Class to provide DVSE XML Pojo.
 *
 */
@XmlRootElement(name = "CustomerUserShortInfo_V1")
public class DvseCustomerUserInfo {
  private String seqNumber;
  private String username;
  private String password;
  private String active;

  @XmlElement(name = "LfdNr")
  public String getSeqNumber() {
    return seqNumber;
  }

  public void setSeqNumber(String seqNumber) {
    this.seqNumber = seqNumber;
  }

  @XmlElement(name = "Username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @XmlElement(name = "Password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @XmlElement(name = "Active")
  public String getActive() {
    return active;
  }

  public void setActive(String active) {
    this.active = active;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("seqNumber", seqNumber).add("username", username).add("password", password).add("active", active).toString();
  }
}
