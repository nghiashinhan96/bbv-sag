package com.sagag.services.stakis.erp.translator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.wsdl.cis.DispatchTypeList;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@RunWith(SpringRunner.class)
public class CisDeliveryInfoTranslatorTest {

  @InjectMocks
  private CisDeliveryInfoTranslator translator;

  private OutCustomer outCustomer;

  private DispatchTypeList dispatchTypList;

  @Before
  public void setup() throws IOException, JAXBException {
    this.outCustomer = DataProvider
        .getFullGetCustomerResponseBody().getValue().getGetCustomerResult().getValue();

    this.dispatchTypList = XmlUtils.getValueOpt(
        this.outCustomer.getCustomer().getValue().getDispatchTypeList()).orElse(null);
  }

	@Test
	public void testTranslateData() {
		Map<SendMethodType, List<DeliveryTypeDto>> deliveryTypeBySendMethod = translator
				.translateToConnect(dispatchTypList);
		SendMethodType sendMethodType = deliveryTypeBySendMethod.entrySet().stream().map(Entry::getKey).findFirst()
				.get();
		List<DeliveryTypeDto> deliveryTypes = deliveryTypeBySendMethod.entrySet().stream().map(Entry::getValue)
				.findFirst().get();
		Assert.assertThat(sendMethodType, Matchers.is(SendMethodType.PICKUP));
		Assert.assertThat(deliveryTypes.size(), Matchers.is(1));
		Assert.assertThat(deliveryTypes.get(0).getDescCode(), Matchers.is(SendMethodType.PICKUP.name()));

	}
}
