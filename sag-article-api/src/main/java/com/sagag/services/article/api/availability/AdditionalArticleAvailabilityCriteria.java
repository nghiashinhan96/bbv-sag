package com.sagag.services.article.api.availability;

import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.executor.WorkingHours;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdditionalArticleAvailabilityCriteria {

  private List<TourTimeDto> tourTimeList;

  private List<WorkingHours> openingHours;

  private String branchId;

  public AdditionalArticleAvailabilityCriteria(List<TourTimeDto> tourTimeList,
      List<WorkingHours> openingHours, String branchId) {
    setTourTimeList(tourTimeList);
    setOpeningHours(openingHours);
    setBranchId(branchId);
  }
}
