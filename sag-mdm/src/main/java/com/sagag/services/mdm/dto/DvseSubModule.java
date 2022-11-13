package com.sagag.services.mdm.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

/**
 * Class to provide DVSE XML Pojo.
 *
 */
@XmlRootElement(name = "CustomerUserModuleSub_V1")
public class DvseSubModule {
  private String id;
  private String description;
  private String enabled;
  private List<DvseKeyValue> keyValues;

  @XmlElement(name = "ModuleSubID")
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

  @XmlElement(name = "CustomerUserHasModuleSub")
  public String getEnabled() {
    return enabled;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }

  @XmlElementWrapper(name = "KeyValuesArray")
  @XmlElement(name = "KeyValue_V2")
  public List<DvseKeyValue> getKeyValues() {
    return keyValues;
  }

  public void setKeyValues(List<DvseKeyValue> keyValues) {
    this.keyValues = keyValues;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("description", description).add("enabled", enabled)
        .add("keyValues", Joiner.on(", ").join(MoreObjects.firstNonNull(keyValues, ImmutableList.of()))).toString();
  }
}
