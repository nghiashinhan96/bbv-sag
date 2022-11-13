package com.sagag.services.domain.sag.legal_term;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LegalTermDto {

  private String name;

  private String summary;

  private String content;

  private String pdfUrl;

  private Long termId;

  private Long userId;

  private LocalDateTime timeAccepted;

  private LocalDate dateValidFrom;

  private Integer acceptancePeriodDays;

  private Integer daysLeft;

  private boolean accepted;

  private Integer sort;
}
