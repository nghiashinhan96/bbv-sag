package com.sagag.services.service.dto.feedback;

import static org.junit.Assert.assertThat;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class FeedbackItemBuilderTest {

  @Test
  public void buildHtml_ShouldBuildFullContentWithNestedItems_GivenDataItemHaveChilds()
      throws Exception {

    FeedbackDataItem invoiceTypeSetting = FeedbackDataItem.builder().key("INVOICE_TYPE")
        .title("Invoice Type").value("Invoice type 1").build();
    FeedbackDataItem deliveryAddressSetting = FeedbackDataItem.builder().key("DELIVERY_ADDRESS")
        .title("Delivery address").value("Anna building").build();
    FeedbackDataItem settingItem =
        FeedbackDataItem.builder().childs(Arrays.asList(invoiceTypeSetting, deliveryAddressSetting))
            .key("CUSTOMER_SETTING").title("Customer settings").build();

    //@formatter:off
    String expectedResult =
        "<div>"
            + "<div>Customer settings: </div>"
            + "<ul style='margin:0' >"
                + "<li style='mso-special-format:bullet'><div><span>Invoice Type: </span><span>Invoice type 1<span></div></li>"
                + "<li style='mso-special-format:bullet'><div><span>Delivery address: </span><span>Anna building<span></div></li>"
            + "</ul>"
      + "</div>";
    //@formatter:on
    assertThat(FeedbackItemBuilder.buildHtml(settingItem), Matchers.is(expectedResult));
  }

  @Test
  public void buildHtml_ShouldBuildContentWithoutNestedItems_GivenDataItemHaveNoChilds()
      throws Exception {
    FeedbackDataItem invoiceTypeSetting = FeedbackDataItem.builder().key("INVOICE_TYPE")
        .title("Invoice Type").value("Invoice type 1").build();
    String expectedResult = "<div><span>Invoice Type: </span><span>Invoice type 1<span></div>";
    assertThat(FeedbackItemBuilder.buildHtml(invoiceTypeSetting), Matchers.is(expectedResult));
  }
}
