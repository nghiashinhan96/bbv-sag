package com.sagag.services.haynespro.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clearStickyUserData",
    propOrder = { "companyIdentificaton", "distributorPassword", "username" })
@Data
public class ClearStickyUserData {

  protected String companyIdentificaton;

  protected String distributorPassword;

  protected String username;

}
