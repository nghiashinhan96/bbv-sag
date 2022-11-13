package com.sagag.services.dvse.dto.dvse;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConnectUser extends UserInfo {

  private static final long serialVersionUID = -2460897740133137311L;

  private ExternalUserDto externalUser;

  private DateTime lastLoginTime;

  public ConnectUser(final UserInfo user, final ExternalUserDto externalUser) {
    super();

    // Map user info
    setId(user.getId());
    setUsername(user.getUsername());
    setFirstName(user.getFirstName());
    setLastName(user.getLastName());
    setLanguage(user.getLanguage());
    setCompanyName(user.getCompanyName());
    setDefaultBranchName(user.getDefaultBranchName());
    setSalesUsername(user.getSalesUsername());
    setSalesId(user.getSalesId());
    setAffiliateShortName(user.getAffiliateShortName());
    setSettings(user.getSettings());
    setEmail(user.getEmail());
    setVatConfirm(user.isVatConfirm());
    setHourlyRate(user.getHourlyRate());
    setRoles(user.getRoles());
    setPermissions(user.getPermissions());
    setCustomer(user.getCustomer());
    setAddresses(user.getAddresses());
    setOrganisationId(user.getOrganisationId());
    setUserLocale(user.getUserLocale());

    // Map external user
    this.externalUser = externalUser;
  }

  public String getAffiliateName() {
    return this.getCompanyName();
  }

  public SupportedAffiliate getAffiliate() {
    return SupportedAffiliate.fromCompanyName(getAffiliateName());
  }

  @Override
  public String getDeliveryAddressId() {
    if (Objects.isNull(getSettings().getUserSettings())
        || StringUtils.isBlank(getSettings().getUserSettings().getDeliveryAddressId())) {
      return StringUtils.EMPTY;
    }
    return getSettings().getUserSettings().getDeliveryAddressId();
  }

  public Integer getUserSettingId() {
    if (Objects.isNull(getSettings().getUserSettings())) {
      throw new IllegalArgumentException("Not found any user settings info");
    }
    return getSettings().getUserSettings().getId();
  }

}
