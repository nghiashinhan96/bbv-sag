package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CollectionRelation;
import com.sagag.services.copydb.domain.dest.DestCollectionRelation;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CollectionRelationCopySteps extends AbstractJobConfig {

  @Autowired
  private CollectionRelationProcessor collectionRelationProcessor;

  @Autowired
  private DestCollectionRelationWriter collectionRelationWriter;

  @Bean(name = "copyCollectionRelation")
  public Step copyCollectionRelation() {
    return stepBuilderFactory.get("copyCollectionRelation").<CollectionRelation, DestCollectionRelation>chunk(DF_CHUNK)
        .reader(collectionRelationReader())
        .processor(collectionRelationProcessor)
        .writer(collectionRelationWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<CollectionRelation> collectionRelationReader() {
    final JpaPagingItemReader<CollectionRelation> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from CollectionRelation e");
    reader.setName("collectionRelationReader");
    return reader;
  }

}
