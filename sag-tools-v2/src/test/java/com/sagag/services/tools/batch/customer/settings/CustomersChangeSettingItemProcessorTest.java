package com.sagag.services.tools.batch.customer.settings;

import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.utils.JsonUtils;
import java.lang.reflect.Method;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CustomersChangeSettingItemProcessorTest {

  private String settingColumn;

  private String settingValue;

  @Test
  public void testProcessShowDiscountSetting() throws Exception {
    testAndAssert("SHOW_DISCOUNT", "true", new CustomerSettings());
  }

  @Test
  public void testProcessSessionTimeoutSecondsSetting() throws Exception {
    testAndAssert("SESSION_TIMEOUT_SECONDS", String.valueOf(Long.MAX_VALUE), new CustomerSettings());
  }

  private void testAndAssert(String column, String value, CustomerSettings settings) throws Exception {
    final CustomerSettings updatedSettings = buildItemProcessor(column, value)
      .process(settings);
    log.debug("{}", JsonUtils.convertObjectToPrettyJson(updatedSettings));
  }

  private CustomersChangeSettingItemProcessor buildItemProcessor(String column, String value)
    throws Exception {
    this.settingColumn = column;
    this.settingValue = value;

    Method method = CustomerSettingsTypeMapper.findMethod(this.settingColumn);
    Function<String, Object> mapper = CustomerSettingsTypeMapper.findTypeMapper(this.settingColumn);
    CustomersChangeSettingItemProcessor processor =
      new CustomersChangeSettingItemProcessor(method, mapper, this.settingValue);
    processor.afterPropertiesSet();
    return processor;
  }

}
