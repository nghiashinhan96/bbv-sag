package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.ExternalUserRepository;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

/**
 * UT to verify for {@link ExternalUserServiceImpl}.
 */
@EshopMockitoJUnitRunner
public class ExternalUserServiceImplTest {

  @InjectMocks
  private ExternalUserServiceImpl externalUserService;

  @Mock
  private ExternalUserRepository externalUserRepo;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetExternalUserByUserIdWithNotFoundUser() throws Exception {

    Long userId = 1L;
    Mockito.when(externalUserRepo.findFirstByEshopUserIdAndExternalApp(
        userId, ExternalApp.DVSE)).thenReturn(Optional.empty());

    final Optional<ExternalUserDto> externalUser =
        externalUserService.getDvseExternalUserByUserId(userId);
    Assert.assertThat(externalUser.isPresent(), Matchers.equalTo(false));

    Mockito.verify(externalUserRepo, Mockito.times(1))
    .findFirstByEshopUserIdAndExternalApp(userId, ExternalApp.DVSE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetExternalUserByUserIdWithNullUserId() throws Exception {

    Long userId = 1L;
    Mockito.when(externalUserRepo.findFirstByEshopUserIdAndExternalApp(
        userId, ExternalApp.DVSE)).thenReturn(Optional.empty());

    final Optional<ExternalUserDto> externalUser =
        externalUserService.getDvseExternalUserByUserId(null);
    Assert.assertThat(externalUser.isPresent(), Matchers.equalTo(false));

    Mockito.verify(externalUserRepo, Mockito.times(1))
    .findFirstByEshopUserIdAndExternalApp(userId, ExternalApp.DVSE);
  }
}
