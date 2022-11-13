package com.sagag.eshop.service.sso;

import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.repo.entity.AadGroup;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileRequestDto;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileResponseDto;
import com.sagag.services.common.utils.SagOptional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SsoLoginProfileServiceCaseTwo extends AbstractSsoLoginProfileService {

  public SsoLoginProfileResponseDto process(SsoLoginProfileRequestDto request) {
    String email = request.getEmail();
    String username = request.getUserName();
    SsoLoginProfileResponseDto response = new SsoLoginProfileResponseDto();

    if (!StringUtils.isNoneBlank(email, username)) {
      return response;
    }

    Optional<List<AadGroup>> aadGroupsOpt = aadGroupRepo.findAllByUuids(request.getGroupUuids());
    if (!aadGroupsOpt.isPresent()) {
      return response;
    }

    Optional<AadAccounts> aadAccountMatchedUuidOpt = findAadAccountByUuid(request.getUuid());
    SagOptional.of(aadAccountMatchedUuidOpt)
        .ifPresent(aadAccountMatchedUuid -> matchUuidCase(email, aadAccountMatchedUuid))
        .orElse(() -> notMatchUuidCase(request, email));

    response.setUsername(username);
    return response;
  }

  private void notMatchUuidCase(SsoLoginProfileRequestDto request, String email) {
    SagOptional.of(axAccountService.searchSaleAccount(email))
        .ifPresent(aadAccountHasSameEmail -> updateUuidForCurrentAadAccount(aadAccountHasSameEmail,
            request.getUuid()))
        .orElse(() -> createBrandNewSalesAccount(request));
  }
}
