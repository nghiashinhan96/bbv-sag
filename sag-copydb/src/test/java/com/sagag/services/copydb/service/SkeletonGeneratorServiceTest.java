package com.sagag.services.copydb.service;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sagag.services.copydb.AbstractServiceTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkeletonGeneratorServiceTest extends AbstractServiceTest {

  @Autowired
  private SkeletonGeneratorService generator;

  @Test
  public void generateSkeletons() {
    try {
      generator.generateSkeletons();
    } catch (ClassNotFoundException | IOException | SQLException ex) {
      log.error("{}", ex);
      fail("Spring batch skeletons generation failed");
    }
  }

  @Test
  public void getTablesDependencies() throws IOException {
    final Map<String, List<String>> tableDependencies = generator.getTablesDependencies();
    final StringBuilder dependencies = new StringBuilder();
    dependencies.append(RETURNED_LINE);
    for (final Entry<String, List<String>> entries : tableDependencies.entrySet()) {
      dependencies.append(NEW_LINE + entries.getKey());
      for (String dependence : entries.getValue()) {
        dependencies.append(NEW_LINE).append(NEW_TAB).append(dependence);
      }
    }
    final String dependencyContents = dependencies.toString();
    Assert.assertThat(dependencyContents, Matchers.notNullValue());

    final String outFileName = String.format("sample/table_dependencies_%s_%s.log", getOneActiveProfile(),
        new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
    writeFile(outFileName, dependencies.toString());

  }

  @Test
  public void sortTableDependencies() throws IOException {
    final List<String> sortedTables = generator.sortTableDependencies(getProperties().getTables());
    final String profile = getOneActiveProfile();
    Assert.assertThat(sortedTables, Matchers.hasSize(Matchers.greaterThan(0)));
    final String sortedTablesFile =
        String.format("sample/table_dependencies_sorted_%s_%s.log", profile, new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
    writeLinesToFile(sortedTablesFile, sortedTables);

    List<String> reversedTables = new ArrayList<>(sortedTables);
    Collections.reverse(reversedTables);
    final List<String> dropTablesQuery = reversedTables.stream().map(t -> String.format("DROP TABLE %s;", t)).collect(Collectors.toList());
    final String dropScriptFile = String.format("sample/table_dependencies_sorted_drop_%s_%s.log", profile,
        new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
    writeLinesToFile(dropScriptFile, dropTablesQuery);

  }
}
