package com.sagag.eshop.service.finalcustomer.register;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.eshop.repo.entity.PriceDisplayType;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractNewFinalCustomerTest {

  protected NewFinalCustomerDto buildNewFinalCustomerDto() {
    PermissionConfigurationDto bulb = PermissionConfigurationDto.builder().permission("BULB")
        .langKey("BULB").permissionId(24).enable(true).editable(true).build();
    PermissionConfigurationDto oil = PermissionConfigurationDto.builder().permission("OIL")
        .langKey("OIL").permissionId(25).enable(false).editable(true).build();
    List<PermissionConfigurationDto> perms = Arrays.asList(bulb, oil);

    NewFinalCustomerDto finalCustomer = new NewFinalCustomerDto();
    finalCustomer.setCustomerOrgId(100);
    finalCustomer.setCollectionShortname("wbb");
    finalCustomer.setCustomerType("ONLINE");
    finalCustomer.setIsActive(true);
    finalCustomer.setShowNetPrice(true);
    finalCustomer.setCustomerNr("100");
    finalCustomer.setCustomerName("final customer 1");
    finalCustomer.setSalutation("Sir");
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

  protected CustomerSettings buildCustomerSetting() {
    InvoiceType invoiceType = new InvoiceType();
    invoiceType.setId(3);
    invoiceType.setInvoiceTypeCode("MONTHLY_INVOICE_WITH_CREDIT_SEPARATION");
    invoiceType.setInvoiceTypeName("MONATSFAGT");
    invoiceType.setInvoiceTypeDesc("Monatsfaktura mit Gutschriftstrennung");

    PaymentMethod paymentMethod = new PaymentMethod();
    paymentMethod.setId(1);
    paymentMethod.setDescCode("CREDIT");
    paymentMethod.setPayMethod("Rechnung");
    paymentMethod.setDescription("Rechnung");
    paymentMethod.setOrderDisplay(1);

    return CustomerSettings.builder().id(152).allocationId(1).deliveryId(1).collectiveDelivery(1)
        .netPriceView(true).netPriceConfirm(true).allowNetPriceChanged(true)
        .viewBilling(false).addressId(false).billingAddressId(null).deliveryAddressId(null)
        .useDefaultSetting(false).emailNotificationOrder(false).sessionTimeoutSeconds(3600)
        .invoiceType(invoiceType).paymentMethod(paymentMethod).showOciVat(false)
        .priceDisplayId(1)
        .build();
  }
  protected Optional<PriceDisplayType> buildPriceType() {
    PriceDisplayType price = new PriceDisplayType();
    price.setId(1);
    price.setType(PriceDisplayTypeEnum.UVPE_OEP_GROSS.name());
    return Optional.of(price);
  }

  protected Organisation buildOrganisation() {
    Organisation organisation = Organisation.builder().description("final customer 1").parentId(137)
        .shortname("customer - 100").build();
    return organisation;
  }
}
