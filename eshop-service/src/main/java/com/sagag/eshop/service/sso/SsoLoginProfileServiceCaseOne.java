package com.sagag.eshop.service.sso;

import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.repo.entity.AadGroup;
import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileRequestDto;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileResponseDto;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.utils.SagOptional;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SsoLoginProfileServiceCaseOne extends AbstractSsoLoginProfileService {

  @Autowired
  private UserService userService;

  public SsoLoginProfileResponseDto process(SsoLoginProfileRequestDto request) {
    String email = request.getEmail();
    String username = request.getUserName();
    SsoLoginProfileResponseDto response = new SsoLoginProfileResponseDto();

    if (!StringUtils.isNoneBlank(email, username)) {
      return response;
    }

    Optional<AadAccounts> aadAccountOpt = axAccountService.searchSaleAccount(email);
    boolean hasSalesRole = userService.hasRoleByUsername(username, EshopAuthority.SALES_ASSISTANT);

    if (aadAccountOpt.isPresent() && hasSalesRole) {
      updateUuidForCurrentAadAccountIfHadBeenChanged(aadAccountOpt.get(), request.getUuid());
    } else {

      // fix for messy data should be removed in the future
      final Optional<ExternalUser> extUser =
          externalUserService.searchByUsernameAndApp(username, ExternalApp.AX);
      if (extUser.isPresent() && hasSalesRole) {
        response.setUsername(username);
        return response;
      }
      // fix for messy data should be removed in the future

      Optional<AadAccounts> aadAccountMatchedUuidOpt = findAadAccountByUuid(request.getUuid());
      if (aadAccountMatchedUuidOpt.isPresent()) {
        matchUuidCase(email, aadAccountMatchedUuidOpt.get());
      } else {
        Optional<List<AadGroup>> aadGroupsOpt =
            aadGroupRepo.findAllByUuids(request.getGroupUuids());
        if (!aadGroupsOpt.isPresent() && !aadAccountOpt.isPresent()) {
          return response;
        }
        SagOptional.of(aadAccountOpt).ifPresent(item -> createFullExternalUser(request))
            .orElse(() -> createBrandNewSalesAccount(request));
      }
    }
    response.setUsername(username);
    return response;
  }

  private void updateUuidForCurrentAadAccountIfHadBeenChanged(AadAccounts aadAccounts,
      String uuid) {
    if (StringUtils.isNotBlank(uuid) && !uuid.equals(aadAccounts.getUuid())) {
      updateUuidForCurrentAadAccount(aadAccounts, uuid);
    }
  }
}
