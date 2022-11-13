package com.sagag.services.haynespro.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registrationResult", propOrder = { "code", "message" })
@XmlSeeAlso({ RegisterVisitResult.class })
@Data
public class RegistrationResult {

  @XmlElement(namespace = "")
  protected int code;

  @XmlElement(namespace = "")
  protected String message;

}
