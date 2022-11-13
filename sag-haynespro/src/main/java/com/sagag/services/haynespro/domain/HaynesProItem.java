package com.sagag.services.haynespro.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", propOrder = { "key", "value" })
@Data
public class HaynesProItem {

  @XmlElement(namespace = "http://xml.apache.org/xml-soap", required = true)
  protected String key;

  @XmlElement(namespace = "http://xml.apache.org/xml-soap", required = true)
  protected String value;

}
