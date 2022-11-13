package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.WssBranch;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Integration test class for {@link WssBranchRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class WssBranchRepositoryIT {

  @Autowired
  private WssBranchRepository wssBranchRepository;

  @Test
  public void testFindOneById() {
    final Optional<WssBranch> branch = wssBranchRepository.findById(1);
    Assert.assertThat(branch.isPresent(), Is.is(true));
    Assert.assertThat(branch.get().getId(), Is.is(1));
  }

  @Test
  public void findAllBranchNrs_shouldReturnAllBranchNrs() throws Exception {
    final List<Integer> branchNumbers = wssBranchRepository.findAllBranchNrs();
    assertNotNull(branchNumbers);
  }
}
