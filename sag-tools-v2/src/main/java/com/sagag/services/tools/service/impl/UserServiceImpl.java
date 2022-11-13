package com.sagag.services.tools.service.impl;


import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.repository.target.VUserDetailRepository;
import com.sagag.services.tools.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@OracleProfile
public class UserServiceImpl implements UserService {

  @Autowired
  private VUserDetailRepository vUserDetailRepository;

  @Override
  public Optional<Integer> getOrgIdByUserId(final Long userId) {
    return vUserDetailRepository.findOrgIdByUserId(userId);
  }
}
