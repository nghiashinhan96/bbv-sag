package com.sagag.services.gtmotive.domain.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModelData {

  private String modelDescription;

  private String umc;

  private String makeCode;

  private String makeDescription;

  public String getModelDescription() {
    return modelDescription;
  }

  public void setModelDescription(String modelDescription) {
    this.modelDescription = modelDescription;
  }

  public String getUmc() {
    return umc;
  }

  public void setUmc(String umc) {
    this.umc = umc;
  }

  public String getMakeCode() {
    return makeCode;
  }

  public void setMakeCode(String makeCode) {
    this.makeCode = makeCode;
  }

  public String getMakeDescription() {
    return makeDescription;
  }

  public void setMakeDescription(String makeDescription) {
    this.makeDescription = makeDescription;
  }

  @Override
  public String toString() {
    return "ClassPojo [modelDescription = " + modelDescription + ", umc = " + umc + ", makeCode = "
        + makeCode + ", makeDescription = " + makeDescription + "]";
  }
}
