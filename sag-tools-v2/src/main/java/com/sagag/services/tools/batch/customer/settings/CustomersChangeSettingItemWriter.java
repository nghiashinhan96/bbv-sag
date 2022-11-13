package com.sagag.services.tools.batch.customer.settings;

import com.sagag.services.tools.domain.target.CustomerSettings;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.item.database.JpaItemWriter;

@Slf4j
public class CustomersChangeSettingItemWriter extends JpaItemWriter<CustomerSettings> {

  @Override
  public void write(List<? extends CustomerSettings> custSettingsList) {
    if (CollectionUtils.isEmpty(custSettingsList)) {
      log.warn("The customer setting list is empty to update into database.");
      return;
    }
    super.write(customerSettingProcessor().apply(custSettingsList));
  }

  private UnaryOperator<List<? extends CustomerSettings>> customerSettingProcessor() {
    return customerSettings -> customerSettings.stream().filter(Objects::nonNull)
      .collect(Collectors.toList());
  }
}
