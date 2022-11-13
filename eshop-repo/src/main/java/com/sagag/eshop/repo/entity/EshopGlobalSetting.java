package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ESHOP_GLOBAL_SETTING")
public class EshopGlobalSetting implements Serializable {

  private static final long serialVersionUID = 2305193822228665964L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String code;

  private String description;

  private boolean enabled;

  private String settingType;

  private Integer settingValue;
}
