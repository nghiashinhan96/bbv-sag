package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.AadAccountsRepository;
import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.ExternalUserRepository;
import com.sagag.eshop.repo.criteria.AadAccountsSearchCriteria;
import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.eshop.repo.specification.AadAccountsSpecification;
import com.sagag.eshop.service.api.AadAccountsService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.converter.AadAccountsConverters;
import com.sagag.eshop.service.exception.DuplicatedEmailException;
import com.sagag.eshop.service.validator.aad.AadAccoutDuplicatedEmailValidator;
import com.sagag.eshop.service.validator.aad.AadAccoutRequiredFieldsValidator;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.criteria.AadAccountsSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.AadAccountsDto;
import com.sagag.services.domain.eshop.dto.AadAccountsSearchResultDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class AadAccountsServiceImpl implements AadAccountsService {

  private static final String GROUP_SALES = "SALES";

  @Autowired
  private AadAccountsRepository aadAccountsRepo;

  @Autowired
  private AadAccoutDuplicatedEmailValidator duplicatedEmailValidator;

  @Autowired
  private AadAccoutRequiredFieldsValidator requiredFieldsValidator;

  @Autowired
  private ExternalUserService externalUserService;

  @Autowired
  private ExternalUserRepository externalUserRepository;

  @Autowired
  private EshopUserRepository eshopUserRepository;

  @Transactional
  @Override
  public void create(AadAccountsDto dto) throws DuplicatedEmailException, ValidationException {
    requiredFieldsValidator.validate(dto);
    String email = dto.getPrimaryContactEmail();
    if (!duplicatedEmailValidator.validate(email)) {
      throw new DuplicatedEmailException(email);
    }
    AadAccounts entity = SagBeanUtils.map(dto, AadAccounts.class);
    entity.setPermitGroup(GROUP_SALES);
    entity.setCreatedDate(Calendar.getInstance().getTime());
    aadAccountsRepo.save(entity);
  }

  @Override
  public Page<AadAccountsSearchResultDto> search(AadAccountsSearchRequestCriteria requestCriteria,
      Pageable pageable) {
    AadAccountsSearchCriteria searchCriteria =
        SagBeanUtils.map(requestCriteria, AadAccountsSearchCriteria.class);

    return aadAccountsRepo
        .findAll(AadAccountsSpecification.searchMessages(searchCriteria), pageable)
        .map(AadAccountsConverters.toSearchResultItemDto());
  }

  @Transactional
  @Override
  public void update(AadAccountsDto dto, int id)
      throws DuplicatedEmailException, ValidationException {
    requiredFieldsValidator.validate(dto);
    AadAccounts entity = aadAccountsRepo.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Not found this account"));

    String newEmail = dto.getPrimaryContactEmail();
    String oldEmail = entity.getPrimaryContactEmail();
    if (!newEmail.equals(oldEmail) && !duplicatedEmailValidator.validate(newEmail)) {
      throw new DuplicatedEmailException(newEmail);
    }
    updateSalesUserProfile(oldEmail, newEmail);
    aadAccountsRepo.save(doUpdate(entity, dto));
  }

  private AadAccounts doUpdate(AadAccounts entity, AadAccountsDto dto) {
    entity.setFirstName(dto.getFirstName());
    entity.setLastName(dto.getLastName());
    entity.setPrimaryContactEmail(dto.getPrimaryContactEmail());
    entity.setPersonalNumber(dto.getPersonalNumber());
    entity.setGender(dto.getGender());
    entity.setLegalEntityId(dto.getLegalEntityId());
    return entity;
  }

  private void updateSalesUserProfile(String oldEmail, String newEmail) {
    externalUserService.searchByUsernameAndApp(oldEmail, ExternalApp.AX)
        .ifPresent(externalUser -> updateExternalUser(externalUser, newEmail));
  }

  private void updateExternalUser(ExternalUser externalUser, String newEmail) {
    Long eshopUserId = externalUser.getEshopUserId();
    externalUser.setUsername(newEmail);
    externalUserRepository.save(externalUser);
    if (!Objects.isNull(eshopUserId)) {
      eshopUserRepository.findById(eshopUserId).ifPresent(user -> {
        if (user.isSalesAssistantRole()) {
          user.setUsername(newEmail);
          user.setEmail(newEmail);
          eshopUserRepository.save(user);
        }
      });
    }
  }

  @Override
  public AadAccountsSearchResultDto findById(Integer id) {
    Assert.notNull(id, "Id must not be null");
    return SagBeanUtils.map(this.aadAccountsRepo.getOne(id), AadAccountsSearchResultDto.class);
  }

  @Override
  public void updateEmail(AadAccounts aadAccounts, String email) {
    String oldEmail = aadAccounts.getPrimaryContactEmail();
    aadAccounts.setPrimaryContactEmail(email);
    aadAccountsRepo.save(aadAccounts);
    updateSalesUserProfile(oldEmail, email);
  }

  @Override
  public Optional<AadAccountsSearchResultDto> findByUserId(Long userId) {
    Assert.notNull(userId, "user Id must not be null");
    final AadAccounts aadAccount = aadAccountsRepo.findByUserId(userId);
    if (aadAccount == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(SagBeanUtils.map(aadAccount, AadAccountsSearchResultDto.class));
  }
}
