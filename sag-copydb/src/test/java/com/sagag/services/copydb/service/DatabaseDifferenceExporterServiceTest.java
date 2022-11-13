package com.sagag.services.copydb.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sagag.services.copydb.AbstractServiceTest;

public class DatabaseDifferenceExporterServiceTest extends AbstractServiceTest {

  @Autowired
  private DatabaseDifferenceExporterService exporter;

  @Test
  public void exportDbDifference() throws SQLException, IOException {
    final DbDifference diff = exporter.exportDbDifference();
    Assert.assertThat(diff, Matchers.notNullValue());

    final StringBuilder diffText = new StringBuilder();

    diffText.append(diff.getSummary());
    diffText.append(RETURNED_LINE);
    final List<TableDifference> minuses = diff.getMinuses();
    if (!CollectionUtils.isEmpty(minuses)) {
      diffText.append("\n(-)Minuses");
      for (TableDifference tabDiff : minuses) {
        diffText.append("\n\t").append(tabDiff.getName());
        for (String colDiff : tabDiff.getDifferences()) {
          diffText.append("\n\t\t-").append(colDiff);
        }
      }
    }
    diffText.append(RETURNED_LINE);
    final List<TableDifference> pluses = diff.getPluses();
    if (!CollectionUtils.isEmpty(pluses)) {
      diffText.append("\n(+)Pluses");
      for (TableDifference tabDiff : pluses) {
        diffText.append("\n\t").append(tabDiff.getName());
        for (String colDiff : tabDiff.getDifferences()) {
          diffText.append("\n\t\t-").append(colDiff);
        }
      }
    }
    final String outFileName = String.format("sample/db_difference_%s_%s.log", getOneActiveProfile(),
        new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
    writeFile(outFileName, diffText.toString());
  }

}
