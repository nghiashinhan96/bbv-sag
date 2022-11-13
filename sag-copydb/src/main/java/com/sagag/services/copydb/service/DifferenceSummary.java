package com.sagag.services.copydb.service;

import lombok.Data;

@Data
public class DifferenceSummary {

  private final String fromDb;
  private final String fromSchema;
  private final String fromUrl;

  private final String toDb;
  private final String toSchema;
  private final String toUrl;

  @Override
  public String toString() {
    return "DifferenceSummary \n\tfromDb=" + fromDb + ", fromSchema=" + fromSchema + ", fromUrl=" + fromUrl + "\n\ttoDb=" + toDb + ", toSchema=" + toSchema
        + ", toUrl=" + toUrl;
  }

}
