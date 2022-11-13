package com.sagag.services.gtmotive.domain.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Items {

  private String[] item;

  public String[] getItem() {
    return item;
  }

  public void setItem(String[] item) {
    this.item = item;
  }

  @Override
  public String toString() {
    return "ClassPojo [item = " + item + "]";
  }
}
