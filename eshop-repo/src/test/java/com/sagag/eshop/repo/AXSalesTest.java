package com.sagag.eshop.repo;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Test class to load csv file and generate the insert script.
 */
@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class AXSalesTest {

  @Test
  @Ignore("only for support insert test query, no benefit in feature testing")
  public void givenAXSalesCSV_shouldGetInsertScript() throws IOException {
    final List<CsvAadAccount> accounts = loadCSVData(CsvAadAccount.class, "sales/AX_Sales.csv");
    accounts.forEach(a -> {
      a.setPermitGroup("SALES");
      log.debug("AX Accounts: {}", a.toInsertScript());
    });
    File outFile = writeOutToFile("sales/AX_Sales_Insert.sql", accounts);
    Assert.assertThat(outFile.length(), Matchers.greaterThan(0L));
  }

  private static File writeOutToFile(final String fileName, final Collection<CsvAadAccount> data)
      throws IOException {
    final File out = new ClassPathResource(fileName).getFile();
    FileUtils.writeLines(out, data);
    return out;
  }

  private static <T> List<T> loadCSVData(Class<T> type, String fileName) {
    try {
      final CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
      final CsvMapper csvMapper = new CsvMapper();
      final File file = new ClassPathResource(fileName).getFile();
      final MappingIterator<T> readValues =
          csvMapper.readerFor(type).with(csvSchema).readValues(file);
      return readValues.readAll();
    } catch (final IOException ioe) {
      log.error("Error occurred while loading object list from file " + fileName, ioe);
      return Collections.emptyList();
    }
  }

}
