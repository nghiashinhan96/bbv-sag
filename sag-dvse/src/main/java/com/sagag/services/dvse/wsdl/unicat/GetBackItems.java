package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBackItems", propOrder = { "items" })
public class GetBackItems {

  @XmlElement(name = "Items")
  protected ArrayOfItem items;

  public ArrayOfItem getItems() {
    return items;
  }

  public void setItems(ArrayOfItem value) {
    this.items = value;
  }

}
