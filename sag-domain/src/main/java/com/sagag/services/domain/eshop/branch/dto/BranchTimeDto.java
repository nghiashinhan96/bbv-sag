package com.sagag.services.domain.eshop.branch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.LocalTime;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchTimeDto implements Serializable {

  private static final long serialVersionUID = -8809711288178249882L;

  private LocalTime openingTime;

  private LocalTime closingTime;

  private LocalTime lunchStartTime;

  private LocalTime lunchEndTime;
}
