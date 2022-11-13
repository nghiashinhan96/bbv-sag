package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.sagag.eshop.repo.api.collection.OrganisationCollectionRepository;
import com.sagag.eshop.repo.api.collection.OrganisationCollectionsSettingsRepository;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Integration test for Organisation JPA repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OrganisationRepositoryIT {

  private static final String ORG_TYPE = "AFFILIATE";

  private static final String ORG_NAME = "Derendinger-Austria";

  private static final String ORG_SHORTNAME = "derendinger-at";

  private static final String ORG_CODE = "300000";

  @Autowired
  private OrganisationRepository organisationRepo;

  @Autowired
  private OrganisationCollectionRepository orgCollectionRepo;

  @Autowired
  private OrganisationCollectionsSettingsRepository collectionSettingsRepo;

  @Test
  public void givenShortNameShouldGetOne() {
    final Optional<Organisation> orgOpt = organisationRepo.findOneByShortname(ORG_SHORTNAME);
    Assert.assertThat(orgOpt.isPresent(), Matchers.is(true));
    assertDerendingerAtOrganisation(orgOpt.get());
  }

  @Test
  public void givenAffiliateShouldGetFirstCustomer() {
    final List<Integer> orIds = organisationRepo.findOrgIdInAffiliate(ORG_SHORTNAME);
    Assert.assertThat(orIds, Matchers.notNullValue());
    Assert.assertThat(orIds, Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));

  }

  @Test
  public void givenOrgCodeShouldGetOne() {
    final Optional<Organisation> orgOpt = organisationRepo.findOneByOrgCode(ORG_CODE);
    Assert.assertThat(orgOpt.isPresent(), Matchers.is(true));
    assertDerendingerAtOrganisation(orgOpt.get());
  }

  private void assertDerendingerAtOrganisation(final Organisation org) {
    Assert.assertThat(org.getName(), Matchers.is(ORG_NAME));
    Assert.assertThat(org.getDescription(),
        Matchers.is("This is Derendinger Austria organisation"));
    Assert.assertThat(org.getShortname(), Matchers.is(ORG_SHORTNAME));
    Assert.assertThat(org.getOrgCode(), Matchers.is(ORG_CODE));
    OrganisationCollection orgCollection =
        orgCollectionRepo.findCollectionByOrgId(org.getId()).get();

    final Map<String, String> settings =
        collectionSettingsRepo.findByCollectionId(orgCollection.getId()).stream()
            .collect(Collectors.toMap(OrganisationCollectionsSettings::getSettingKey,
                OrganisationCollectionsSettings::getSettingValue));
    Assert.assertThat("shop@derendinger.at",
        Is.is(settings.get(SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName())));
  }

  @Test
  public void givenShortNameShouldGetCompanyName() {
    final Optional<String> shortNameOpt =
        organisationRepo.findCompanyNameByShortname(ORG_SHORTNAME);
    Assert.assertThat(shortNameOpt.isPresent(), Matchers.is(true));
    Assert.assertThat(shortNameOpt.get(), Matchers.is(ORG_NAME));
  }

  @Test
  public void givenShortNameShouldGetId() {
    final Optional<Integer> orgIdOpt = organisationRepo.findIdByShortName(ORG_SHORTNAME);
    Assert.assertThat(orgIdOpt.isPresent(), Matchers.is(true));
    Assert.assertThat(orgIdOpt.get(), Matchers.is(RepoDataTests.DDAT_AFFILIATE_ORG_ID));
  }

  @Test
  public void givenUserIdShouldGetAllBelongedOrganisations() {
    final List<Organisation> orgs =
        organisationRepo.findAllByUserId(RepoDataTests.USER_AX_AD_USER_ID);
    Assert.assertThat(orgs, Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
  }

  @Test
  public void givenOrgIdShouldGetOne() {
    final Optional<Organisation> org =
        organisationRepo.findByOrgId(RepoDataTests.DDAT_AFFILIATE_ORG_ID);
    Assert.assertThat(org.isPresent(), Matchers.is(true));
  }

  @Test
  public void shouldGetAllOrganisationsByNameAndOrgCodeAndAffiliate() {
    final String name = "Aigner R. GesmbH";
    final String orgCode = "1100005";
    final String affiliate = "derendinger-at";
    final Pageable pageable = PageUtils.DEF_PAGE;
    final Page<Organisation> orgs =
        organisationRepo.findByNameAndOrgCodeAndAffiliate(name, orgCode, affiliate, pageable);
    Assert.assertThat(orgs, Matchers.notNullValue());
    Assert.assertThat(orgs.getContent(), Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
  }

  @Test
  public void shouldGetAllOrganisationsByType() {
    final List<Organisation> orgs = organisationRepo.findOrganisationsByType(ORG_TYPE);
    Assert.assertThat(orgs, Matchers.notNullValue());
    Assert.assertThat(orgs, Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
  }

  @Test
  public void shouldCheckExistsByOrgCode() {
    Assert.assertThat(organisationRepo.isExistedByOrgCode(ORG_CODE), Matchers.is(true));
  }

  @Test
  public void findIdByOrgCode_shouldGetId_givenOrgCode() throws Exception {
    Integer id = organisationRepo.findIdByOrgCode("1100005").get();
    assertNotNull(id);
  }

  @Test
  public void findFinalOrgIdByOrgCode_shouldReturnResult_givenOrgCode() throws Exception {
    List<Long> finalOrdIds = organisationRepo.findFinalOrgIdByOrgCode("5132364");
    Assert.assertThat(finalOrdIds, Matchers.is(Matchers.not(Matchers.empty())));
  }

  @Test
  public void findNameById_shouldReturnCompanyName_givenOrgId() throws Exception {
    String name = organisationRepo.findNameById(139).orElse(StringUtils.EMPTY);
    assertThat(name, Matchers.is("final customer"));
  }

  @Test
  public void findIdsByParentId_shouldReturnOrgIds_givenParentIds() throws Exception {
    List<Integer> ids = organisationRepo.findIdsByParentId(139);
    assertNotNull(ids);
  }
}
