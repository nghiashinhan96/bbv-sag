package com.sagag.eshop.service.api.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.sagag.eshop.repo.api.GroupUserRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.AxUserDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.UserManagementDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class integration test for {@link UserService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class UserServiceImplIT {

  private static final String USERNAME_NON_SALE = "truong.le";

  private static final String SSO_USER = "adreas.kaltenbrunner@derendinger.at";

  @Autowired
  private UserService userService;

  @Autowired
  private GroupUserRepository groupUserRepo;

  @Test
  public void testGetUserByUsernameTechnomagGroupAdmin() {
    final List<EshopUser> users = userService.getUsersByUsername("technomag-at1");
    assertThat(false, is(users.isEmpty()));
    assertThat("technomag-at1", is(users.get(0).getUsername()));
  }

  @Test
  public void testGetUserByUsernameDerendingerNormalUser() {
    final List<EshopUser> users = userService.getUsersByUsername("tuan1.ax");
    assertThat(false, is(users.isEmpty()));
    assertThat("tuan1.ax", is(users.get(0).getUsername()));
  }

  @Test
  @Ignore
  @Deprecated
  public void givenNonSaleProfile_shouldCreateAxUserOnEConnect() {
    final UserProfileDto userProfileDto = new UserProfileDto();
    userProfileDto.setSalutationId(1);
    userProfileDto.setUserName(USERNAME_NON_SALE);
    userProfileDto.setSurName("LE");
    userProfileDto.setFirstName("Truong Q.");
    userProfileDto.setEmail("lqtruong@gmail.com");
    userProfileDto.setPhoneNumber("123456789");
    userProfileDto.setLanguageId(1);
    userProfileDto.setTypeId(1);
    userProfileDto.setHourlyRate(11D);
    userProfileDto.setAccessUrl("https://sts.windows.net/87ba7b57-cd64-42e8-a3c4-6300e4bdca25/");
    final AxUserDto extUser = userService.createAXUser(userProfileDto);
    final List<EshopUser> users = userService.getUsersByUsername(USERNAME_NON_SALE);
    Assert.assertThat(users.size(), Matchers.greaterThanOrEqualTo(1));
    final EshopUser user = users.get(0);
    Assert.assertThat(USERNAME_NON_SALE, Is.is(extUser.getUsername()));
    Assert.assertThat(extUser.getEshopUserId(), Is.is(user.getId()));
    Assert.assertThat(groupUserRepo.findOneByEshopUser(user).isPresent(), Matchers.is(false));
  }

  @Test
  public void findAffiliateShortNameById_shouldReturnAffiliateShortName_givenUserId()
      throws Exception {
    String orgShortName = userService.findAffiliateShortNameById(46L).get();
    assertThat(orgShortName, Matchers.is("derendinger-at"));
  }

  @Test
  @Ignore
  @Deprecated
  public void createAXUser_shouldAssignToSaleGroup_givenUserHasSaleAadAccountAndHasNoRole() {
    String username = "adreas.kaltenbrunner@derendinger.at";
    final UserProfileDto userProfileDto = new UserProfileDto();
    userProfileDto.setUserName(username);
    final AxUserDto extUser = userService.createAXUser(userProfileDto);
    final List<EshopUser> users = userService.getUsersByUsername(username);
    String groupName = users.get(0).getGroupUsers().get(0).getEshopGroup().getName();
    assertThat(extUser.getUsername(), Matchers.is(username));
    assertThat(groupName, Matchers.is("SALES_ASSISTANT"));
  }

  @Test
  public void testGetSessionUserInfoSucceed() {
    final long id = 1l; // Username: admin

    UserInfo user = userService.getSessionUserInfo(id);

    Assert.assertThat(user, Matchers.notNullValue());
    Assert.assertThat(user.getSignInDate(), Matchers.notNullValue());
    Assert.assertThat(user.getFirstLoginDate(), Matchers.notNullValue());
  }

  @Test
  public void testGetUserSameOrganisation() {
    UserInfo user = new UserInfo();
    user.setId(1l);
    user.setOrganisationId(1);
    final List<UserManagementDto> users = userService.getUserSameOrganisation(user);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(users));
  }

  @Test
  public void testHasRoleByUserName() {
    boolean hasSalesRole =
        userService.hasRoleByUsername(SSO_USER, EshopAuthority.SALES_ASSISTANT);
    Assert.assertThat(hasSalesRole, Matchers.is(true));
  }

  @Test
  public void testGetUserProfileTemplate() {
    final UserProfileDto profile = userService.getUserProfileTemplate();
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(profile));
  }
}
