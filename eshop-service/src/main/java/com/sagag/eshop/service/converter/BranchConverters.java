package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.entity.BranchOpeningTime;
import com.sagag.eshop.service.dto.BranchDetailDto;
import com.sagag.eshop.service.dto.BranchDto;
import com.sagag.services.domain.eshop.branch.dto.BranchRequestBody;
import com.sagag.services.domain.eshop.dto.BranchOpeningTimeDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.assertj.core.util.Lists;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converter for branch
 */
@UtilityClass
public final class BranchConverters {

  private static BranchDetailDto convertToBranchDetail(final Branch branch,
      final List<BranchOpeningTime> branchOpeningTimes) {
    final BranchDetailDto branchDetailDto = new BranchDetailDto(branch);
    branchDetailDto
        .setBranchOpeningTimes(convertBranchOpeningTime(branchOpeningTimes));
    return branchDetailDto;
  }

  private static BranchDto convertToBranchDto(final Branch branch,
      final List<BranchOpeningTime> branchOpeningTimes) {
    final BranchDto branchDto = new BranchDto(branch);
    branchDto.setBranchOpeningTimes(convertBranchOpeningTime(branchOpeningTimes));
    return branchDto;
  }

  public static Branch convertFromRequest(final BranchRequestBody request) {
    return Branch.builder().branchNr(request.getBranchNr()).branchCode(request.getBranchCode())
        .orgId(request.getOrgId()).zip(request.getZip()).countryId(request.getCountryId())
        .addressStreet(request.getAddressStreet()).addressCity(request.getAddressCity())
        .addressDesc(request.getAddressDesc()).addressCountry(request.getAddressCountry())
        .regionId(request.getRegionId()).primaryFax(request.getPrimaryFax())
        .primaryEmail(request.getPrimaryEmail()).primaryPhone(request.getPrimaryPhone())
        .primaryUrl(request.getPrimaryUrl())
        .validForKSL(BooleanUtils.toBooleanDefaultIfNull(request.getValidForKSL(), false))
        .hideFromCustomers(
            BooleanUtils.toBooleanDefaultIfNull(request.getHideFromCustomers(), false))
        .hideFromSales(BooleanUtils.toBooleanDefaultIfNull(request.getHideFromSales(), false))
        .build();
  }

  public static Function<Branch, BranchDetailDto> optionalBranchDetailConverter(
      final List<BranchOpeningTime> branchOpeningTimes) {
    return branch -> convertToBranchDetail(branch, branchOpeningTimes);
  }

  public static Function<Branch, BranchDto> optionalBranchConverter() {
    return branch -> convertToBranchDto(branch, Lists.newArrayList());
  }

  public static Function<Branch, BranchDto> optionalBranchConverter(
      final List<BranchOpeningTime> branchOpeningTimes) {
    return branch -> convertToBranchDto(branch, branchOpeningTimes);
  }

  public static Function<Branch, BranchRequestBody> convertToRequest() {
    return col -> BranchRequestBody.builder().branchCode(col.getBranchCode())
        .branchNr(col.getBranchNr()).build();
  }

  private static List<BranchOpeningTimeDto> convertBranchOpeningTime(
      List<BranchOpeningTime> branchOpeningTimes) {
    if (CollectionUtils.isEmpty(branchOpeningTimes)) {
      return Lists.emptyList();
    }
    return branchOpeningTimes.stream()
        .map(BranchOpeningTimeConverters::convertToBranchOpeningTimeDto)
        .collect(Collectors.toList());
  }
}
