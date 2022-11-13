package com.sagag.eshop.service.validator;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.InvalidUserProfileException;
import com.sagag.eshop.service.exception.UsernameDuplicationException;
import com.sagag.eshop.service.validator.criteria.UserProfileValidateCriteria;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * IT to verify for {@link UserProfileValidator}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class UserProfileValidatorIT {

  @Autowired
  private UserProfileValidator validator;

  @Test
  public void shouldReturnTrueWithFullUserProfileInfo() throws ValidationException {
    final UserProfileDto profile = buildDummyUserProfileDto();

    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    boolean result = validator.validate(new UserProfileValidateCriteria(profile, affiliate));
    Assert.assertThat(result, Matchers.is(true));
  }

  @Test(expected = InvalidUserProfileException.class)
  public void shouldThrowEmailInvalid() throws ValidationException {
    final UserProfileDto profile = buildDummyUserProfileDto();
    profile.setEmail("duong.dang"); // Wrong email format
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    validator.validate(new UserProfileValidateCriteria(profile, affiliate));
  }

  @Test(expected = InvalidUserProfileException.class)
  public void shouldThrowUsernameInvalid() throws ValidationException {
    final UserProfileDto profile = buildDummyUserProfileDto();
    profile.setUserName(StringUtils.EMPTY);
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    validator.validate(new UserProfileValidateCriteria(profile, affiliate));
  }

  @Test(expected = InvalidUserProfileException.class)
  public void shouldValidateWithEmptyUserProfile() throws ValidationException {
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    validator.validate(new UserProfileValidateCriteria(new UserProfileDto(), affiliate));
  }

  @Test(expected = UsernameDuplicationException.class)
  public void validate_shouldThrowException_givenDuplicatedUsername() throws ValidationException {
    final UserProfileDto profile = buildDummyUserProfileDto();
    profile.setUserName("tuan2.ax");
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    validator.validate(new UserProfileValidateCriteria(profile, affiliate));
  }

  private static UserProfileDto buildDummyUserProfileDto() {
    final UserProfileDto profile = new UserProfileDto();
    profile.setId(27L);
    profile.setUserName("tuan1.ax");
    profile.setFirstName("first name");
    profile.setSurName("sur name");
    profile.setEmail("duong.dang@bbv.vn");
    return profile;
  }
}
