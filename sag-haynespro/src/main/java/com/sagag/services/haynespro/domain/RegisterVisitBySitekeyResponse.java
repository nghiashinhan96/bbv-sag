package com.sagag.services.haynespro.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registerVisitBySitekeyResponse", propOrder = { "registerVisitBySitekeyReturn" })
@Data
public class RegisterVisitBySitekeyResponse {

  protected RegisterVisitResult registerVisitBySitekeyReturn;

}
