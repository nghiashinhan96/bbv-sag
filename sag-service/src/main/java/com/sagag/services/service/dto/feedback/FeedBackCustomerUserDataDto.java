package com.sagag.services.service.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackCustomerUserDataDto implements Serializable {

  private static final long serialVersionUID = -1828065490999201929L;

  private String customerNr;
  private String customerName;
  private String customerTown;
  private List<String> customerEmails;
  private List<String> customerPhones;
  private String defaultBranch;
  private String userEmail;
}
