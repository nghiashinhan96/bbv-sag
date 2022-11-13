package com.sagag.services.mdm.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

/**
 * Class to provide DVSE XML Pojo.
 *
 */
@XmlRootElement(name = "CustomerUser_V1")
public class DvseCustomerUser {
  private String seqNumber;
  private String cdate;
  private String udate;
  private String uuser;
  private String delete;
  private List<DvseMainModule> modules;
  private List<DvseSubModule> subModules;

  @XmlElement(name = "LfdNr")
  public String getSeqNumber() {
    return seqNumber;
  }

  public void setSeqNumber(String seqNumber) {
    this.seqNumber = seqNumber;
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

  @XmlElementWrapper(name = "ModuleMainsArray")
  @XmlElement(name = "CustomerUserModuleMain_V1")
  public List<DvseMainModule> getModules() {
    return modules;
  }

  public void setModules(List<DvseMainModule> modules) {
    this.modules = modules;
  }

  @XmlElementWrapper(name = "ModuleSubsArray")
  @XmlElement(name = "CustomerUserModuleSub_V1")
  public List<DvseSubModule> getSubModules() {
    return subModules;
  }

  public void setSubModules(List<DvseSubModule> subModules) {
    this.subModules = subModules;
  }

  @XmlElement(name = "Delete")
  public String getDelete() {
    return delete;
  }

  public void setDelete(String delete) {
    this.delete = delete;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("seqNumber", seqNumber).add("cdate", cdate).add("udate", udate).add("uuser", uuser)
        .add("modules", Joiner.on(", ").join(MoreObjects.firstNonNull(modules, ImmutableList.of())))
        .add("subModules", Joiner.on(", ").join(MoreObjects.firstNonNull(subModules, ImmutableList.of()))).toString();
  }
}
