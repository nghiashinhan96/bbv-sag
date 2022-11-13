package com.sagag.services.domain.eshop.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionSearchResultDto implements Serializable {

  private static final long serialVersionUID = -6962216937034800078L;

  private String collectionName;

  private String affiliate;

  private String customerNr;

  private String collectionShortName;

}
