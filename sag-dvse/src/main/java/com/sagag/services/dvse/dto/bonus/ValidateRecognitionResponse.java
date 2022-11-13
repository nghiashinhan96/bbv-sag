package com.sagag.services.dvse.dto.bonus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;

/**
 * <p>Java class for validateRecognitionResponse complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="validateRecognitionResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://eshop.sag.ch/elements}validatedRecognition" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validateRecognitionResponse", propOrder = { "validatedRecognition"},
  namespace = "http://eshop.sag.ch/types/specificservices")
@XmlRootElement(namespace = "http://eshop.sag.ch/elements")
public class ValidateRecognitionResponse {

  @XmlElement
  protected ValidatedRecognition validatedRecognition;

}
