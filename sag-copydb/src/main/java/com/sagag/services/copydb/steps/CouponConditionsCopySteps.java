package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CouponConditions;
import com.sagag.services.copydb.domain.dest.DestCouponConditions;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CouponConditionsCopySteps extends AbstractJobConfig {

  @Autowired
  private CouponConditionsProcessor couponConditionsProcessor;

  @Autowired
  private DestCouponConditionsWriter couponConditionsWriter;

  @Bean(name = "copyCouponConditions")
  public Step copyCouponConditions() {
    return stepBuilderFactory.get("copyCouponConditions").<CouponConditions, DestCouponConditions>chunk(DF_CHUNK)
        .reader(couponConditionsReader())
        .processor(couponConditionsProcessor)
        .writer(couponConditionsWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<CouponConditions> couponConditionsReader() {
    final JpaPagingItemReader<CouponConditions> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from CouponConditions e");
    reader.setName("couponConditionsReader");
    return reader;
  }

}
