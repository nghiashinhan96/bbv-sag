package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * Integration test class for role {@link LoginRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class LoginRepositoryIT {

  @Autowired
  private LoginRepository repository;

  @Test
  public void shouldGetLoginInfoWithValidUserId() {
    final long userId = 27L;
    Optional<Login> loginOpt = repository.findByUserId(userId);
    Assert.assertThat(loginOpt.isPresent(), Matchers.is(true));
  }

  @Test
  public void test_updateOnBehalfOfDate() {
    Date now = Calendar.getInstance().getTime();
    repository.updateLastOnBehalfOfDate(now, 32);
  }

}
