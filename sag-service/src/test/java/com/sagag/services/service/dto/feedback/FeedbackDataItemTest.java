package com.sagag.services.service.dto.feedback;

import static org.junit.Assert.assertThat;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class FeedbackDataItemTest {

  @Test
  public void getContent_shouldGetContent_givenItemHaveNoChilds() throws Exception {
    FeedbackDataItem websiteItem =
        FeedbackDataItem.builder().key("WEBSITE").title("Website").value("Home page").build();
    System.out.println(websiteItem.getContent());
    //@formatter:off
    String expectedResult =
        "<div>"
            + "<span>Website: </span>"
            + "<span>Home page<span>"
      + "</div>";
    //@formatter:on
    assertThat(websiteItem.getContent(), Matchers.is(expectedResult));
  }

  @Test
  public void getContent_shouldGetContent_givenItemHaveChilds() throws Exception {
    FeedbackDataItem invoiceTypeSetting = FeedbackDataItem.builder().key("INVOICE_TYPE")
        .title("Invoice Type").value("Invoice type 1").build();
    FeedbackDataItem deliveryAddressSetting = FeedbackDataItem.builder().key("DELIVERY_ADDRESS")
        .title("Delivery address").value("Anna building").build();
    FeedbackDataItem settingItem =
        FeedbackDataItem.builder().childs(Arrays.asList(invoiceTypeSetting, deliveryAddressSetting))
            .key("CUSTOMER_SETTING").title("Customer settings").build();

    System.out.println(settingItem.getContent());
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
    assertThat(settingItem.getContent(), Matchers.is(expectedResult));
  }
}
