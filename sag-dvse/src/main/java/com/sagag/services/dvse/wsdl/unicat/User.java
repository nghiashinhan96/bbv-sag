package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User", propOrder = {
    "sid",
    "username"
})
public class User {
  
  @XmlElement(name = "Sid")
  private String sid;
  @XmlElement(name = "Username")
  private String username;
  
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getSid() {
    return sid;
  }
  public void setSid(String sid) {
    this.sid = sid;
  }

}
