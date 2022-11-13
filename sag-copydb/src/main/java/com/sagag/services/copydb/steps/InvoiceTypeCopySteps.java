package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.InvoiceType;
import com.sagag.services.copydb.domain.dest.DestInvoiceType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class InvoiceTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private InvoiceTypeProcessor invoiceTypeProcessor;

  @Autowired
  private DestInvoiceTypeWriter invoiceTypeWriter;

  @Bean(name = "copyInvoiceType")
  public Step copyInvoiceType() {
    return stepBuilderFactory.get("copyInvoiceType").<InvoiceType, DestInvoiceType>chunk(DF_CHUNK)
        .reader(invoiceTypeReader())
        .processor(invoiceTypeProcessor)
        .writer(invoiceTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<InvoiceType> invoiceTypeReader() {
    final JpaPagingItemReader<InvoiceType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from InvoiceType e");
    reader.setName("invoiceTypeReader");
    return reader;
  }

}
