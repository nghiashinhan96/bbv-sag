package com.sagag.services.service.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerBranchRequest implements Serializable {

  private static final long serialVersionUID = -4472795342698460862L;

  private String defaultBranchId;

  private String companyName;
}
