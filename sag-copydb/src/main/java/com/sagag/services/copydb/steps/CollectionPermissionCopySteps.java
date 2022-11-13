package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CollectionPermission;
import com.sagag.services.copydb.domain.dest.DestCollectionPermission;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CollectionPermissionCopySteps extends AbstractJobConfig {

  @Autowired
  private CollectionPermissionProcessor collectionPermissionProcessor;

  @Autowired
  private DestCollectionPermissionWriter collectionPermissionWriter;

  @Bean(name = "copyCollectionPermission")
  public Step copyCollectionPermission() {
    return stepBuilderFactory.get("copyCollectionPermission").<CollectionPermission, DestCollectionPermission>chunk(DF_CHUNK)
        .reader(collectionPermissionReader())
        .processor(collectionPermissionProcessor)
        .writer(collectionPermissionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<CollectionPermission> collectionPermissionReader() {
    final JpaPagingItemReader<CollectionPermission> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from CollectionPermission e");
    reader.setName("collectionPermissionReader");
    return reader;
  }

}
