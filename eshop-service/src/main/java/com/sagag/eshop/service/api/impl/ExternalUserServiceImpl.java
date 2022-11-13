package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.ExternalUserRepository;
import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.converter.ExternalUserConverters;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ExternalUserServiceImpl implements ExternalUserService {

  @Autowired
  private ExternalUserRepository externalUserRepo;

  @Override
  public Optional<ExternalUserDto> getDvseExternalUserByUserId(Long userId) {
    Assert.notNull(userId, "The given userId must not be null");
    return externalUserRepo.findFirstByEshopUserIdAndExternalApp(userId, ExternalApp.DVSE)
        .map(ExternalUserConverters.optionalExternalUserConverter());
  }

  @Override
  public ExternalUser addExternalUser(ExternalUserDto externalUserDto) {
    final ExternalUser externalUser = SagBeanUtils.map(externalUserDto, ExternalUser.class);
    return externalUserRepo.save(externalUser);
  }

  @Override
  public Optional<ExternalUserDto> getExternalUser(String username, String password,
      ExternalApp app) {
    final Optional<ExternalUser> extUserOpt =
        externalUserRepo.findFirstByUsernameAndPasswordAndExternalApp(username, password, app);
    return extUserOpt.map(ExternalUserConverters.optionalExternalUserConverter());
  }

  @Override
  public Page<ExternalUserDto> searchInactiveExternalUser(ExternalApp app, Pageable pageable) {
    Assert.notNull(app, "The given external app must not be null");
    Assert.notNull(pageable, "The pagable must not be null");
    final Page<ExternalUser> extUsers =
        externalUserRepo.findByActiveAndExternalApp(false, app, pageable);
    return extUsers.map(ExternalUserConverters.optionalExternalUserConverter());
  }

  @Override
  public List<ExternalUserDto> updateExternalUsers(List<ExternalUserDto> externalUserDtos) {

    Assert.notEmpty(externalUserDtos, "The given external users must not be empty");
    List<ExternalUser> externalUsers = externalUserDtos.stream()
        .map(extUser -> SagBeanUtils.map(extUser, ExternalUser.class)).collect(Collectors.toList());
    List<ExternalUser> entities = externalUserRepo.saveAll(externalUsers);
    return entities.stream().map(ExternalUserConverters.optionalExternalUserConverter())
        .collect(Collectors.toList());
  }

  @Override
  public Optional<ExternalUser> searchByUsernameAndApp(String username, ExternalApp app) {
    return externalUserRepo.findFirstByUsernameAndExternalApp(username, app)
        .filter(user -> username.equals(user.getUsername()));
  }

  @Override
  public void removeExternalUserById(Long id) {
    log.debug("Deleting external user with id = {}", id);

    // Should not check existing, because Spring JPA interface handle it
    externalUserRepo.deleteById(id);
  }

  @Override
  public boolean isUsernameExisted(final String externalUsername) {
    if (StringUtils.isBlank(externalUsername)) {
      throw new IllegalArgumentException("Username should not be blank");
    }
    return externalUserRepo.isUsernameExisted(externalUsername);
  }

  @Override
  public int countVirtualUserExisted(String orgCode) {
    Assert.hasText(orgCode, "orgCode should not be empty");
    return externalUserRepo.countVirtualUserExisted(orgCode);
  }

  @Override
  public Optional<ExternalUserDto> getAvailableVirtualUser(final String customerNumber) {
    Asserts.notEmpty(customerNumber, "CustomerNumber should not be empty.");
    return externalUserRepo.getAvailableVirtualUser(customerNumber)
        .map(user -> SagBeanUtils.map(user, ExternalUserDto.class));
  }

  @Override
  public void releaseVirtualUsers(List<Long> eshopUserIds) {
    Assert.notEmpty(eshopUserIds, "eshopUserIds should not be empty.");
    final long start = System.currentTimeMillis();
    externalUserRepo.releaseVirtualUsers(eshopUserIds);
    log.debug(
        "Perf: ExternalUserServiceImpl -> releaseVirtualUsers {} ms",
        System.currentTimeMillis() - start);
  }

  @Override
  public List<ExternalUserDto> searchExternalUsersByUserId(Long userId) {
    if (userId == null) {
      return Collections.emptyList();
    }
    return externalUserRepo.findByEshopUserId(userId).stream()
        .map(ExternalUserConverters.optionalExternalUserConverter()).collect(Collectors.toList());
  }
}
