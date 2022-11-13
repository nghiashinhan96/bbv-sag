package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.AffiliatePermission;
import com.sagag.services.copydb.domain.dest.DestAffiliatePermission;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class AffiliatePermissionCopySteps extends AbstractJobConfig {

  @Autowired
  private AffiliatePermissionProcessor affiliatePermissionProcessor;

  @Autowired
  private DestAffiliatePermissionWriter affiliatePermissionWriter;

  @Bean(name = "copyAffiliatePermission")
  public Step copyAffiliatePermission() {
    return stepBuilderFactory.get("copyAffiliatePermission").<AffiliatePermission, DestAffiliatePermission>chunk(DF_CHUNK)
        .reader(affiliatePermissionReader())
        .processor(affiliatePermissionProcessor)
        .writer(affiliatePermissionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<AffiliatePermission> affiliatePermissionReader() {
    final JpaPagingItemReader<AffiliatePermission> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from AffiliatePermission e");
    reader.setName("affiliatePermissionReader");
    return reader;
  }

}
