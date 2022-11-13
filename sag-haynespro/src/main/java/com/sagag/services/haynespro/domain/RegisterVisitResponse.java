package com.sagag.services.haynespro.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registerVisitResponse", propOrder = { "registerVisitReturn" })
@Data
public class RegisterVisitResponse {

  protected RegisterVisitResult registerVisitReturn;

}
