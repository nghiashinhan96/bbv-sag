package com.sagag.services.ax.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.api.WtFinancialCardExternalService;
import com.sagag.services.common.annotation.SbEshopIntegrationTest;
import com.sagag.services.domain.sag.financialcard.FinancialCardAmountDto;
import com.sagag.services.domain.sag.financialcard.FinancialCardDetailDto;
import com.sagag.services.domain.sag.financialcard.FinancialCardDocDto;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@SbEshopIntegrationTest
@Ignore("Cannot reach to WINT ERP IP at Bamboo server")
public class WtFinancialCardExternalServiceIT {

  @Autowired
  private WtFinancialCardExternalService wtFinancialCardExternalService;

  @Test
  public void testGetFinancialCardHistory() {
    Page<FinancialCardDocDto> data = wtFinancialCardExternalService.getFinancialCardHistory(
        AxDataTestUtils.companyNameOfWtSB(), AxDataTestUtils.CUSTOMER_NR_WINT_SB_200014,
        AxDataTestUtils.financialCardHistoryRequest());

    Assert.assertThat(data.getTotalElements(), Matchers.greaterThan(0L));
    List<FinancialCardDocDto> unmatchData = data.getContent().stream()
        .filter(doc -> !StringUtils.equals(doc.getStatus(), "Posted")
            || !StringUtils.equals(doc.getPaymentMethod(), "Wholesale")
            || !StringUtils.equals(doc.getCustomerNr(), AxDataTestUtils.CUSTOMER_NR_WINT_SB_200014))
        .collect(Collectors.toList());
    Assert.assertEquals(unmatchData.size(), 0);
  }


  @Test
  public void testGetFinancialCardAmount() {
    Optional<FinancialCardAmountDto> data = wtFinancialCardExternalService.getFinancialCardAmount(
            AxDataTestUtils.companyNameOfWtSB(), AxDataTestUtils.CUSTOMER_NR_WINT_SB_200014,
            AxDataTestUtils.PAYMENT_METHOD_WHOLESALE);

    Assert.assertThat(data.isPresent(), Matchers.is(true));
    if (data.isPresent()) {
      Assert.assertThat(data.get().getInProcessAmount(), Matchers.greaterThanOrEqualTo(0D));
      Assert.assertThat(data.get().getPostedBalance(), Matchers.greaterThanOrEqualTo(0D));
    }
  }

  @Test
  public void testGetFinancialCardDetail() {
    Optional<FinancialCardDetailDto> financialCardDetailOpt =
        wtFinancialCardExternalService.getFinancialCardDetail(AxDataTestUtils.companyNameOfWtSB(),
            AxDataTestUtils.CUSTOMER_NR_WINT_SB_200014, AxDataTestUtils.FINANCIAL_CARD_DOCUMENT_NR,
            AxDataTestUtils.financialCardDetailRequest());

    Assert.assertTrue(financialCardDetailOpt.isPresent());
  }
}
