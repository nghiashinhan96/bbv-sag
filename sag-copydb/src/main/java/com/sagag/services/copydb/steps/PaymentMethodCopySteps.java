package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.PaymentMethod;
import com.sagag.services.copydb.domain.dest.DestPaymentMethod;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class PaymentMethodCopySteps extends AbstractJobConfig {

  @Autowired
  private PaymentMethodProcessor paymentMethodProcessor;

  @Autowired
  private DestPaymentMethodWriter paymentMethodWriter;

  @Bean(name = "copyPaymentMethod")
  public Step copyPaymentMethod() {
    return stepBuilderFactory.get("copyPaymentMethod").<PaymentMethod, DestPaymentMethod>chunk(DF_CHUNK)
        .reader(paymentMethodReader())
        .processor(paymentMethodProcessor)
        .writer(paymentMethodWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<PaymentMethod> paymentMethodReader() {
    final JpaPagingItemReader<PaymentMethod> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from PaymentMethod e");
    reader.setName("paymentMethodReader");
    return reader;
  }

}
