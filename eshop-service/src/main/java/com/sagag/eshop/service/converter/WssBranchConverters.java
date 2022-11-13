package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.WssBranch;
import com.sagag.eshop.repo.entity.WssBranchOpeningTime;
import com.sagag.services.domain.eshop.branch.dto.WssBranchRequestBody;
import com.sagag.services.domain.eshop.dto.WssBranchDto;
import com.sagag.services.domain.eshop.dto.WssBranchOpeningTimeDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converter for WSS branch
 */
@UtilityClass
public final class WssBranchConverters {

  public static WssBranchDto convertToBranchDto(final WssBranch branch) {
    return WssBranchDto.builder().id(branch.getId()).branchCode(branch.getBranchCode())
        .branchNr(branch.getBranchNr()).orgId(branch.getOrgId())
        .wssBranchOpeningTimes(convertWssBranchOpeningTime(branch.getWssBranchOpeningTimes()))
        .build();
  }

  public static WssBranch convertFromRequest(final WssBranchRequestBody request, final int orgId) {
    return WssBranch.builder().branchNr(request.getBranchNr()).branchCode(request.getBranchCode())
        .orgId(orgId).build();
  }

  public static Function<WssBranch, WssBranchDto> optionalBranchConverter() {
    return WssBranchConverters::convertToBranchDto;
  }

  private static List<WssBranchOpeningTimeDto> convertWssBranchOpeningTime(
      List<WssBranchOpeningTime> wssBranchOpeningTimes) {
    if (CollectionUtils.isEmpty(wssBranchOpeningTimes)) {
      return Lists.emptyList();
    }
    return wssBranchOpeningTimes.stream()
        .map(WssBranchOpeningTimeConverters::convertToWssBranchOpeningTimeDto)
        .collect(Collectors.toList());
  }

}
