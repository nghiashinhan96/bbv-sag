package com.sagag.services.tools.domain.mdm;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to provide DVSE XML Pojo.
 *
 */
@XmlRootElement(name = "KeyValue_V2")
public class DvseKeyValue {
  private String key;
  private String value;
  private String description;

  public DvseKeyValue() {}

  public DvseKeyValue(String key, String value, String description) {
    this.key = key;
    this.value = value;
    this.description = description;
  }

  @XmlElement(name = "KeyID")
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @XmlElement(name = "Value")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @XmlElement(name = "Description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
