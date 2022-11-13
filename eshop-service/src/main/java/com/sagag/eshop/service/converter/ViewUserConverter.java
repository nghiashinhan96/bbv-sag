package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.VActiveUser;
import com.sagag.services.domain.eshop.dto.UserSearchResultItemDto;

import lombok.experimental.UtilityClass;

import org.springframework.core.convert.converter.Converter;

@UtilityClass
public class ViewUserConverter {

  public static UserSearchResultItemDto convert(VActiveUser viewUser) {
    return UserSearchResultItemDto.builder()
        .id(viewUser.getId())
        .affiliate(viewUser.getAffiliate())
        .organisationCode(viewUser.getOrgCode())
        .organisationName(viewUser.getOrgName())
        .userName(viewUser.getUsername())
        .email(viewUser.getEmail())
        .telephone(viewUser.getPhone())
        .roleName(viewUser.getRoleName())
        .isUserActive(viewUser.getIsUserActive())
        .build();
  }

  public static Converter<VActiveUser, UserSearchResultItemDto> toUserSearchResultItemDto() {
    return ViewUserConverter::convert;
  }
}
