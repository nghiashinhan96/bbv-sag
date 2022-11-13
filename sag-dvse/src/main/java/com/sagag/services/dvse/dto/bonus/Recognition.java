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
@XmlType(name = "recognition", namespace = "http://eshop.sag.ch/types/specificservices")
public class Recognition implements Serializable {

  private static final long serialVersionUID = 4664424213481166801L;

  @XmlElement(required = true)
  private String token;

  @XmlElement(required = true)
  private RecognitionstateType state;

  @XmlElement()
  private String accessPoint;

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }
}
