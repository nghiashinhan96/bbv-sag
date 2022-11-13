package com.sagag.services.haynespro.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registerVisitResult", propOrder = { "redirectUrl" })
@Data
@EqualsAndHashCode(callSuper = false)
public class RegisterVisitResult extends RegistrationResult {

  @XmlElement(namespace = "")
  protected String redirectUrl;

}
