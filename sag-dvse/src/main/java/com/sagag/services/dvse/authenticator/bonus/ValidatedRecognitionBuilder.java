package com.sagag.services.dvse.authenticator.bonus;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.services.dvse.dto.bonus.Customer;
import com.sagag.services.dvse.dto.bonus.CustomerType;
import com.sagag.services.dvse.dto.bonus.Recognition;
import com.sagag.services.dvse.dto.bonus.RecognitionstateType;
import com.sagag.services.dvse.dto.bonus.User;
import com.sagag.services.dvse.dto.bonus.ValidatedRecognition;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatedRecognitionBuilder {

  public static ValidatedRecognition build(EshopUser eshopUser, Login login,
      Organisation orgCustomer, String accessPoint, String token, Locale locale) {
    final ValidatedRecognition recognition = new ValidatedRecognition();
    recognition.setRecognition(buildRecognition(login, accessPoint, token));
    recognition.setCustomer(buildBonusCustomer(orgCustomer));
    recognition.setUser(buildBonusUser(eshopUser, locale));
    return recognition;
  }

  private static Recognition buildRecognition(Login login, String accessPoint, String token) {
    final Recognition recognition = new Recognition();
    recognition.setState(RecognitionstateType.INVALID);
    if (login.isUserActive()) {
      recognition.setState(RecognitionstateType.VALID);
    }

    recognition.setAccessPoint(accessPoint);
    recognition.setToken(token);

    return recognition;
  }

  private static Customer buildBonusCustomer(Organisation orgCustomer) {
    final Customer customer = new Customer();
    customer.setNumber(BigDecimal.valueOf(Long.valueOf(orgCustomer.getOrgCode())));
    customer.setName(orgCustomer.getName());
    customer.setType(CustomerType.WHOLESALE);
    return customer;
  }

  private static User buildBonusUser(EshopUser eshopUser, Locale locale) {
    final User user = new User();
    user.setName(eshopUser.getUsername());
    user.setFirstname(eshopUser.getFirstName());
    user.setEmail(eshopUser.getEmail());
    user.setLangIso(StringUtils.upperCase(locale.getLanguage()));
    return user;
  }

}
