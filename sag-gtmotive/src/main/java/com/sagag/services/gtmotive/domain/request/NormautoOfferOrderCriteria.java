package com.sagag.services.gtmotive.domain.request;

import com.sagag.services.gtmotive.domain.response.GtmotiveSimpleOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NormautoOfferOrderCriteria implements Serializable {

  private static final long serialVersionUID = 934099061268666030L;

  private String toEmail;
  private String vin;
  private String vehicleCode;
  private String vehicleInfo;
  private List<GtmotiveSimpleOperation> nonMatchedOperations;

}
