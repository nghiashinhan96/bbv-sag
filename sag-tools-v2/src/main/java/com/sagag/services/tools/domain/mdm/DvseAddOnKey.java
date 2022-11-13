package com.sagag.services.tools.domain.mdm;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to provide DVSE XML Pojo.
 *
 */
@XmlRootElement(name = "CustomerUserModuleAddOnKey_V1")
public class DvseAddOnKey {
  private String id;
  private String description;
  private String shortCode;
  private String def;

  @XmlElement(name = "KeyID")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @XmlElement(name = "Description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @XmlElement(name = "ShortCode")
  public String getShortCode() {
    return shortCode;
  }

  public void setShortCode(String shortCode) {
    this.shortCode = shortCode;
  }

  @XmlElement(name = "Default")
  public String getDef() {
    return def;
  }

  public void setDef(String def) {
    this.def = def;
  }

}
