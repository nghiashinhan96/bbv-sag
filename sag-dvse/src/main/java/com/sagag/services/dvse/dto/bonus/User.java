package com.sagag.services.dvse.dto.bonus;

import java.io.Serializable;

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
@XmlType(name = "user", namespace = "http://eshop.sag.ch/types/specificservices")
public class User implements Serializable {

  private static final long serialVersionUID = -6928599795998996662L;

  @XmlElement()
  private String name;

  @XmlElement()
  private String firstname;

  @XmlElement()
  private String langIso;

  @XmlElement()
  private String email;

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

}
