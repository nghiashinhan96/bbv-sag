package com.sagag.services.hazelcast.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EshopGlobalSettingDto implements Serializable {

  private static final long serialVersionUID = -7109205567329487440L;

  private int id;

  private String code;

  private String description;

  private boolean enabled;

  private String settingType;

  private Integer settingValue;

}
