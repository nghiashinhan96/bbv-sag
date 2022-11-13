package com.sagag.services.haynespro.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registerVisitBySitekey", propOrder = { "sitekey", "properties" })
@Data
public class RegisterVisitBySitekey {

  protected String sitekey;
  protected HaynesProMap properties;

}
