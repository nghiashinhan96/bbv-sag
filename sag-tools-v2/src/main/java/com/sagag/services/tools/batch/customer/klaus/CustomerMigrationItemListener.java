package com.sagag.services.tools.batch.customer.klaus;

import com.sagag.services.tools.domain.customer.CsvCustomerInfoData;
import com.sagag.services.tools.domain.customer.ImportedCustomerData;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.SystemUtils;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.batch.operations.BatchRuntimeException;

@Component
@StepScope
@Slf4j
public class CustomerMigrationItemListener
    implements ItemProcessListener<CsvCustomerInfoData, ImportedCustomerData>,
    ItemWriteListener<ImportedCustomerData> {

  private static final String COMPLETED_MIGRATED_CUSTOMER_CSV =
      SystemUtils.getUserDir() + "/csv/completed_migrated_klaus_customer";

  private static final String NOT_MIGRATED_CUSTOMER_CSV =
      SystemUtils.getUserDir() + "/csv/not_migrated_klaus_customer";

  private List<String> completeMigratedCustomerList = new ArrayList<>();

  private List<String> notMigratedCustomerList = new ArrayList<>();

  @Override
  public void beforeProcess(CsvCustomerInfoData item) {
    log.info("****************************** Before Process ******************************");
    log.debug("Item = {}", item);
    log.info("****************************** End Before Process ******************************");
  }

  @Override
  public void afterProcess(CsvCustomerInfoData item, ImportedCustomerData result) {
    log.info("****************************** After Process ******************************");
    log.debug("Result = {}", result);
    if (item != null && (result == null)) {
      log.warn("****************************** Not Migrate ******************************");
      log.warn("Customer = {} have not enough data to migrate", item.getCustomerNr());
      notMigratedCustomerList.add(item.getCustomerNr());
      log.warn("****************************** End Not Migrate ******************************");
    }
    if (item != null && result != null) {
      completeMigratedCustomerList.add(result.getCustomerNr());
    }

    log.info("****************************** End After Process ******************************");
  }

  @Override
  public void onProcessError(CsvCustomerInfoData item, Exception e) {

    log.info("****************************** onProcessError ******************************");
    log.error("Processing customer has error with customer = {} ", item.getCustomerNr(), e);
    log.info("****************************** End onProcessError ******************************");
    throw new BatchRuntimeException(e);
  }

  @Override
  public void beforeWrite(List<? extends ImportedCustomerData> items) {
    log.warn("The not migrated Klaus customers = {}",
        JsonUtils.convertObjectToPrettyJson(notMigratedCustomerList));
    try {
      final List<CsvKlausCustomerInfo> custNrs = notMigratedCustomerList.stream()
          .map(CsvKlausCustomerInfo::new).collect(Collectors.toList());
      CsvUtils.write(NOT_MIGRATED_CUSTOMER_CSV + System.currentTimeMillis() + ".csv",
          custNrs);
    } catch (BatchJobException e) {
      log.error("Error :", e);
    }
  }

  @Override
  public void afterWrite(List<? extends ImportedCustomerData> items) {

    log.debug("The completeted migrated Klaus customers = {}",
        JsonUtils.convertObjectToPrettyJson(completeMigratedCustomerList));
    try {
      final List<CsvKlausCustomerInfo> custNrs = completeMigratedCustomerList.stream()
          .map(CsvKlausCustomerInfo::new).collect(Collectors.toList());
      CsvUtils.write(COMPLETED_MIGRATED_CUSTOMER_CSV + System.currentTimeMillis() + ".csv",
          custNrs);
    } catch (BatchJobException e) {
      log.error("Error :", e);
    }
  }

  @Override
  public void onWriteError(Exception exception, List<? extends ImportedCustomerData> items) {
    // do nothing
  }

}
