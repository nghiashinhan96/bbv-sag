package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Integration test class for {@link BranchRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class BranchRepositoryIT {

  @Autowired
  private BranchRepository branchRepository;

  @Test
  public void testFindOneById() {
    final Optional<Branch> branch = branchRepository.findById(1);
    Assert.assertThat(branch.isPresent(), Is.is(true));
    Assert.assertThat(branch.get().getId(), Is.is(1));
  }

  @Test
  public void testFindOneByBranchNr() {
    final Optional<Branch> branchOpt = branchRepository.findOneByBranchNr(1001);
    Assert.assertThat(branchOpt.isPresent(), Is.is(true));

    final Branch branch = branchOpt.get();

    Assert.assertThat(branch.getBranchNr(), Is.is(1001));
    Assert.assertThat(branch.getBranchCode(), Is.is("LEO"));
    Assert.assertThat(branch.getOrgId(), Is.is(10003));
    Assert.assertThat(branch.getZip(), Is.is("4060"));
    Assert.assertThat(branch.getAddressStreet(), Is.is("Edtstrasse 13"));
    Assert.assertThat(branch.getAddressCity(), Is.is("Leonding"));
    Assert.assertThat(branch.getAddressDesc(), Is.is("Filiale Leonding"));
    Assert.assertThat(branch.getAddressCountry(), Is.is("Österreich"));
    Assert.assertThat(branch.getCountryId(), Is.is(17));
    Assert.assertThat(branch.getRegionId(), Is.is("AUT"));
    Assert.assertThat(branch.getPrimaryFax(), Is.is("+43 732 678 181 200"));
    Assert.assertThat(branch.getPrimaryEmail(), Is.is("filleo@derendinger.at"));
    Assert.assertThat(branch.getPrimaryPhone(), Is.is("+43 732 678 181"));
    Assert.assertThat(branch.getPrimaryUrl(), Is.is("www.derendinger.at"));
    Assert.assertThat(branch.getValidForKSL(), Is.is(true));
  }

  @Test
  public void testCheckExistingBranchBranchNr() {
    final boolean isExistingBranch = branchRepository.checkExistingBranchByBranchNr(1001);
    Assert.assertThat(isExistingBranch, Is.is(true));
  }

  @Test
  public void testCheckExistingBranchBranchNr_NotExits() {
    final boolean isExistingBranch = branchRepository.checkExistingBranchByBranchNr(20000000);
    Assert.assertThat(isExistingBranch, Is.is(false));
  }

  @Test
  public void testCheckExistingBranchBranchCode() {
    final boolean isExistingBranch = branchRepository.checkExistingBranchByBranchCode("LIE");
    Assert.assertThat(isExistingBranch, Is.is(true));
  }

  @Test
  public void testCheckExistingBranchBranchCode_NotExits() {
    final boolean isExistingBranch = branchRepository.checkExistingBranchByBranchCode("ABCDEF");
    Assert.assertThat(isExistingBranch, Is.is(false));
  }

  @Test
  public void testFindByCountries_Multi() {
    final List<Branch> branches = branchRepository.findByCountries(Arrays.asList(17));
    Assert.assertThat(CollectionUtils.isEmpty(branches), Is.is(false));
    Assert.assertThat(branches.size(), Is.is(32));
  }

  @Test
  public void findAllBranchNrs_shouldReturnAllBranchNrs() throws Exception {
    final List<Integer> branchNumbers = branchRepository.findAllBranchNrs();
    assertNotNull(branchNumbers);
  }
}
