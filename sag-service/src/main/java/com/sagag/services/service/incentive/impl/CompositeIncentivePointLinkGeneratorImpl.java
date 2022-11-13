package com.sagag.services.service.incentive.impl;

import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.PasswordHash;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.incentive.api.IncentiveService;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.domain.IncentivePasswordHashDto;
import com.sagag.services.incentive.response.IncentiveLinkResponse;
import com.sagag.services.service.incentive.CompositeIncentivePointLinkGenerator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

@Component
@Slf4j
public class CompositeIncentivePointLinkGeneratorImpl implements CompositeIncentivePointLinkGenerator {

  @Autowired
  private IncentiveService incentiveService;

  @Autowired
  private LoginRepository loginRepo;

  @Override
  public IncentiveLinkResponse generate(UserInfo user) throws CookiePrivacyException {
    final Supplier<?>[] suppliers = new Supplier<?>[] {
      customerNrSupplier(user),
      usernameSupplier(user),
      passwordHashSupplier(user.getId())
    };
    return incentiveService.getIncentiveUrl(user.getSupportedAffiliate(), user.isShowHappyPoints(),
        suppliers);
  }

  private Supplier<String> usernameSupplier(UserInfo user) {
    log.debug("Building usernameSupplier");
    return user::getUsername;
  }

  private Supplier<IncentivePasswordHashDto> passwordHashSupplier(Long userId) {
    return () -> {
      log.debug("Building passwordHashSupplier");
      final Login login = loginRepo.findByUserId(userId)
          .orElseThrow(() -> new NoSuchElementException(
              String.format("Not found login info by user id = %s", userId)));

      final PasswordHash passwordHash = login.getPasswordHash();
      if (passwordHash == null) {
        throw new IllegalArgumentException("The given password hash must not be null");
      }
      IncentivePasswordHashDto incentivePasswordHash = new IncentivePasswordHashDto();
      incentivePasswordHash.setPassword(passwordHash.getPassword());
      incentivePasswordHash.setHashType(passwordHash.getHashType());
      return incentivePasswordHash;
    };
  }

  private Supplier<String> customerNrSupplier(UserInfo user) {
    log.debug("Building customerNrSupplier");
    return user::getCustNrStr;
  }
}
