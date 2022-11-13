package com.sagag.services.service.utils;

import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.domain.eshop.dto.AllocationTypeDto;
import com.sagag.services.domain.eshop.dto.CollectiveDeliveryDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.InvoiceTypeDto;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.domain.sag.erp.Address;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class ContextDataTestUtils {

  public PaymentSettingDto getPaymentSettings() {
    final PaymentSettingDto paymentSetting = new PaymentSettingDto();

    // Addresses
    final Address addresses =
        Address.builder().id("2000000011720265606").street("Bahnhofstrasse 91").active(true)
            .addressId("2000000011720265606").addressType(ErpAddressType.DEFAULT.name())
            .addressTypeCode(ErpAddressType.DEFAULT.name()).city("Kalsdorf bei Graz")
            .country("Österreich").countryCode("AUT").postCode("8401").build();
    paymentSetting.setAddresses(Collections.singletonList(addresses));

    final AllocationTypeDto allocationType1 = AllocationTypeDto.builder().id(1).allowChoose(true)
        .descCode("ALLOCATION_TYPE1").description("Verrechnung gemäss Vereinbarung")
        .type("Verrechnung gemäss Vereinbarung").build();
    final AllocationTypeDto allocationType2 = AllocationTypeDto.builder().id(2).allowChoose(true)
        .descCode("ALLOCATION_TYPE2").description("Verrechnung als Einzelfaktura")
        .type("Verrechnung als Einzelfaktura").build();
    paymentSetting.setAllocationTypes(
        Stream.of(allocationType1, allocationType2).collect(Collectors.toList()));

    // Collective delivery
    final CollectiveDeliveryDto collectionDelivery1 = CollectiveDeliveryDto.builder().id(1)
        .allowChoose(true).descCode("COLLECTIVE_DELIVERY1")
        .description(
            "Ich benötige die verfügbare Ware dringend. Bitte senden Sie mir die verfügbaren Artikel schnellst möglich.")
        .type(
            "Ich benötige die verfügbare Ware dringend. Bitte senden Sie mir die verfügbaren Artikel schnellst möglich.")
        .build();
    final CollectiveDeliveryDto collectionDelivery2 = CollectiveDeliveryDto.builder().id(2)
        .allowChoose(true).descCode("COLLECTIVE_DELIVERY2")
        .description(
            "Die Ware wird nicht dringend benötigt. Bitte senden Sie mir die Ware in einer Lieferung, sobald sie verfügbar ist.")
        .type(
            "Die Ware wird nicht dringend benötigt. Bitte senden Sie mir die Ware in einer Lieferung, sobald sie verfügbar ist.")
        .build();
    paymentSetting.setCollectiveTypes(
        Stream.of(collectionDelivery1, collectionDelivery2).collect(Collectors.toList()));

    // Delivery type
    final DeliveryTypeDto pickupDeliveryType =
        DeliveryTypeDto.builder().id(1).allowChoose(true).descCode("PICKUP")
            .description("Abholung durch den Kunden").type("Abholung in Filiale").build();
    final DeliveryTypeDto tourDeliveryType = DeliveryTypeDto.builder().id(2).allowChoose(true)
        .descCode("TOUR").description("Kundenbelieferung durch Gebietsleiter")
        .type("Lieferung gemäss Tourenplan").build();
    paymentSetting.setDeliveryTypes(
        Stream.of(pickupDeliveryType, tourDeliveryType).collect(Collectors.toList()));

    // Invoice type
    final InvoiceTypeDto twoWeekWithCredit = InvoiceTypeDto.builder().id(1).allowChoose(true)
        .descCode("TWO_WEEKLY_INVOICE_WITH_CREDIT_SEPARATION").invoiceType("2WFAKTGT")
        .invoiceTypeDesc("2 Wochen Faktura mit Gutschriftstrennung").build();
    final InvoiceTypeDto singleWithCredit = InvoiceTypeDto.builder().id(2).allowChoose(true)
        .descCode("SINGLE_INVOICE_WITH_CREDIT_SEPARATION").invoiceType("EINZELFAGT")
        .invoiceTypeDesc("Fakturierung pro Auftrag mit Gutschriftstennung").build();
    final InvoiceTypeDto monthlyWithCredit = InvoiceTypeDto.builder().id(3).allowChoose(true)
        .descCode("MONTHLY_INVOICE_WITH_CREDIT_SEPARATION").invoiceType("MONATSFAGT")
        .invoiceTypeDesc("Monatsfaktura mit Gutschriftstrennung").build();
    final InvoiceTypeDto dailyWithCredit = InvoiceTypeDto.builder().id(4).allowChoose(true)
        .descCode("DAILY_INVOICE_WITH_CREDIT_SEPARATION").invoiceType("TAGESFAGT")
        .invoiceTypeDesc("Tagesfaktura mit Gutschriftstrennung").build();
    final InvoiceTypeDto weeklyWithCredit = InvoiceTypeDto.builder().id(5).allowChoose(true)
        .descCode("WEEKLY_INVOICE_WITH_CREDIT_SEPARATION").invoiceType("WOCHENFAGT")
        .invoiceTypeDesc("Wochenfaktura mit Gutschriftstrennung").build();
    final InvoiceTypeDto single =
        InvoiceTypeDto.builder().id(6).allowChoose(true).descCode("SINGLE_INVOICE")
            .invoiceType("EINZELFAKT").invoiceTypeDesc("Fakturierung pro Auftrag").build();
    final InvoiceTypeDto twoWeek = InvoiceTypeDto.builder().id(7).allowChoose(true)
        .descCode("TWO_WEEKLY_INVOICE").invoiceType("2WFAKTGT")
        .invoiceTypeDesc("2 Wochen Faktura mit Gutschriftstrennung").build();
    final InvoiceTypeDto weekly =
        InvoiceTypeDto.builder().id(8).allowChoose(true).descCode("WEEKLY_INVOICE")
            .invoiceType("WOCHENFAGT").invoiceTypeDesc("Wochenfaktura").build();
    final InvoiceTypeDto daily = InvoiceTypeDto.builder().id(3).allowChoose(true)
        .descCode("DAILY_INVOICE").invoiceType("TAGESFAGT").invoiceTypeDesc("Tagesfaktura").build();
    final InvoiceTypeDto monthly =
        InvoiceTypeDto.builder().id(9).allowChoose(true).descCode("MONTHLY_INVOICE")
            .invoiceType("MONATSFAGT").invoiceTypeDesc("Monatsfaktura").build();
    final InvoiceTypeDto monthlyInvoiceWeekly = InvoiceTypeDto.builder().id(10).allowChoose(true)
        .descCode("MONTHLY_INVOICE_WEEKLY_CREDIT_SEPARATION").invoiceType("MONATSFAGT")
        .invoiceTypeDesc(StringUtils.EMPTY).build();
    paymentSetting.setInvoiceTypes(Stream
        .of(twoWeekWithCredit, singleWithCredit, monthlyWithCredit, dailyWithCredit,
            weeklyWithCredit, single, twoWeek, weekly, daily, monthly, monthlyInvoiceWeekly)
        .collect(Collectors.toList()));

    // Payment method
    final PaymentMethodDto credit = PaymentMethodDto.builder().id(1).allowChoose(true)
        .descCode("CREDIT").description("Rechnung").payMethod("Rechnung").build();
    final PaymentMethodDto cash = PaymentMethodDto.builder().id(2).allowChoose(true)
        .descCode("CASH").description("Barzahlung").payMethod("Barzahlung").build();
    final PaymentMethodDto directInvoice =
        PaymentMethodDto.builder().id(3).allowChoose(true).descCode("DIRECT_INVOICE")
            .description("Sofortrechnung").payMethod("Sofortrechnung").build();
    final PaymentMethodDto card = PaymentMethodDto.builder().id(4).allowChoose(true)
        .descCode("CARD").description("Kartenzahlung").payMethod("Kartenzahlung").build();
    paymentSetting.setPaymentMethods(
        Stream.of(credit, cash, directInvoice, card).collect(Collectors.toList()));

    return paymentSetting;
  }

  public UserSettingsDto getUserSettings() {
    return UserSettingsDto.builder().id(10663).allocationId(1)
        .billingAddressId("2000000011720265606").classicCategoryView(false).singleSelectMode(false)
        .collectiveDelivery(1)
        .deliveryAddressId("2000000011720265606").deliveryId(2).emailNotificationOrder(false)
        .invoiceId(8).netPriceConfirm(true).netPriceView(true).paymentId(1).showDiscount(false)
        .viewBilling(true).build();
  }
}
