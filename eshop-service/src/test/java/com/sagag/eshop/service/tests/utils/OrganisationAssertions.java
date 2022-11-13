package com.sagag.eshop.service.tests.utils;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopAddress;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationAddress;
import com.sagag.eshop.repo.entity.OrganisationType;

import lombok.experimental.UtilityClass;

import org.hamcrest.Matchers;

import java.util.List;

@UtilityClass
public class OrganisationAssertions {

  public static void assertFoundOrganisation(final Organisation foundOrg) {
    assertThat(foundOrg.getDescription(), Matchers.is(TestsConstants.ORG_DESC));
    assertThat(foundOrg.getName(), Matchers.is(TestsConstants.ORG_NAME));
    assertThat(foundOrg.getOrgCode(), Matchers.is(TestsConstants.ORG_CODE));
    assertThat(foundOrg.getShortname(), Matchers.is(TestsConstants.ORG_SHORTNAME));
    assertThat(foundOrg.getId(), Matchers.is(TestsConstants.ORG_ID));
    assertThat(foundOrg.getParentId(), Matchers.is(TestsConstants.DDAT_ORG_ID));

    final List<CouponUseLog> couponUseLogs = foundOrg.getCouponUseLog();
    assertThat(couponUseLogs, Matchers.hasSize(1));
    assertCouponUseLog(couponUseLogs.get(0));

    assertCustomerSettings(foundOrg.getCustomerSettings());

    final List<OrganisationAddress> organisationAddresses = foundOrg.getOrganisationAddresses();
    assertThat(organisationAddresses, Matchers.hasSize(1));
    assertOrganisationAddress(organisationAddresses.get(0));


    assertOrganisationType(foundOrg.getOrganisationType());

  }

  private static void assertOrganisationType(OrganisationType organisationType) {
    assertThat(organisationType.getId(), Matchers.is(TestsConstants.ORG_TYPE));
    assertThat(organisationType.getLevel(), Matchers.is(2));
    assertThat(organisationType.getName(), Matchers.is(TestsConstants.ORG_TYPE_NAME));
  }

  private static void assertOrganisationAddress(OrganisationAddress organisationAddress) {
    assertThat(organisationAddress.getId(), Matchers.is(1));
    assertEshopAddress(organisationAddress.getAddress());
  }

  private static void assertEshopAddress(EshopAddress address) {
    assertThat(address.getId(), Matchers.is(1));
    assertThat(address.getCountryiso(), Matchers.is(TestsConstants.COUNTRY_ISO_CODE_AT));
    assertThat(address.getZipcode(), Matchers.is(TestsConstants.ZIP_CODE));
    assertThat(address.getCity(), Matchers.isEmptyString());
  }

  private static void assertCustomerSettings(CustomerSettings customerSettings) {
    assertThat(customerSettings.getId(), Matchers.is(TestsConstants.CUST_SETTINGS_ID));
    assertThat(customerSettings.getBillingAddressId(), Matchers.is(TestsConstants.BILLING_ADDR_ID));
    assertThat(customerSettings.getAllocationId(), Matchers.is(1));
    assertThat(customerSettings.getDeliveryId(), Matchers.is(2));
    assertThat(customerSettings.getSessionTimeoutSeconds(),
        Matchers.is(TestsConstants.TIMEOUT_1HOUR));
    assertThat(customerSettings.getDeliveryAddressId(),
        Matchers.is(TestsConstants.BILLING_ADDR_ID));
    CommonAssertions.assertPaymentMethod(customerSettings.getPaymentMethod());
    assertThat(customerSettings.getCollectiveDelivery(), Matchers.is(2));

    CommonAssertions.assertInvoiceType(customerSettings.getInvoiceType());
  }

  private static void assertCouponUseLog(CouponUseLog couponUseLog) {
    assertThat(couponUseLog.getId(), Matchers.is(1L));
    assertThat(couponUseLog.getUserId(), Matchers.is(TestsConstants.USER_ID));
    assertThat(couponUseLog.getCustomerNr(), Matchers.is(TestsConstants.ORG_CODE));
    assertThat(couponUseLog.getUmarId(), Matchers.is(TestsConstants.ART_ID));
    assertThat(couponUseLog.getDiscountArticleId(), Matchers.is(TestsConstants.ART_ID));
    assertThat(couponUseLog.getArticleIdMatch(), Matchers.is("1000000014461823757"));
    assertThat(couponUseLog.getCouponsCode(), Matchers.is(TestsConstants.COUPON_CODE));
    CommonAssertions.assertDate(couponUseLog.getDateUsed());
    assertThat(couponUseLog.getOrderID(), Matchers.is(7L));
    assertThat(couponUseLog.getAffiliateMatch(), Matchers.is("DERENDINGER-AT"));
    assertThat(couponUseLog.getAmoutApplied(), Matchers.is(25.0));
    assertThat(couponUseLog.getArticleCategories(), Matchers.is("82,854"));
    assertThat(couponUseLog.getBrandsMatch(), Matchers.is("30,2,48"));
    assertThat(couponUseLog.getCountryMatch(), Matchers.is("CH"));
    assertThat(couponUseLog.getOrderConfirmationId(), Matchers.is(5L));
    assertThat(couponUseLog.getUsageCountRemainder(), Matchers.is(10));
  }

}
