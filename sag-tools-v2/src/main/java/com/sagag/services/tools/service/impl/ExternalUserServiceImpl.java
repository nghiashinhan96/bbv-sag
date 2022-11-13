package com.sagag.services.tools.service.impl;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.mdm.ExternalUserDto;
import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.repository.target.ExternalUserRepository;
import com.sagag.services.tools.service.ExternalUserService;
import com.sagag.services.tools.utils.SagBeanUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@OracleProfile
public class ExternalUserServiceImpl implements ExternalUserService {

  @Autowired
  private ExternalUserRepository externalUserRepo;


  @Override
  public ExternalUser addExternalUser(ExternalUserDto externalUserDto) {
    final ExternalUser externalUser = SagBeanUtils.map(externalUserDto, ExternalUser.class);
    return externalUserRepo.save(externalUser);
  }

  @Override
  public boolean isUsernameExisted(final String externalUsername) {
    if (StringUtils.isBlank(externalUsername)) {
      throw new IllegalArgumentException("Username should not be blank");
    }
    return externalUserRepo.isUsernameExisted(externalUsername);
  }

}
