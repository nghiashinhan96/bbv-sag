package com.sagag.eshop.service.finalcustomer;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.FinalCustomerPropertyRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.SavingFinalCustomerDto;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.common.EshopUserCreateAuthority;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UpdatingFinalCustomerProcessorTest {


  private static final String ORG_NAME = "final customer 1";

  @InjectMocks
  private UpdatingFinalCustomerProcessor updatingFinalCustomerProcessor;

  @Mock
  private OrganisationRepository organisationRepo;

  @Mock
  private CustomerSettingsRepository customerSettingsRepo;

  @Mock
  private FinalCustomerPropertyRepository finalCustomerPropertyRepo;

  @Mock
  private PermissionService permissionService;

  @Mock
  private EshopGroupRepository eshopGroupRepo;

  @Mock
  private FinalCustomerUserLoginStatusProcessor finalCustomerUserLoginStatusProcessor;

  @Test
  public void process_shouldUpdateOrganisation_giveFinalCustomer() throws Exception {
    Organisation organisation = Organisation.builder().name("original name")
        .description("original name").shortname("original shortname").build();
    when(organisationRepo.findOneById(anyInt())).thenReturn(Optional.of(organisation));
    mockUpdateCustomerSettings();
    mockUpdateCustomerProperty();
    mockUpdateGroup();
    updatingFinalCustomerProcessor.process(1, buildFinalCustomerDto());
    assertThat(organisation.getName(), Matchers.is(ORG_NAME));
    assertThat(organisation.getDescription(), Matchers.is(ORG_NAME));
    assertThat(organisation.getShortname(), Matchers.is("customer - 100"));
  }

  @Test
  public void process_shouldUpdateCustomerSettings_givenFinalCustomerModel() throws Exception {
    mockUpdateOrganisation();
    CustomerSettings settings = new CustomerSettings();
    settings.setNetPriceView(false);
    when(customerSettingsRepo.findSettingsByOrgId(anyInt())).thenReturn(settings);
    mockUpdateCustomerProperty();
    mockUpdateGroup();
    updatingFinalCustomerProcessor.process(1, buildFinalCustomerDto());
    assertTrue(settings.isNetPriceView());
  }

  @Test
  public void process_shouldUpdateCustomerProperties_givenFinalCustomerModel() throws Exception {
    mockUpdateOrganisation();
    mockUpdateCustomerSettings();

    List<FinalCustomerProperty> properties = buildProperties();
    when(finalCustomerPropertyRepo.findByOrgId(anyLong())).thenReturn(properties);
    mockUpdateGroup();
    updatingFinalCustomerProcessor.process(1, buildFinalCustomerDto());

    assertThat(properties.get(0).getValue(), Matchers.is("ONLINE"));
    assertThat(properties.get(1).getValue(), Matchers.is("ACTIVE"));
    assertThat(properties.get(2).getValue(), Matchers.is("100"));
    assertThat(properties.get(3).getValue(), Matchers.is("GENERAL_SALUTATION_MALE"));
    assertThat(properties.get(4).getValue(), Matchers.is("Nguyen"));
    assertThat(properties.get(5).getValue(), Matchers.is("Danh"));
    assertThat(properties.get(6).getValue(), Matchers.is("Quang Trung"));
    assertThat(properties.get(7).getValue(), Matchers.is("261 Le Duc Tho"));
    assertThat(properties.get(8).getValue(), Matchers.is("262 Le Duc Tho"));
    assertThat(properties.get(9).getValue(), Matchers.is("P1"));
    assertThat(properties.get(10).getValue(), Matchers.is("70001"));
    assertThat(properties.get(11).getValue(), Matchers.is("70000"));
    assertThat(properties.get(12).getValue(), Matchers.is("0344389123"));
    assertThat(properties.get(13).getValue(), Matchers.is("323 555 1234"));
    assertThat(properties.get(14).getValue(), Matchers.is("danh_online_company@bbv.ch"));
  }

  @Test
  public void process_shouldUpdateGroup_givenFinalCustomer() throws Exception {
    mockUpdateOrganisation();
    mockUpdateCustomerSettings();
    mockUpdateCustomerProperty();

    EshopGroup adminGroup = EshopGroup.builder().name("FINAL_CUSTOMER_000_USER_ADMIN")
        .description("User admin group of final customer 000").build();
    EshopGroup normalGroup = EshopGroup.builder().name("FINAL_CUSTOMER_000_NORMAL_USER")
        .description("Normal user group of final customer 000").build();
    when(eshopGroupRepo.findByOrgIdAndRoleName(1, EshopUserCreateAuthority.FINAL_USER_ADMIN.name()))
        .thenReturn(Optional.of(adminGroup));
    when(
        eshopGroupRepo.findByOrgIdAndRoleName(1, EshopUserCreateAuthority.FINAL_NORMAL_USER.name()))
            .thenReturn(Optional.of(normalGroup));

    updatingFinalCustomerProcessor.process(1, buildFinalCustomerDto());

    assertThat(adminGroup.getName(), Matchers.is("FINAL_CUSTOMER_100_USER_ADMIN"));
    assertThat(adminGroup.getDescription(), Matchers.is("User admin group of " + ORG_NAME));
    assertThat(normalGroup.getName(), Matchers.is("FINAL_CUSTOMER_100_NORMAL_USER"));
    assertThat(normalGroup.getDescription(), Matchers.is("Normal user group of " + ORG_NAME));
  }

  private SavingFinalCustomerDto buildFinalCustomerDto() {
    PermissionConfigurationDto bulb = PermissionConfigurationDto.builder().permission("BULB")
        .langKey("BULB").permissionId(24).enable(true).editable(true).build();
    PermissionConfigurationDto oil = PermissionConfigurationDto.builder().permission("OIL")
        .langKey("OIL").permissionId(25).enable(false).editable(true).build();
    List<PermissionConfigurationDto> perms = Arrays.asList(bulb, oil);

    SavingFinalCustomerDto finalCustomer = new NewFinalCustomerDto();
    finalCustomer.setCustomerType("ONLINE");
    finalCustomer.setIsActive(true);
    finalCustomer.setShowNetPrice(true);
    finalCustomer.setCustomerNr("100");
    finalCustomer.setCustomerName(ORG_NAME);
    finalCustomer.setSalutation("GENERAL_SALUTATION_MALE");
    finalCustomer.setSurName("Nguyen");
    finalCustomer.setFirstName("Danh");
    finalCustomer.setStreet("Quang Trung");
    finalCustomer.setAddressOne("261 Le Duc Tho");
    finalCustomer.setAddressTwo("262 Le Duc Tho");
    finalCustomer.setPoBox("P1");
    finalCustomer.setPostcode("70001");
    finalCustomer.setPlace("70000");
    finalCustomer.setPhone("0344389123");
    finalCustomer.setFax("323 555 1234");
    finalCustomer.setEmail("danh_online_company@bbv.ch");
    finalCustomer.setPerms(perms);
    finalCustomer.setDeliveryId(1);

    return finalCustomer;
  }

  private List<FinalCustomerProperty> buildProperties() {

    FinalCustomerProperty type = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.TYPE.name()).value("PASSANT").build();

    FinalCustomerProperty status = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.STATUS.name()).value("INACTIVE").build();

    FinalCustomerProperty customerNr = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.CUSTOMER_NUMBER.name()).value("99").build();

    FinalCustomerProperty salutation = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.SALUTATION.name())
        .value("GENERAL_SALUTATION_FEMALE").build();

    FinalCustomerProperty surname = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.SURNAME.name()).value("surname").build();

    FinalCustomerProperty firstName = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.FIRSTNAME.name()).value("firstName")
        .build();

    FinalCustomerProperty street = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.STREET.name()).value("street").build();

    FinalCustomerProperty address1 = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.ADDRESS_1.name()).value("address1").build();

    FinalCustomerProperty address2 = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.ADDRESS_2.name()).value("address2").build();

    FinalCustomerProperty poBox = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.PO_BOX.name()).value("P2").build();

    FinalCustomerProperty postCode = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.POSTCODE.name()).value("10000").build();

    FinalCustomerProperty place = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.PLACE.name()).value("100001").build();

    FinalCustomerProperty phone = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.PHONE.name()).value("42342423434").build();

    FinalCustomerProperty fax = FinalCustomerProperty.builder()
        .settingKey(SettingsKeys.FinalCustomer.Settings.FAX.name()).value("1212 2121 212").build();

    FinalCustomerProperty email =
        FinalCustomerProperty.builder().settingKey(SettingsKeys.FinalCustomer.Settings.EMAIL.name())
            .value("test_009_008@bbv.ch").build();

    List<FinalCustomerProperty> properties = new ArrayList<>();
    properties.add(type);
    properties.add(status);
    properties.add(customerNr);
    properties.add(salutation);
    properties.add(surname);
    properties.add(firstName);
    properties.add(street);
    properties.add(address1);
    properties.add(address2);
    properties.add(poBox);
    properties.add(postCode);
    properties.add(place);
    properties.add(phone);
    properties.add(fax);
    properties.add(email);

    return properties;
  }

  private void mockUpdateOrganisation() {
    when(organisationRepo.findOneById(anyInt())).thenReturn(Optional.of(new Organisation()));
  }

  private void mockUpdateCustomerSettings() {
    when(customerSettingsRepo.findSettingsByOrgId(anyInt())).thenReturn(new CustomerSettings());
  }

  private void mockUpdateCustomerProperty() {
    when(finalCustomerPropertyRepo.findByOrgId(anyLong())).thenReturn(Collections.emptyList());
  }

  private void mockUpdateGroup() {
    when(eshopGroupRepo.findByOrgIdAndRoleName(anyInt(), anyString()))
        .thenReturn(Optional.of(new EshopGroup()));
  }
}
