package com.sagag.services.service.order.ordercondition;

import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.api.InvoiceTypeRepository;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.BranchExternalService;
import com.sagag.services.article.api.EmployeeExternalService;
import com.sagag.services.ax.enums.AxPaymentType;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.enums.ErpInvoiceTypeCode;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.domain.eshop.dto.InvoiceTypeDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.hazelcast.api.TourTimeCacheService;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.service.api.UserBusinessService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

@Slf4j
public class AbstractOrderConditionInitializer implements OrderConditionInitializer {

  @Autowired
  private EmployeeExternalService employeeExtService;

  @Autowired
  private UserBusinessService userBusinessService;

  @Autowired
  private BranchRepository branchRepo;

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepo;

  @Autowired
  private AddressFilterService addressFilterService;

  @Autowired
  private TourTimeCacheService tourTimeCacheService;

  @Autowired
  private BranchExternalService branchExtService;

  @Override
  public EshopBasketContext initialize(UserInfo user) {
    log.debug("Initialize the order conditions for user");

    final PaymentSettingDto paymentSetting = userBusinessService.getUserPaymentSetting(user);
    Assert.notNull(paymentSetting, "The payment settings must not be null");
    final UserSettingsDto userSettings = userBusinessService.getUserSettings(user);
    Assert.notNull(userSettings, "The user settings must not be null");

    final EshopBasketContext orderConditions = new EshopBasketContext();

    // Valid to show ksl
    orderConditions.setShowKSLMode(checkShowKSLMode(user.getDefaultBranchId()));

    // Allocation type
    paymentSetting.getAllocationTypes().stream()
        .filter(type -> type.getId() == userSettings.getAllocationId()).findFirst()
        .ifPresent(orderConditions::setAllocation);

    // Invoice type
    final Optional<InvoiceTypeDto> invoiceType = paymentSetting.getInvoiceTypes().stream()
        .filter(type -> type.getId() == userSettings.getInvoiceId()).findFirst();
    if (invoiceType.isPresent()) {
      orderConditions.setInvoiceType(invoiceType.get());
    } else {
      final Optional<InvoiceType> singleInvoiceType =
          invoiceTypeRepo.findOneByInvoiceTypeCode(ErpInvoiceTypeCode.SINGLE_INVOICE.name());
      singleInvoiceType.ifPresent(singleType -> paymentSetting.getInvoiceTypes().stream()
          .filter(type -> type.getId() > singleType.getId()
              && userSettings.getInvoiceId() > singleType.getId())
          .findFirst().ifPresent(orderConditions::setInvoiceType));
    }

    // Delivery type
    paymentSetting.getDeliveryTypes().stream()
        .filter(type -> type.getId() == userSettings.getDeliveryId()).findFirst()
        .ifPresent(orderConditions::setDeliveryType);

    // Payment method
    if (user.isSaleOnBehalf()) {
      final PaymentMethodType connectPaymentForSales = employeeExtService.getConnectPaymentForSales(
          user.getCustomer().getAxPaymentType(), orderConditions.getShowKSLMode());
      paymentSetting.getPaymentMethods().stream()
          .filter(type -> connectPaymentForSales.name().equals(type.getDescCode())).findFirst()
          .ifPresent(orderConditions::setPaymentMethod);
      syncDeliveryTypeWithAx(user.getCustomer(), paymentSetting, orderConditions);
    } else {
      paymentSetting.getPaymentMethods().stream()
          .filter(method -> method.getId() == userSettings.getPaymentId()).findFirst()
          .ifPresent(orderConditions::setPaymentMethod);
    }

    // Collection delivery
    paymentSetting.getCollectiveTypes().stream()
        .filter(type -> type.getId() == userSettings.getCollectiveDelivery()).findFirst()
        .ifPresent(orderConditions::setCollectionDelivery);

    // Delivery and billing address
    initializeAddresses(orderConditions, userSettings, paymentSetting.getAddresses());

    // Pickup branch
    orderConditions.setPickupBranch(CustomerBranch.builder().branchId(user.getDefaultBranchId())
        .branchName(user.getDefaultBranchName()).build());

    // Order type
    orderConditions.setOrderType(OrderType.ORDER);

    // All branches
    final List<CustomerBranch> branches = new ArrayList<>();
    if (!user.isSalesNotOnBehalf()) {
      branches.addAll(branchExtService.getBranches(user.getCompanyName()));
    }

    // Tour time
    final List<TourTimeDto> tourTimes = new ArrayList<>();
    if (user.hasCust()) {
      tourTimes.addAll(tourTimeCacheService.searchTourTimesByCustNr(user.getCustNr()));
    }
    tourTimes.forEach(tt -> branches.forEach(br -> {
      if (br.getBranchId().equals(tt.getBranchId())) {
        tt.setBranchName(br.getBranchName());
      }
    }));
    orderConditions.setTourTimes(tourTimes);

    return orderConditions;
  }

  private Boolean checkShowKSLMode(final String branchId) {
    if (StringUtils.isBlank(branchId)) {
      return false;
    }
    return branchRepo.findOneByBranchNr(Integer.valueOf(branchId))
        .map(Branch::getValidForKSL).orElse(false);
  }

  private void initializeAddresses(final EshopBasketContext orderConditions,
      final UserSettingsDto userSettings, final List<Address> addresses) {
    if (CollectionUtils.isEmpty(addresses)) {
      return;
    }
    addressFilterService.filterAddress(addresses, userSettings.getDeliveryAddressId(),
        ErpAddressType.DELIVERY).ifPresent(orderConditions::setDeliveryAddress);
    addressFilterService.filterAddress(addresses, userSettings.getBillingAddressId(),
        ErpAddressType.INVOICE).ifPresent(orderConditions::setBillingAddress);
  }

  private void syncDeliveryTypeWithAx(final Customer customer,
      final PaymentSettingDto paymentSetting, final EshopBasketContext orderConditions) {
    if (!SendMethodType.TOUR.name().equals(customer.getAxSendMethod())
        || !AxPaymentType.BAR.getCode().equals(customer.getAxPaymentType())) {
      return;
    }
    // #3328: AX send method is "TOUR" and AX payment type is "Bar", then the Connect is set
    // to PICKUP and "Barzahlung".
    paymentSetting.getDeliveryTypes().stream()
        .filter(type -> SendMethodType.PICKUP.name().equals(type.getDescCode())).findFirst()
        .ifPresent(orderConditions::setDeliveryType);
  }

}
