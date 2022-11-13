package com.sagag.services.tools.domain.mdm;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to provide DVSE XML Pojo.
 *
 */
@XmlRootElement(name = "Customer")
public class DvseCustomer {
  private String traderId;
  private String customerId;
  private String customerName;
  private String cdate;
  private String udate;
  private String uuser;
  private String memo;
  private String delete;
  private List<DvseCustomerUserInfo> usersInfos;
  private List<DvseMainModule> modules;

  @XmlElement(name = "TraderID")
  public String getTraderId() {
    return traderId;
  }

  public void setTraderId(String traderId) {
    this.traderId = traderId;
  }

  @XmlElement(name = "IKndNr")
  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  @XmlElement(name = "CustomerNo")
  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  @XmlElementWrapper(name = "CustomerUsersArray")
  @XmlElement(name = "CustomerUserShortInfo_V1")
  public List<DvseCustomerUserInfo> getUsersInfos() {
    return usersInfos;
  }

  public void setUsersInfos(List<DvseCustomerUserInfo> usersInfos) {
    this.usersInfos = usersInfos;
  }

  @XmlElementWrapper(name = "ModuleMainsArray")
  @XmlElement(name = "CustomerUserModuleMain_V1")
  public List<DvseMainModule> getModules() {
    return modules;
  }

  public void setModules(List<DvseMainModule> modules) {
    this.modules = modules;
  }

  @XmlElement(name = "CDate")
  public String getCdate() {
    return cdate;
  }

  public void setCdate(String cdate) {
    this.cdate = cdate;
  }

  @XmlElement(name = "UDate")
  public String getUdate() {
    return udate;
  }

  public void setUdate(String udate) {
    this.udate = udate;
  }

  @XmlElement(name = "UUser")
  public String getUuser() {
    return uuser;
  }

  public void setUuser(String uuser) {
    this.uuser = uuser;
  }

  @XmlElement(name = "Memo")
  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  @XmlElement(name = "Delete")
  public String getDelete() {
    return delete;
  }

  public void setDelete(String delete) {
    this.delete = delete;
  }

}
