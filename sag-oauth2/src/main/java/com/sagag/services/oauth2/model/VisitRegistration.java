package com.sagag.services.oauth2.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class VisitRegistration implements Serializable {

  private static final long serialVersionUID = -1908062886809455204L;

  private String companyID;

  private String username;

  private String customerID;

  // Base64 ([CompanyID][CompanyPassword][Username][CustomerID][timeStamp])
  private String visitRequestKey;

  private String language;

  private String returnURL;

  private String timeStamp;
}
