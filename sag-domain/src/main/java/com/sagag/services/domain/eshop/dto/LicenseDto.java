package com.sagag.services.domain.eshop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LicenseDto implements Serializable {

  private static final long serialVersionUID = 6481864110416606186L;

  private long id;

  private String typeOfLicense;

  private long packId;

  private String packName;

  private long customerNr;

  private long userId;

  private Date beginDate;

  private Date endDate;

  private int quantity;

  private int quantityUsed;

  private Date lastUsed;

  private Date lastUpdate;

  private long lastUpdateBy;
}
