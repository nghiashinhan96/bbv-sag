package com.sagag.services.ivds.response.availability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetArticleAvailabilityResponse implements Serializable {

  private static final long serialVersionUID = 8152702801873716036L;

  private Map<String, AvailabilityResponseItem> items;
  private int numberOfRequestedItems;
  private int errorCode;

}
