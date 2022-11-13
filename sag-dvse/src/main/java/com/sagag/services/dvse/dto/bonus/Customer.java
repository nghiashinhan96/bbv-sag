package com.sagag.services.dvse.dto.bonus;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(namespace = "http://eshop.sag.ch/elements")
@XmlType(name = "customer", namespace = "http://eshop.sag.ch/types/specificservices")
public class Customer implements Serializable {

  private static final long serialVersionUID = 6254547451463293016L;

  @XmlElement(required = true)
  private BigDecimal number;

  @XmlElement()
  private String name;

  @XmlElement()
  private CustomerType type;

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }
}
