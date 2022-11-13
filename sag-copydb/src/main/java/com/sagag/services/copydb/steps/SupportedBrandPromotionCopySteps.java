package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.SupportedBrandPromotion;
import com.sagag.services.copydb.domain.dest.DestSupportedBrandPromotion;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class SupportedBrandPromotionCopySteps extends AbstractJobConfig {

  @Autowired
  private SupportedBrandPromotionProcessor supportedBrandPromotionProcessor;

  @Autowired
  private DestSupportedBrandPromotionWriter supportedBrandPromotionWriter;

  @Bean(name = "copySupportedBrandPromotion")
  public Step copySupportedBrandPromotion() {
    return stepBuilderFactory.get("copySupportedBrandPromotion").<SupportedBrandPromotion, DestSupportedBrandPromotion>chunk(DF_CHUNK)
        .reader(supportedBrandPromotionReader())
        .processor(supportedBrandPromotionProcessor)
        .writer(supportedBrandPromotionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<SupportedBrandPromotion> supportedBrandPromotionReader() {
    final JpaPagingItemReader<SupportedBrandPromotion> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from SupportedBrandPromotion e");
    reader.setName("supportedBrandPromotionReader");
    return reader;
  }

}
