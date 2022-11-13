package com.sagag.services.haynespro;

import com.sagag.services.common.enums.HaynesProLicenseTypeEnum;
import com.sagag.services.haynespro.dto.HaynesProCustomer;
import com.sagag.services.haynespro.dto.HaynesProPart;
import com.sagag.services.haynespro.dto.HaynesProParts;
import com.sagag.services.haynespro.dto.HaynesProShoppingCart;
import com.sagag.services.haynespro.dto.HaynesProVehicle;
import com.sagag.services.haynespro.request.HaynesProAccessUrlRequest;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.UUID;

@UtilityClass
public class HaynesProDataProvider {

  private static final String SHOP_CART_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<shoppingCart><customer><username>thinguyen@bbv.ch#SAG Swiss Automotive Group AG@SAG Swiss Automotive Group AG</username>"
      + "<loginId></loginId><customerId></customerId><productId>20</productId></customer>"
      + "<vehicle><name>VOLKSWAGEN Golf VI (5K, AJ) 1.2 TSi</name><id>t_102001202</id><ktypnr>32625</ktypnr><motnrs>23520</motnrs></vehicle>"
      + "<jobs><job><name>Zeit-/fahrstreckenabhängige Wartung, (2011 - 2013)</name><id>ms_300000909</id><type>MAINTENANCE_SYSTEM</type></job>"
      + "<job><name>30 000 km/24 Monate</name><id>mp_300011060</id><type>PERIOD</type><awNumber></awNumber><oeCode>01 36 00 24</oeCode>"
      + "<time>0.9</time><labourRate>1.0</labourRate></job></jobs><parts><part><name>Motoröl</name><genartNumber>3224</genartNumber>"
      + "<mandatoryPart>true</mandatoryPart><quantity>1.0</quantity></part><part><name>Ölfilter</name><genartNumber>7</genartNumber>"
      + "<mandatoryPart>true</mandatoryPart><quantity>1.0</quantity></part></parts></shoppingCart>\r\n";

  public HaynesProAccessUrlRequest getHaynesProAccessRequest(String lang) {
    final HaynesProAccessUrlRequest request = new HaynesProAccessUrlRequest();
    request.setUuid(UUID.randomUUID().toString());
    request.setUsername("test");
    request.setLanguage(lang);
    request.setSubject("maintenance");
    request.setKType("58944");
    request.setMotorId("27193");
    request.setHourlyRate("3.5");
    request.setVatRate("7.7");
    request.setLicenseType(HaynesProLicenseTypeEnum.ULTIMATE);
    request.setCallbackBtnText("Export back to SAG e-Store");
    request.setCallbackUrl("https://pre.d-store.ch");
    return request;
  }

  public HaynesProShoppingCart getHaynesProShoppingCart() {
    final HaynesProCustomer customer = new HaynesProCustomer();
    customer.setCustomerId("customer id");

    final HaynesProParts parts = new HaynesProParts();
    parts.setPart(new HaynesProPart[] {new HaynesProPart()});

    HaynesProVehicle vehicle = new HaynesProVehicle();
    vehicle.setKtypnr("ktype");

    final HaynesProShoppingCart cart = new HaynesProShoppingCart();
    cart.setCustomer(customer);
    cart.setParts(parts);
    cart.setVehicle(vehicle);
    return cart;
  }

  public BufferedReader parseXmlToBufferedReader() {
    StringReader reader = new StringReader(SHOP_CART_XML);
    return new BufferedReader(reader);
  }

}
