package com.sagag.services.service.order.ordercondition;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.api.InvoiceTypeRepository;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.EmployeeExternalService;
import com.sagag.services.ax.enums.AxPaymentType;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.service.DataProvider;
import com.sagag.services.service.api.UserBusinessService;
import com.sagag.services.service.utils.ContextDataTestUtils;

import lombok.Getter;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class DefaultOrderConditionInitializerTest {

  @InjectMocks
  private DefaultOrderConditionInitializer initializer;

  @Mock
  private EmployeeExternalService employeeExtService;

  @Mock
  private UserBusinessService userBusinessService;

  @Mock
  private BranchRepository branchRepo;

  @Mock
  private InvoiceTypeRepository invoiceTypeRepo;

  @Mock
  private AddressFilterService addressFilterService;

  @Mock
  @Getter
  private UserInfo user;

  @Test
  public void testInitOrderConditionsForSalesTOURandSelbstzahlWithKSL() {
    user = DataProvider.initSaleOnbehalfUser();
    final Customer customer = user.getCustomer();
    customer.setDefaultBranchId("1001");
    customer.setAxSendMethod(SendMethodType.TOUR.name());
    customer.setAxPaymentType(AxPaymentType.SELBSTZAHL.getCode());

    commonMockForInitOrderCondition(ContextDataTestUtils.getUserSettings());
    when(employeeExtService.getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(PaymentMethodType.CREDIT);

    final Branch branch = new Branch();
    branch.setValidForKSL(true);
    when(branchRepo.findOneByBranchNr(Mockito.any()))
        .thenReturn(Optional.ofNullable(branch));

    final EshopBasketContext initialOrderConditions = initializer.initialize(user);

    Assert.assertNotNull(initialOrderConditions);
    Mockito.verify(employeeExtService, Mockito.times(1))
        .getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean());
    assertSendMethodAndPaymentMethod(initialOrderConditions, SendMethodType.TOUR,
        PaymentMethodType.CREDIT);
  }

  @Test
  public void testInitOrderConditionsForCustomer() {
    user = DataProvider.initCustomerUser();

    final UserSettingsDto userSettings = ContextDataTestUtils.getUserSettings();
    userSettings.setPaymentId(2);
    userSettings.setDeliveryId(2);

    commonMockForInitOrderCondition(userSettings);

    final EshopBasketContext initialOrderConditions = initializer.initialize(user);

    Assert.assertNotNull(initialOrderConditions);
    Mockito.verify(employeeExtService, Mockito.times(0))
        .getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean());
    assertSendMethodAndPaymentMethod(initialOrderConditions, SendMethodType.TOUR,
        PaymentMethodType.CASH);
  }

  @Test
  public void testInitOrderConditionsForSalesTOURandBarWithoutKSL() {
    user = DataProvider.initSaleOnbehalfUser();
    final Customer customer = user.getCustomer();
    customer.setAxSendMethod(SendMethodType.TOUR.name());
    customer.setAxPaymentType(AxPaymentType.BAR.getCode());

    commonMockForInitOrderCondition(ContextDataTestUtils.getUserSettings());
    when(employeeExtService.getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(PaymentMethodType.CASH);

    final EshopBasketContext initialOrderConditions = initializer.initialize(user);

    Assert.assertNotNull(initialOrderConditions);
    Mockito.verify(employeeExtService, Mockito.times(1))
        .getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean());
    assertSendMethodAndPaymentMethod(initialOrderConditions, SendMethodType.PICKUP,
        PaymentMethodType.CASH);
  }

  @Test
  public void testInitOrderConditionsForSalesTOURandSofortWithoutKSL() {
    user = DataProvider.initSaleOnbehalfUser();
    final Customer customer = user.getCustomer();
    customer.setAxSendMethod(SendMethodType.TOUR.name());
    customer.setAxPaymentType(AxPaymentType.SOFORT.getCode());

    commonMockForInitOrderCondition(ContextDataTestUtils.getUserSettings());
    when(employeeExtService.getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(PaymentMethodType.DIRECT_INVOICE);

    final EshopBasketContext initialOrderConditions = initializer.initialize(user);

    Assert.assertNotNull(initialOrderConditions);
    Mockito.verify(employeeExtService, Mockito.times(1))
        .getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean());
    assertSendMethodAndPaymentMethod(initialOrderConditions, SendMethodType.TOUR,
        PaymentMethodType.DIRECT_INVOICE);
  }

  @Test
  public void testInitOrderConditionsForSalesPICKUPandFinWithoutKSL() {
    user = DataProvider.initSaleOnbehalfUser();
    final Customer customer = user.getCustomer();
    customer.setAxSendMethod(SendMethodType.PICKUP.name());
    customer.setAxPaymentType(AxPaymentType.FIN.getCode());

    final UserSettingsDto userSettings = ContextDataTestUtils.getUserSettings();
    userSettings.setDeliveryId(1);
    commonMockForInitOrderCondition(userSettings);
    when(employeeExtService.getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(PaymentMethodType.CREDIT);

    final EshopBasketContext initialOrderConditions = initializer.initialize(user);

    Assert.assertNotNull(initialOrderConditions);
    Mockito.verify(employeeExtService, Mockito.times(1))
        .getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean());
    assertSendMethodAndPaymentMethod(initialOrderConditions, SendMethodType.PICKUP,
        PaymentMethodType.CREDIT);
  }

  @Test
  public void testInitOrderConditionsForSalesPICKUPandBarWithoutKSL() {
    user = DataProvider.initSaleOnbehalfUser();
    final Customer customer = user.getCustomer();
    customer.setAxSendMethod(SendMethodType.PICKUP.name());
    customer.setAxPaymentType(AxPaymentType.BAR.getCode());

    final UserSettingsDto userSettings = ContextDataTestUtils.getUserSettings();
    userSettings.setDeliveryId(1);
    commonMockForInitOrderCondition(userSettings);
    when(employeeExtService.getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(PaymentMethodType.DIRECT_INVOICE);

    final EshopBasketContext initialOrderConditions = initializer.initialize(user);

    Assert.assertNotNull(initialOrderConditions);
    Mockito.verify(employeeExtService, Mockito.times(1))
        .getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean());
    assertSendMethodAndPaymentMethod(initialOrderConditions, SendMethodType.PICKUP,
        PaymentMethodType.DIRECT_INVOICE);
  }

  @Test
  public void testInitOrderConditionsForSalesTOURandBarWithKSL() {
    user = DataProvider.initSaleOnbehalfUser();
    final Customer customer = user.getCustomer();
    customer.setDefaultBranchId("1001");
    customer.setAxSendMethod(SendMethodType.TOUR.name());
    customer.setAxPaymentType(AxPaymentType.BAR.getCode());

    commonMockForInitOrderCondition(ContextDataTestUtils.getUserSettings());
    when(employeeExtService.getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(PaymentMethodType.DIRECT_INVOICE);

    final Branch branch = new Branch();
    branch.setValidForKSL(true);
    when(branchRepo.findOneByBranchNr(Mockito.any()))
        .thenReturn(Optional.ofNullable(branch));

    final EshopBasketContext initialOrderConditions = initializer.initialize(user);

    Assert.assertNotNull(initialOrderConditions);
    Mockito.verify(employeeExtService, Mockito.times(1))
        .getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean());
    assertSendMethodAndPaymentMethod(initialOrderConditions, SendMethodType.PICKUP,
        PaymentMethodType.DIRECT_INVOICE);
  }

  @Test
  public void testInitOrderConditionsForCustomerAdminInvoiceType() {
    user = DataProvider.initCustomerUser();

    final UserSettingsDto userSettings = ContextDataTestUtils.getUserSettings();
    commonMockForInitOrderCondition(userSettings);
    when(employeeExtService.getConnectPaymentForSales(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(PaymentMethodType.CASH);

    final EshopBasketContext initialOrderConditions = initializer.initialize(user);

    Assert.assertNotNull(initialOrderConditions);
    assertThat(initialOrderConditions.getInvoiceType().getId(),
        Matchers.is(userSettings.getInvoiceId()));
  }

  private void assertSendMethodAndPaymentMethod(final EshopBasketContext initialOrderConditions,
      final SendMethodType sendMethod, final PaymentMethodType paymentMethod) {
    assertThat(initialOrderConditions.getDeliveryType().getDescCode(),
        Matchers.is(sendMethod.name()));
    assertThat(initialOrderConditions.getPaymentMethod().getDescCode(),
        Matchers.is(paymentMethod.name()));
  }

  private void commonMockForInitOrderCondition(final UserSettingsDto userSettings) {
    when(userBusinessService.getUserPaymentSetting(Mockito.any()))
        .thenReturn(ContextDataTestUtils.getPaymentSettings());
    when(userBusinessService.getUserSettings(Mockito.any())).thenReturn(userSettings);
    final InvoiceType singleInvoice = new InvoiceType();
    singleInvoice.setId(1);
    when(invoiceTypeRepo.findOneByInvoiceTypeCode(Mockito.any()))
        .thenReturn(Optional.ofNullable(singleInvoice));
  }
}
