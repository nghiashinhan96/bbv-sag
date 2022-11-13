package com.sagag.services.haynespro.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Map", namespace = "http://xml.apache.org/xml-soap", propOrder = { "item" })
public class HaynesProMap {

  @XmlElement(nillable = true)
  protected List<HaynesProItem> item;

  /**
   * Gets the value of the item property.
   *
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the item property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   *
   * <pre>
   * getItem().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list {@link HaynesProItem }
   *
   *
   */
  public List<HaynesProItem> getItem() {
    if (item == null) {
      item = new ArrayList<>();
    }
    return this.item;
  }

}
