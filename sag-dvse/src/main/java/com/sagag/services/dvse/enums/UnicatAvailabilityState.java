package com.sagag.services.dvse.enums;

import java.util.Arrays;

import com.sagag.services.stakis.erp.enums.StakisArticleAvailabilityState;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnicatAvailabilityState {

  AVAILABLE(
      1,new StakisArticleAvailabilityState[] {StakisArticleAvailabilityState.GREEN}),
  NOT_AVAILABLE(2,new StakisArticleAvailabilityState[]
      {StakisArticleAvailabilityState.RED, StakisArticleAvailabilityState.BLACK}),
  PARTIALLY_AVAILABLE_3(3,new StakisArticleAvailabilityState[]
      {StakisArticleAvailabilityState.YELLOW}),
  PARTIALLY_AVAILABLE_4(4,new StakisArticleAvailabilityState[]
      {StakisArticleAvailabilityState.YELLOW_GREEN}),
  ;

  private int code;

  private StakisArticleAvailabilityState[] stakisAvailStates;

  public static UnicatAvailabilityState fromStakisAvailStateCode(int stakisAvailCode) {
    StakisArticleAvailabilityState stakisAvailState =
        StakisArticleAvailabilityState.fromCode(stakisAvailCode);

    return Arrays.asList(values()).stream()
        .filter(t -> Arrays.asList(t.getStakisAvailStates()).contains(stakisAvailState)).findFirst()
        .orElse(NOT_AVAILABLE);
  }
}
