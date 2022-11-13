package com.sagag.services.tools.domain.mdm;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to provide DVSE XML Pojo.
 *
 */
@XmlRootElement(name = "CustomerUserModuleMainExternSystem_V2")
public class DvseExternalSystem {
  private String id;
  private String description;
  private List<DvseKeyValue> keyValues;

  @XmlElement(name = "SystemID")
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

  @XmlElementWrapper(name = "KeyValuesArray")
  @XmlElement(name = "KeyValue_V2")
  public List<DvseKeyValue> getKeyValues() {
    return keyValues;
  }

  public void setKeyValues(List<DvseKeyValue> keyValues) {
    this.keyValues = keyValues;
  }

}
