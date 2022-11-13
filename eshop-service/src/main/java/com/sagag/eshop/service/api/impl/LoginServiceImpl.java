package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.password.DefaultPasswordHashBuilder;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.services.common.enums.HashType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Login implementation service class.
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private DefaultPasswordHashBuilder passwordHashBuilder;

  @Override
  public void update(Login login) {
    loginRepository.save(login);
  }

  @Override
  public void updateLastOnBehalfOfDate(final int id, final Date lastOnBehalfOfDate) {
    loginRepository.updateLastOnBehalfOfDate(lastOnBehalfOfDate, id);
  }

  @Override
  public void updateFirstLoginDate(final int id, final Date firstSignInDate) {
    loginRepository.updateFirstLoginDate(firstSignInDate, id);
  }

  @Override
  public Login getLoginForUser(long userId) {
    return loginRepository.findByUserId(userId).orElseThrow(
        () -> new NoSuchElementException(
            String.format("Not found login info by user id = %s", userId)));
  }

  @Override
  public Login createLogin(EshopUser eshopUser, String rawPassword) {
    Login login = new Login();
    login.setPasswordHash(passwordHashBuilder.buildPasswordHash(rawPassword));
    login.setUserActive(true);
    login.setEshopUser(eshopUser);
    return loginRepository.save(login);
  }

  @Override
  public Login createAPMUserLogin(EshopUser eshopUser, String passwordHash, String passwordSalt) {
    Login login = new Login();
    login.setPasswordHash(
        passwordHashBuilder.buildPasswordHash(passwordHash, passwordSalt, HashType.SHA_512));
    login.setUserActive(true);
    login.setEshopUser(eshopUser);
    return loginRepository.save(login);
  }
}
