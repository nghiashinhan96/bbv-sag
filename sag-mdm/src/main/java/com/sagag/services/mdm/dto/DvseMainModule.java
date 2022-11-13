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
@XmlRootElement(name = "CustomerUserModuleMain_V1")
public class DvseMainModule {
  private String id;
  private String description;
  private String fullFrom;
  private List<DvseKeyValue> keyValues;
  private List<DvseExternalSystem> externalSystems;
  private List<DvseAddOn> addOns;

  @XmlElement(name = "ModuleMainID")
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

  @XmlElement(name = "FullFrom", nillable = true)
  public String getFullFrom() {
    return fullFrom;
  }

  public void setFullFrom(String fullFrom) {
    this.fullFrom = fullFrom;
  }

  @XmlElementWrapper(name = "KeyValuesArray")
  @XmlElement(name = "KeyValue_V2")
  public List<DvseKeyValue> getKeyValues() {
    return keyValues;
  }

  public void setKeyValues(List<DvseKeyValue> keyValues) {
    this.keyValues = keyValues;
  }

  @XmlElementWrapper(name = "ExternSystemsArray")
  @XmlElement(name = "CustomerUserModuleMainExternSystem_V2")
  public List<DvseExternalSystem> getExternalSystems() {
    return externalSystems;
  }

  public void setExternalSystems(List<DvseExternalSystem> externalSystems) {
    this.externalSystems = externalSystems;
  }

  @XmlElementWrapper(name = "AddOnsArray")
  @XmlElement(name = "CustomerUserModuleAddOn_V1")
  public List<DvseAddOn> getAddOns() {
    return addOns;
  }

  public void setAddOns(List<DvseAddOn> addOns) {
    this.addOns = addOns;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("description", description).add("fullFrom", fullFrom)
        .add("keyValues", Joiner.on(", ").join(MoreObjects.firstNonNull(keyValues, ImmutableList.of())))
        .add("externalSystems", Joiner.on(", ").join(MoreObjects.firstNonNull(externalSystems, ImmutableList.of())))
        .add("addOns", Joiner.on(", ").join(MoreObjects.firstNonNull(addOns, ImmutableList.of()))).toString();
  }
}
