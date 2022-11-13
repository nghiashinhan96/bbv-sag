package com.sagag.services.haynespro.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registerVisitByDistributorResponse",
    propOrder = { "registerVisitByDistributorReturn" })
@Data
public class RegisterVisitByDistributorResponse {

  protected RegisterVisitResult registerVisitByDistributorReturn;

}
