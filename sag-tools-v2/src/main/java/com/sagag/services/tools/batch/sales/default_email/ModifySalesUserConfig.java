package com.sagag.services.tools.batch.sales.default_email;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.domain.target.EshopUser;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManagerFactory;

@Configuration
public class ModifySalesUserConfig extends AbstractJobConfig {

  @Value("${sales.index.from:1}")
  private int from;

  @Value("${sales.index.to:5000}")
  private int to;

  @Value("${sales.finding.mode:email}")
  private String mode;

  @Autowired
  private ModifySalesUserItemProcessor processor;

  @Autowired
  private ModifySalesUserItemWriter writer;

  @Autowired
  private ModifySalesUserItemByUserNameProcessor userNameProcessor;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Override
  protected String jobName() {
    return "modifySaleUsers";
  }

  @Bean
  public Job modifySalesUser(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .start(initialSalesUserModificationStep())
        .build();
  }

  @Bean
  public Step initialSalesUserModificationStep() {
    return stepBuilderFactory.get("initialSalesUserModificationStep")
        .<Integer, Optional<EshopUser>>chunk(DF_CHUNK)
        .reader(saleUserIndex())
        .processor(getItemProcessor())
        .writer(writer)
        .transactionManager(targetTransactionManager)
        .build();
  }

  private ItemProcessor<Integer, Optional<EshopUser>> getItemProcessor(){
    if(mode.equals("email")) {
      return processor;
    }
    if(mode.equals("username")) {
      return userNameProcessor;
    }
    throw new IllegalArgumentException("Not supported");
  }

  @Bean(destroyMethod = "")
  @StepScope
  public ListItemReader<Integer> saleUserIndex() {
    return new ListItemReader<>(IntStream.rangeClosed(from, to)
        .boxed()
        .collect(Collectors.toList()));
  }

}
