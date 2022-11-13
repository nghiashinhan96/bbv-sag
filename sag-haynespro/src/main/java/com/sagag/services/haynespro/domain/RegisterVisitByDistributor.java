package com.sagag.services.haynespro.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registerVisitByDistributor",
    propOrder = { "companyIdentificaton", "distributorPassword", "username", "properties" })
@XmlRootElement(name = "registerVisitByDistributor")
@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class RegisterVisitByDistributor {

  protected String companyIdentificaton;

  protected String distributorPassword;

  protected String username;

  protected HaynesProMap properties;

}
