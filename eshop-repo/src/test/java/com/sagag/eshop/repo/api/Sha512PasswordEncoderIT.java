package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.security.crypto.sha512.Sha512PasswordEncoder;
import com.sagag.services.common.utils.HashUtils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepoApplication.class)
@EshopIntegrationTest
@Transactional
public class Sha512PasswordEncoderIT {

  @Autowired
  private Sha512PasswordEncoder pwdEncoder;

  @Autowired
  private LoginRepository repository;

  @Test
  public void test() {
    Login login = repository.findByUserId(10056l).orElse(null);
    final String raw = "1234";
    final String hash = login.getPasswordHash().getPasswordHash();
    final String salt = login.getPasswordHash().getPasswordSalt();
    final String encodedPasswordAndSaltAsString =
        HashUtils.getEncodedPasswordAndSaltAsString(hash, salt);
    boolean matched = pwdEncoder.matches(raw, encodedPasswordAndSaltAsString);
    Assert.assertThat(matched, Matchers.is(true));
  }

}
