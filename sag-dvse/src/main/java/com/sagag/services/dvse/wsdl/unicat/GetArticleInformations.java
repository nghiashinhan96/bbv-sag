package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "user",
    "items"
})
@XmlRootElement(name = "GetArticleInformations")
public class GetArticleInformations {

  @XmlElement(name = "User")
  protected User user;

  @XmlElement(name = "Items")
  protected UnicatArrayOfItem items;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public UnicatArrayOfItem getItems() {
    return items;
  }
  public void setItems(UnicatArrayOfItem items) {
    this.items = items;
  }

}
