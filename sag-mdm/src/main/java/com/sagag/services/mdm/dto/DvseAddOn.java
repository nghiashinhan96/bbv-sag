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
@XmlRootElement(name = "CustomerUserModuleAddOn_V1")
public class DvseAddOn {
  private String id;
  private String description;
  private List<DvseAddOnKey> keys;

  @XmlElement(name = "AddOnID")
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

  @XmlElementWrapper(name = "AddOnKeysArray")
  @XmlElement(name = "CustomerUserModuleAddOnKey_V1")
  public List<DvseAddOnKey> getKeys() {
    return keys;
  }

  public void setKeys(List<DvseAddOnKey> keys) {
    this.keys = keys;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("description", description)
        .add("keyValues", Joiner.on(", ").join(MoreObjects.firstNonNull(keys, ImmutableList.of()))).toString();
  }
}
