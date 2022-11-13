package com.sagag.services.tools.domain;

import com.sagag.services.tools.domain.target.AadAccounts;
import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.domain.target.GroupUser;
import com.sagag.services.tools.domain.target.Login;
import com.sagag.services.tools.domain.target.UserSettings;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SsoSalesUserOutput {

  private AadAccounts aadAccounts;
  private ExternalUser externalUser;
  private EshopUser eshopUser;
  private UserSettings userSettings;
  private GroupUser groupUser;
  private Login login;
}
