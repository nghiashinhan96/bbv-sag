package com.sagag.services.incentive.api.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.config.IncentiveProperties;
import com.sagag.services.incentive.config.TrainingConfiguration;
import com.sagag.services.incentive.domain.TrainingLoginDto;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TraningSingleSignOnServiceImplTest {

  @InjectMocks
  private TrainingSingleSignOnServiceImpl chTrainingSingleSignOnService;

  @Mock
  private IncentiveProperties incentiveProperties;

  @Test
  public void getAuthenInfo_shouldReturnAuthenInfo() throws Exception {
    TrainingConfiguration traningConfiguration = new TrainingConfiguration();
    traningConfiguration.setCompanyPassword("7BbyfJH9S8q6T8GP@#$");
    traningConfiguration.setHashEncrypt("SHA-1");
    Mockito.when(incentiveProperties.getTraining()).thenReturn(traningConfiguration);
    TrainingLoginDto authenInfo = chTrainingSingleSignOnService
        .getAuthenInfo(SupportedAffiliate.DERENDINGER_CH, 1000l, "357461", "Danh", "Nguyen");
    assertNotNull(authenInfo);
    assertThat(authenInfo.getCompanyId(), Matchers.is(1000l));
    assertThat(authenInfo.getUserId(), Matchers.is("357461"));
    assertThat(authenInfo.getFirstName(), Matchers.is("Danh"));
    assertThat(authenInfo.getLastName(), Matchers.is("Nguyen"));
    assertThat(authenInfo.getFs(), Matchers.is("7dbebc2a4da3bbb51978bf1f4f3866aef4f56412"));
    System.out.println(authenInfo);
  }
}
