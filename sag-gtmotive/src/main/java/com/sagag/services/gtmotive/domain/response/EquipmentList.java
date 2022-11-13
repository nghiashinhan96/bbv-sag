package com.sagag.services.gtmotive.domain.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EquipmentList {

  private Items items;

  public Items getItems() {
    return items;
  }

  public void setItems(Items items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "ClassPojo [items = " + items + "]";
  }
}
