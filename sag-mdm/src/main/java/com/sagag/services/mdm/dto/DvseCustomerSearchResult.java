package com.sagag.services.mdm.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CustomerSearchResult_V1")
public class DvseCustomerSearchResult {

  private String cdate;
  private String column0;
  private String column1;
  private String column2;
  private String column3;
  private String column4;
  private String column5;
  private String column6;
  private String column7;
  private String column8;
  private String column9;
  private String ikndnr;
  private String lfdnr;
  private String udate;
  private String uuser;

  @XmlElement(name = "CDate")
  public String getCdate() {
    return cdate;
  }

  public void setCdate(String cdate) {
    this.cdate = cdate;
  }

  @XmlElement(name = "Column0")
  public String getColumn0() {
    return column0;
  }

  public void setColumn0(String column0) {
    this.column0 = column0;
  }

  @XmlElement(name = "Column1")
  public String getColumn1() {
    return column1;
  }

  public void setColumn1(String column1) {
    this.column1 = column1;
  }

  @XmlElement(name = "Column2")
  public String getColumn2() {
    return column2;
  }

  public void setColumn2(String column2) {
    this.column2 = column2;
  }

  @XmlElement(name = "Column3")
  public String getColumn3() {
    return column3;
  }

  public void setColumn3(String column3) {
    this.column3 = column3;
  }

  @XmlElement(name = "Column4")
  public String getColumn4() {
    return column4;
  }

  public void setColumn4(String column4) {
    this.column4 = column4;
  }

  @XmlElement(name = "Column5")
  public String getColumn5() {
    return column5;
  }

  public void setColumn5(String column5) {
    this.column5 = column5;
  }

  @XmlElement(name = "Column6")
  public String getColumn6() {
    return column6;
  }

  public void setColumn6(String column6) {
    this.column6 = column6;
  }

  @XmlElement(name = "Column7")
  public String getColumn7() {
    return column7;
  }

  public void setColumn7(String column7) {
    this.column7 = column7;
  }

  @XmlElement(name = "Column8")
  public String getColumn8() {
    return column8;
  }

  public void setColumn8(String column8) {
    this.column8 = column8;
  }

  @XmlElement(name = "Column9")
  public String getColumn9() {
    return column9;
  }

  public void setColumn9(String column9) {
    this.column9 = column9;
  }

  @XmlElement(name = "IKndNr")
  public String getIkndnr() {
    return ikndnr;
  }

  public void setIkndnr(String ikndnr) {
    this.ikndnr = ikndnr;
  }

  @XmlElement(name = "LfdNr")
  public String getLfdnr() {
    return lfdnr;
  }

  public void setLfdnr(String lfdnr) {
    this.lfdnr = lfdnr;
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

}
