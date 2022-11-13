package com.sagag.services.service.returnorder;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;
import com.sagag.services.service.SagServiceApplication;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class ReturnTransactionReferencesFilterIT {

  @Autowired
  private ReturnTransactionReferencesFilter filter;

  @Test
  @Ignore
  public void test() {
    List<TransactionReferenceDto> references = filter.filterTransactionReferences(
        SupportedAffiliate.DERENDINGER_CH, "AU07100021015", StringUtils.EMPTY);

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(references));
  }
}
