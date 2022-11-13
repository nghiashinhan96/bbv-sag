package com.sagag.services.ivds.request.vehicle;

import lombok.Data;

import java.io.Serializable;

@Data
public class MotorbikeSubClass implements Serializable {

  private static final long serialVersionUID = -4293094418234658405L;

  private boolean isSeleted;

  private String shortName;

}
