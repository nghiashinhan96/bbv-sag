package com.sagag.services.rest.authorization.impl;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.service.dto.UserInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SameWholesalerAuthorizationImpl extends AbstractAuthorization {

  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Autowired
  private OrganisationRepository orgRepo;

  @Override
  protected boolean hasPermission(Authentication authed, Object targetDomainObject) {
    final Long userId = (Long) targetDomainObject;
    final UserInfo user = getUserInfo(authed);
    String customerOrgCode = vUserDetailRepo.findByUserId(userId)
        .map(VUserDetail::getOrgParentId)
        .flatMap(orgRepo::findByOrgId)
        .map(Organisation::getOrgCode)
        .orElse(null);
    return StringUtils.isNotBlank(customerOrgCode) && user.getCustNrStr().equals(customerOrgCode);
  }

  @Override
  public String authorizeType() {
    return "isTheSameWholesaler";
  }
}
