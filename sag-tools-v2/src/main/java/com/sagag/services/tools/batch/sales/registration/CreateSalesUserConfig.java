package com.sagag.services.tools.batch.sales.registration;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.domain.target.EshopUser;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManagerFactory;

@Configuration
public class CreateSalesUserConfig extends AbstractJobConfig {

  @Value("${sales.index.from:1}")
  private int from;

  @Value("${sales.index.to:5000}")
  private int to;

  @Value("${sales.create.sso:false}")
  private boolean isCreateSSO;

  @Autowired
  private CreateSalesUserItemProcessor processor;

  @Autowired
  private CreateSalesUserItemWriter writer;

  @Autowired
  private CreateSaleUserSSOWriter ssoWriter;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Override
  protected String jobName() {
    return "createTestSalesUser";
  }

  @Bean
  public Job createSalesUser(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .start(initialSalesUserCreationStep())
        .build();
  }

  @Bean
  public CompositeItemWriter<EshopUser> createSaleUserCompositeItemWriter() {
    List<ItemWriter<? super EshopUser>> itemWriters =
        isCreateSSO ? Arrays.asList(writer, ssoWriter) : Arrays.asList(writer);
    return new CompositeItemWriterBuilder<EshopUser>()
        .delegates(itemWriters)
        .build();
  }

  @Bean
  public Step initialSalesUserCreationStep() {
    return stepBuilderFactory.get("initialSalesUserCreationStep")
        .<Integer, EshopUser>chunk(DF_CHUNK)
        .reader(saleUserIndex())
        .processor(processor)
        .writer(createSaleUserCompositeItemWriter())
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean(destroyMethod = "")
  @StepScope
  public ListItemReader<Integer> saleUserIndex() {
    return new ListItemReader<>(IntStream.rangeClosed(from, to)
        .boxed()
        .collect(Collectors.toList()));
  }

}
