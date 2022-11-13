package com.sagag.services.dvse.dto.bonus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlEnum()
@XmlType(name = "customertype", propOrder = { "RETAIL", "WHOLESALE" },
    namespace = "http://eshop.sag.ch/types/customer")
public enum CustomerType {

  @XmlEnumValue("RETAIL")
  RETAIL,

  @XmlEnumValue("WHOLESALE")
  WHOLESALE
}
