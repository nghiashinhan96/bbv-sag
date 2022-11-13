package com.sagag.services.gtmotive.domain.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModelList {

  private ModelData modelData;

  public ModelData getModelData() {
    return modelData;
  }

  public void setModelData(ModelData modelData) {
    this.modelData = modelData;
  }

  @Override
  public String toString() {
    return "ClassPojo [modelData = " + modelData + "]";
  }
}
