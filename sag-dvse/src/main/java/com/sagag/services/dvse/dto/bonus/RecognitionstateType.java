package com.sagag.services.dvse.dto.bonus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlEnum()
@XmlType(name = "recognitionstate",
    propOrder = { "VALID", "OUTDATED", "LOGEDIN", "LOGEDOUT", "INVALID" },
    namespace = "http://eshop.sag.ch/types/specificservices")
public enum RecognitionstateType {

  @XmlEnumValue("VALID")
  VALID,

  @XmlEnumValue("OUTDATED")
  OUTDATED,

  @XmlEnumValue("LOGEDIN")
  LOGEDIN,

  @XmlEnumValue("LOGEDOUT")
  LOGEDOUT,

  @XmlEnumValue("INVALID")
  INVALID
}
