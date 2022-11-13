package com.sagag.services.gtmotive.app;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.gtmotive.domain.GtmotiveProfileDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { GtmotiveApplication.class })
@EshopIntegrationTest
public class GtmotiveAccountsConfigurationIT {

  @Autowired
  private GtInterfaceAccountsConfiguration iframeAccounts;
  @Autowired
  private GtmotiveVehicleAccountConfiguration vehAccount;

  @Test
  public void verifyGraphicalAccountsConfiguration() {

    Assert.assertThat(iframeAccounts.getUrl(), Matchers.is(
        "https://estimate.mygtmotive.com/webservice/GtInterfaceWS.asmx"));
    Assert.assertThat(iframeAccounts.isMultilang(), Matchers.is(true));
    Assert.assertThat(iframeAccounts.getAccounts(), Matchers.notNullValue());

    // DE
    GtmotiveProfileDto de = iframeAccounts.getAccounts().getDe();
    Assert.assertThat(de.getClientid(), Matchers.is("SAG_AT"));
    Assert.assertThat(de.getUserid(), Matchers.is("ctlgpro"));
    Assert.assertThat(de.getPassword(), Matchers.is("FC3306DA3A"));
    Assert.assertThat(de.getGsId(), Matchers.is("SGS029700N"));

    // FR
    GtmotiveProfileDto fr = iframeAccounts.getAccounts().getFr();
    Assert.assertThat(fr.getClientid(), Matchers.is("SAG_AT"));
    Assert.assertThat(fr.getUserid(), Matchers.is("ctlgpro"));
    Assert.assertThat(fr.getPassword(), Matchers.is("FC3306DA3A"));
    Assert.assertThat(fr.getGsId(), Matchers.is("SGS029700N"));

    // IT
    GtmotiveProfileDto it = iframeAccounts.getAccounts().getIt();
    Assert.assertThat(it.getClientid(), Matchers.is("SAG_AT"));
    Assert.assertThat(it.getUserid(), Matchers.is("ctlgpro"));
    Assert.assertThat(it.getPassword(), Matchers.is("FC3306DA3A"));
    Assert.assertThat(it.getGsId(), Matchers.is("SGS029700N"));
  }

  @Test
  public void verifyVehicleAccountConfiguration() {
    Assert.assertThat(vehAccount.getUrl(), Matchers.is(
        "http://wsgti.gtestimate.com/WSGtVehicleInformationService.svc"));
    Assert.assertThat(vehAccount.getGsId(), Matchers.is("SGS029700N"));
    Assert.assertThat(vehAccount.getPassword(), Matchers.is("FC3306DA3A"));
    Assert.assertThat(vehAccount.getCustomerId(), Matchers.is("SAG_AT"));
    Assert.assertThat(vehAccount.getUserId(), Matchers.is("wsuserdev"));
  }

}
