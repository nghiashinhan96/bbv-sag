package com.sagag.eshop.service.api.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import com.sagag.eshop.repo.api.FinalCustomerPropertyRepository;
import com.sagag.eshop.repo.api.VFinalCustomerRepository;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchCriteria;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchSortCriteria;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchTermCriteria;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.repo.entity.VFinalCustomer;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerSettingDto;
import com.sagag.services.common.enums.FinalCustomerType;
import com.sagag.services.common.utils.PageUtils;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FinalCustomerServiceImplTest {

  private static final int CUSTOMER_ORG_ID = 137;

  @InjectMocks
  private FinalCustomerServiceImpl finalCustomerService;

  @Mock
  private FinalCustomerPropertyRepository finalCustomerPropertyRepo;

  @Mock
  private VFinalCustomerRepository vFinalCustomerRepo;

  @Test
  public void getFinalCustomerSettingsShouldReturnResultGivenOrgId() {
    final Long orgId = 2L;
    Mockito.when(finalCustomerPropertyRepo.findByOrgId(orgId))
        .thenReturn(buildCustomerProperties(orgId));
    FinalCustomerSettingDto result = finalCustomerService.getFinalCustomerSettings(orgId);
    Assert.assertThat(result.getCustomerNumber(), is(not(isEmptyString())));
    Assert.assertThat(result.getEmail(), is(not(isEmptyString())));
  }

  private List<FinalCustomerProperty> buildCustomerProperties(final Long orgId) {
    List<FinalCustomerProperty> customerProperties = new ArrayList<>();
    FinalCustomerProperty companyNameProperty = FinalCustomerProperty.builder().orgId(orgId)
        .settingKey(SettingsKeys.FinalCustomer.Settings.CUSTOMER_NUMBER.name())
        .value("customer number test").build();
    FinalCustomerProperty emailProperty = FinalCustomerProperty.builder().orgId(orgId)
        .settingKey(SettingsKeys.FinalCustomer.Settings.EMAIL.name()).value("email@test.vn")
        .build();
    customerProperties.add(companyNameProperty);
    customerProperties.add(emailProperty);
    return customerProperties;
  }

  @Test
  public void shouldReturnEmptyPageWithInvalidCriteria() {
    Assert.assertThat(
        finalCustomerService.searchFinalCustomersBelongToCustomer(null, null, null).hasContent(),
        Matchers.is(false));

    Assert.assertThat(finalCustomerService
        .searchFinalCustomersBelongToCustomer(CUSTOMER_ORG_ID, null, null).hasContent(),
        Matchers.is(false));

    final FinalCustomerSearchCriteria criteria = new FinalCustomerSearchCriteria();
    Assert
        .assertThat(
            finalCustomerService
                .searchFinalCustomersBelongToCustomer(CUSTOMER_ORG_ID, criteria, null).hasContent(),
            Matchers.is(false));

    final FinalCustomerSearchTermCriteria term = new FinalCustomerSearchTermCriteria();
    criteria.setTerm(term);
    Assert
        .assertThat(
            finalCustomerService
                .searchFinalCustomersBelongToCustomer(CUSTOMER_ORG_ID, criteria, null).hasContent(),
            Matchers.is(false));

    final FinalCustomerSearchSortCriteria sort = new FinalCustomerSearchSortCriteria();
    criteria.setSort(sort);
    Assert
        .assertThat(
            finalCustomerService
                .searchFinalCustomersBelongToCustomer(CUSTOMER_ORG_ID, criteria, null).hasContent(),
            Matchers.is(false));
  }

  @Test
  public void shouldReturnResultPageWithValidCriteria() {
    final FinalCustomerSearchTermCriteria term = new FinalCustomerSearchTermCriteria();
    term.setName("final");
    term.setFinalCustomerType(FinalCustomerType.ONLINE);
    term.setAddress("address_1");
    term.setContactInfo("firstname");

    final FinalCustomerSearchSortCriteria sort = new FinalCustomerSearchSortCriteria();
    sort.setOrderDescByName(true);

    final FinalCustomerSearchCriteria criteria = new FinalCustomerSearchCriteria();
    criteria.setTerm(term);
    criteria.setSort(sort);

    Mockito.when(vFinalCustomerRepo.findAll(Mockito.any(), Mockito.eq(PageUtils.DEF_PAGE)))
        .thenReturn(new PageImpl<>(Arrays.asList(new VFinalCustomer())));

    final Page<FinalCustomerDto> finalCustomers = finalCustomerService
        .searchFinalCustomersBelongToCustomer(CUSTOMER_ORG_ID, criteria, PageUtils.DEF_PAGE);

    Assert.assertThat(finalCustomers.hasContent(), Matchers.is(true));

    Mockito.verify(vFinalCustomerRepo, Mockito.times(1)).findAll(Mockito.any(),
        Mockito.eq(PageUtils.DEF_PAGE));
  }

  @Test
  public void shouldReturnFinalCustomerInfo() {
    final int finalCustOrgId = 139;

    Mockito.when(vFinalCustomerRepo.findByOrgId(Mockito.eq(finalCustOrgId)))
    .thenReturn(Optional.of(new VFinalCustomer()));

    FinalCustomerProperty property = new FinalCustomerProperty();
    property.setSettingKey(StringUtils.EMPTY);
    property.setValue(StringUtils.EMPTY);

    Mockito.when(finalCustomerPropertyRepo.findByOrgId(Mockito.eq(Long.valueOf(finalCustOrgId))))
        .thenReturn(Arrays.asList(property));

    final Optional<FinalCustomerDto> finalCustomerOpt =
        finalCustomerService.getFinalCustomerInfo(finalCustOrgId, true);

    Assert.assertThat(finalCustomerOpt.isPresent(), Matchers.is(true));

    Mockito.verify(vFinalCustomerRepo, Mockito.times(1))
        .findByOrgId(Mockito.eq(finalCustOrgId));

    Mockito.verify(finalCustomerPropertyRepo, Mockito.times(1))
        .findByOrgId(Mockito.eq(Long.valueOf(finalCustOrgId)));
  }

  @Test
  public void shouldReturnFinalCustomerInfoWithSortMode() {
    final int finalCustOrgId = 139;

    Mockito.when(vFinalCustomerRepo.findByOrgId(Mockito.eq(finalCustOrgId)))
    .thenReturn(Optional.of(new VFinalCustomer()));

    FinalCustomerProperty property = new FinalCustomerProperty();
    property.setSettingKey(StringUtils.EMPTY);
    property.setValue(StringUtils.EMPTY);

    final Optional<FinalCustomerDto> finalCustomerOpt =
        finalCustomerService.getFinalCustomerInfo(finalCustOrgId, false);

    Assert.assertThat(finalCustomerOpt.isPresent(), Matchers.is(true));

    Mockito.verify(vFinalCustomerRepo, Mockito.times(1))
        .findByOrgId(Mockito.eq(finalCustOrgId));

    Mockito.verify(finalCustomerPropertyRepo, Mockito.times(0))
        .findByOrgId(Mockito.eq(Long.valueOf(finalCustOrgId)));
  }

  @Test
  public void shouldReturnFinalCustomerInfoWithNullInputs() {
    final int finalCustOrgId = 139;

    Assert.assertThat(
        finalCustomerService.getFinalCustomerInfo(finalCustOrgId, false).isPresent(),
        Matchers.is(false));


    Assert.assertThat(
        finalCustomerService.getFinalCustomerInfo(null, false).isPresent(),
        Matchers.is(false));
  }

}
