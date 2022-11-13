package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CouponUseLog;
import com.sagag.services.copydb.domain.dest.DestCouponUseLog;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CouponUseLogCopySteps extends AbstractJobConfig {

  @Autowired
  private CouponUseLogProcessor couponUseLogProcessor;

  @Autowired
  private DestCouponUseLogWriter couponUseLogWriter;

  @Bean(name = "copyCouponUseLog")
  public Step copyCouponUseLog() {
    return stepBuilderFactory.get("copyCouponUseLog").<CouponUseLog, DestCouponUseLog>chunk(DF_CHUNK)
        .reader(couponUseLogReader())
        .processor(couponUseLogProcessor)
        .writer(couponUseLogWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<CouponUseLog> couponUseLogReader() {
    final JpaPagingItemReader<CouponUseLog> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from CouponUseLog e");
    reader.setName("couponUseLogReader");
    return reader;
  }

}
