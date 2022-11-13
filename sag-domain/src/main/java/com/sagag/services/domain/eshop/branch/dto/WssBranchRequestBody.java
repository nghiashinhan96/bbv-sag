package com.sagag.services.domain.eshop.branch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.domain.eshop.dto.WssBranchOpeningTimeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class WssBranchRequestBody implements Serializable {

  private static final long serialVersionUID = 8490939496093362035L;

  @NotNull(message = "Branch number must not be null")
  private Integer branchNr;

  @NotBlank(message = "Branch code must not be blank")
  @Size(min = 0, max = 10)
  private String branchCode;

  private String openingTime;

  private String closingTime;

  private String lunchStartTime;

  private String lunchEndTime;

  private List<WssBranchOpeningTimeDto> wssBranchOpeningTimes;

}
