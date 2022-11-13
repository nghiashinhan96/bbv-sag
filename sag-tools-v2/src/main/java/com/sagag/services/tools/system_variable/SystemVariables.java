package com.sagag.services.tools.system_variable;

import com.sagag.services.tools.utils.QueryUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sag")
@Data
@Slf4j
public class SystemVariables implements InitializingBean {

  @Value("#{'${sag.migration.customer.numbers:}'.split(',')}")
  private List<String> custNrs;

  @Value("${sag.migration.csv.path:}")
  private String csvPath;

  @Value("${sag.migration.affiliate:}")
  private String affiliate;

  @Value("${sag.migration.master:false}")
  private boolean migrateMasterData;

  @Value("#{'${sag.batch.permission.affiliate_shortname:}'}")
  private String affiliateShortName;

  @Value("${sag.diff.customer:}")
  private String diffCustomerNr;

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("Building the customer list from {}", this);
    List<String> custNrs = QueryUtils.getDistinctTrimedValues(getCustNrs());
    if (!CollectionUtils.isEmpty(custNrs)) {
      log.debug("Reading customer list from input = {}", custNrs);
      setCustNrs(custNrs);
      return;
    }
  }
}
