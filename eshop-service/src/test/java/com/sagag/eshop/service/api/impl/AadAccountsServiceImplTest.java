package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.AadAccountsRepository;
import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.ExternalUserRepository;
import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.DuplicatedEmailException;
import com.sagag.eshop.service.validator.aad.AadAccoutDuplicatedEmailValidator;
import com.sagag.eshop.service.validator.aad.AadAccoutRequiredFieldsValidator;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.criteria.AadAccountsSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.AadAccountsDto;
import com.sagag.services.domain.eshop.dto.AadAccountsSearchResultDto;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
public class AadAccountsServiceImplTest {

  @InjectMocks
  private AadAccountsServiceImpl aadAccountsService;

  @Mock
  private AadAccountsRepository aadAccountsRepo;

  @Mock
  private AadAccoutDuplicatedEmailValidator duplicatedEmailValidator;

  @Mock
  private AadAccoutRequiredFieldsValidator requiredFieldsValidator;

  @Mock
  private UserService userService;

  @Mock
  private ExternalUserService externalUserService;

  @Mock
  private ExternalUserRepository externalUserRepository;

  @Mock
  private EshopUserRepository eshopUserRepository;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void search_shouldReturnResults_givenFoundFirstName() throws Exception {
    AadAccountsSearchRequestCriteria requestCriteria =
        AadAccountsSearchRequestCriteria.builder().firstName("Son").build();

    AadAccounts account = new AadAccounts();
    account.setFirstName("Son");
    account.setLastName("Jon Min");
    Page<AadAccounts> page = new PageImpl<>(Arrays.asList(account));
    when(aadAccountsRepo.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
        .thenReturn(page);

    List<AadAccountsSearchResultDto> contents =
        aadAccountsService.search(requestCriteria, PageUtils.DEF_PAGE).getContent();
    assertThat(contents.size(), Matchers.is(1));
    assertThat(contents.get(0).getLastName(), Matchers.is("Jon Min"));
  }

  @Test(expected = DuplicatedEmailException.class)
  public void update_shouldThrowsException_givenDuplicatedEmail() throws Exception {
    AadAccounts account = getAccountEntity();
    account.setPrimaryContactEmail("sonmin_1@bbv.ch");
    when(aadAccountsRepo.findById(1)).thenReturn(Optional.of(account));
    aadAccountsService.update(getAccountDto(), 1);
  }

  @Test
  public void update_shouldUpdateSuccessfully_givenValidData() throws Exception {
    when(aadAccountsRepo.findById(1)).thenReturn(Optional.of(getAccountEntity()));
    when(requiredFieldsValidator.validate(getAccountDto())).thenReturn(true);
    aadAccountsService.update(getAccountDto(), 1);
    verify(aadAccountsRepo, times(1)).save(Mockito.any(AadAccounts.class));
  }

  private AadAccountsDto getAccountDto() {
    AadAccountsDto dto = new AadAccountsDto();
    dto.setPrimaryContactEmail("sonmin@bbv.ch");
    dto.setFirstName("Son");
    dto.setLastName("Jon Min");
    dto.setPersonalNumber("123");
    return dto;
  }

  private AadAccounts getAccountEntity() {
    AadAccounts entity = new AadAccounts();
    entity.setPrimaryContactEmail("sonmin@bbv.ch");
    return entity;
  }

  @Test
  public void findById_shouldReturnFoundAccount_givenId() throws Exception {
    AadAccounts account = new AadAccounts();
    account.setPrimaryContactEmail("account_01@bbv.ch");
    when(aadAccountsRepo.getOne(1)).thenReturn(account);
    AadAccountsSearchResultDto results = aadAccountsService.findById(1);
    assertThat(results.getPrimaryContactEmail(), Matchers.is(account.getPrimaryContactEmail()));
  }

  @Test
  public void update_shouldUpdateSalesUserProfileEither_givenValidDataWithChangedEmail()
      throws Exception {
    String originalEmail = "sonmin@bbv.ch";
    String updatedEmail = "updated_email@bbv.ch";
    when(aadAccountsRepo.findById(1)).thenReturn(Optional.of(getAccountEntity()));
    when(duplicatedEmailValidator.validate(updatedEmail)).thenReturn(true);

    EshopRole role = new EshopRole();
    role.setName("SALES_ASSISTANT");
    GroupRole groupRole = new GroupRole();
    groupRole.setEshopRole(role);
    EshopGroup group = new EshopGroup();
    group.setGroupRoles(Arrays.asList(groupRole));
    GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(group);

    EshopUser user = new EshopUser();
    user.setId(1L);
    user.setGroupUsers(Arrays.asList(groupUser));
    ExternalUser externalUser = new ExternalUser();
    externalUser.setEshopUserId(1L);
    when(externalUserService.searchByUsernameAndApp(originalEmail, ExternalApp.AX))
        .thenReturn(Optional.of(externalUser));
    when(eshopUserRepository.findById(1L)).thenReturn(Optional.of(user));
    AadAccountsDto dto = getAccountDto();
    dto.setPrimaryContactEmail(updatedEmail);
    aadAccountsService.update(dto, 1);
    verify(aadAccountsRepo, times(1)).save(Mockito.any(AadAccounts.class));
    assertThat(user.getUsername(), Matchers.is(updatedEmail));
    assertThat(user.getEmail(), Matchers.is(updatedEmail));
    assertThat(externalUser.getUsername(), Matchers.is(updatedEmail));
  }
}
