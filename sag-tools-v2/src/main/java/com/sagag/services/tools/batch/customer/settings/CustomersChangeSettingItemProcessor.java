package com.sagag.services.tools.batch.customer.settings;

import com.sagag.services.tools.domain.target.CustomerSettings;
import java.lang.reflect.Method;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

@RequiredArgsConstructor
@Slf4j
public class CustomersChangeSettingItemProcessor
  implements ItemProcessor<CustomerSettings, CustomerSettings>, InitializingBean {

  @NonNull
  private Method method;

  @NonNull
  private Function<String, Object> typeMapper;

  @NonNull
  private String settingValue;

  @Override
  public void afterPropertiesSet() throws Exception {
    log.debug("Checking the setting input settingValue = {}", settingValue);
    Assert.hasText(settingValue, "The given setting value must not be empty");
    Assert.notNull(typeMapper, "The given type mapper must not be null");
    Assert.notNull(method, "The given method must not be null");
  }

  @Override
  public CustomerSettings process(CustomerSettings custSettings) throws Exception {
    ReflectionUtils.invokeMethod(method, custSettings, this.typeMapper.apply(settingValue));
    return custSettings;
  }

}
