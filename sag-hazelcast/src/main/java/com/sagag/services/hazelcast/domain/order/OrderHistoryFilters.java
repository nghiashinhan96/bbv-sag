package com.sagag.services.hazelcast.domain.order;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
@JsonPropertyOrder({ "orderStatuses", "usernames", "dateFrom", "dateTo" })
public class OrderHistoryFilters implements Serializable {

  private static final long serialVersionUID = 8261564634365389079L;

  private List<String> orderStatuses;

  private List<String> usernames;

  private String dateFrom;

  private String dateTo;
}
