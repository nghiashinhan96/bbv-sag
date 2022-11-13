package com.sagag.services.haynespro.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registerVisit",
    propOrder = { "companyIdentificaton", "username", "password", "properties" })
@Data
public class RegisterVisit {

  protected String companyIdentificaton;
  protected String username;
  protected String password;
  protected HaynesProMap properties;

}
