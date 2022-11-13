package com.sagag.services.dvse.dto.dvse;

import lombok.Data;

import java.io.Serializable;

@Data
public class TotalCartNotifyInfo implements Serializable {

  private static final long serialVersionUID = 1258220781365475673L;

  private Integer total;
  private String userId;
}
