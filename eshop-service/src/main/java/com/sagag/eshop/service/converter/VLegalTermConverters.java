package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.VLegalTerm;
import com.sagag.services.domain.sag.legal_term.LegalTermDto;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

/**
 * Utility provide some converters of VLegalTerm.
 */
@UtilityClass
public final class VLegalTermConverters {

  public static Function<VLegalTerm, LegalTermDto> converter() {
    return vLegalTerm -> {
      LegalTermDto dto = new LegalTermDto();
      dto.setName(vLegalTerm.getName());
      dto.setSummary(vLegalTerm.getSummary());
      dto.setContent(vLegalTerm.getContent());
      dto.setPdfUrl(vLegalTerm.getPdfUrl());
      dto.setTermId(vLegalTerm.getTermId());
      dto.setUserId(vLegalTerm.getUserId());

      Optional.ofNullable(vLegalTerm.getTimeAccepted())
              .map(val -> LocalDateTime.parse(val, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
              .ifPresent(dto::setTimeAccepted);

      Optional.ofNullable(vLegalTerm.getDateValidFrom())
              .map(val -> LocalDate.parse(val, DateTimeFormatter.ISO_LOCAL_DATE))
              .ifPresent(dto::setDateValidFrom);

      dto.setAcceptancePeriodDays(vLegalTerm.getAcceptancePeriodDays());
      dto.setSort(vLegalTerm.getSort());
      return dto;
    };
  }

}
