package com.sagag.services.tools.batch.affiliate.permission;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvPermissionExcludedCustomer;
import com.sagag.services.tools.domain.target.EshopGroup;
import com.sagag.services.tools.domain.target.GroupPermission;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Configuration
@OracleProfile
public class PermissionItemJobConfig extends AbstractJobConfig implements InitializingBean {

  @Value("#{'${sag.batch.permission.affiliate_shortname:derendinger-at}'}")
  private String affiliateShortName;

  @Autowired
  private PermissionItemProcessor processor;

  @Autowired
  private PermissionItemWritter writer;

  @Autowired
  private OrganisationRepository targetOrganisationRepository;

  @Autowired
  private CollectionPermissionTasklet collectionPermissionTasklet;

  private Integer affiliateId;

  @Override
  public void afterPropertiesSet() throws Exception {
    affiliateId =
        targetOrganisationRepository.findIdByShortName(affiliateShortName).orElseThrow(() -> new IllegalArgumentException("Affiliate does not exist"));
  }

  @Override
  protected String jobName() {
    return "SetAffiliatePermission";
  }

  @Bean
  @Transactional
  public Job setAffiliatePermissionJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName()).incrementer(new RunIdIncrementer()).listener(listener).start(toStep(collectionPermissionTasklet))
        .next(setPermissionStep()).build();
  }

  //@formatter:off
  @Bean
  public Step setPermissionStep() throws Exception {
    return stepBuilderFactory.get("setPermissionStep").<EshopGroup, GroupPermission>chunk(DF_CHUNK)
        .reader(groupReader())
        .processor(processor)
        .writer(writer)
        .transactionManager(targetTransactionManager).build();
  }
  //@formatter:on

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopGroup> groupReader() throws Exception {
    final JpaPagingItemReader<EshopGroup> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getTargetEntityManagerFactory());
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new PermissionItemQueryProvider(affiliateId, getExcludedCustomerNrs()));
    reader.setName("groupReader");
    return reader;
  }

  public List<String> getExcludedCustomerNrs() {
    List<String> custNrs = new ArrayList<>();
    File file = new File(SystemUtils.getUserDir() + "/csv/excluded-customer-permission.csv");
    try {
      List<CsvPermissionExcludedCustomer> custNrsFromFile = CsvUtils.read(file, CsvPermissionExcludedCustomer.class);
      custNrs = CollectionUtils.emptyIfNull(custNrsFromFile).stream().map(CsvPermissionExcludedCustomer::getCustomerNr).map(Object::toString)
          .collect(Collectors.toList());
    } catch (BatchJobException e) {
      return Collections.emptyList();
    }
    return custNrs;
  }
}
