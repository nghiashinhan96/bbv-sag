package com.sagag.services.copydb.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sagag.services.copydb.AppProperties;
import com.sagag.services.copydb.utils.DbUtils;
import com.sagag.services.copydb.utils.FileWritingUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Class to generate the skeletons for the copy tables batch script.
 * <p>
 * Including
 * <li>Spring JPA repositories generation
 * <li>Spring batch item process generation
 * <li>Spring batch item writer generation
 * <li>Jobs steps generation
 * <li>Drop all artefacts tables job generation - from Spring Batch execution
 * <li>Truncate all destination tables data job generation
 */
@Slf4j
@Service
public class SkeletonGeneratorService {

  private static final String DOT = ".";
  private static final String REPO_PACKAGE_SRC = "com.sagag.services.copydb.repo.src";
  private static final String REPO_PACKAGE_DEST = "com.sagag.services.copydb.repo.dest";
  private static final String DOMAIN_PACKAGE_SRC = "com.sagag.services.copydb.domain.src";
  private static final String DOMAIN_PACKAGE_DEST = "com.sagag.services.copydb.domain.dest";
  private static final String STEPS_PACKAGE_SRC = "com.sagag.services.copydb.steps";
  private static final String TASKS_CONFIG_PACKAGE = "com.sagag.services.copydb.batch.tasks";
  private static final String COPYJOB_CONFIG_PACKAGE = "com.sagag.services.copydb.batch.jobs";

  @Autowired
  private AppProperties properties;

  @Autowired
  @Qualifier("sourceEntityManager")
  private EntityManager srcEntityManager;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManager destEntityManager;

  @Autowired
  private DatabaseDifferenceExporterService exportService;

  /**
   * Generates spring batch skeleton for copying tables.
   * 
   * @throws IOException
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  @Transactional(readOnly = true)
  public void generateSkeletons() throws ClassNotFoundException, IOException, SQLException {
    final List<String> tables = sortTableDependencies(properties.getTables());
    log.info("Copying tables {}", tables);
    final List<String> entities = toJavaEntities(tables);
    log.info("Java entities {}", entities);
    final Map<String, List<String>> allSortedTablesWithColumns = getAllSortedTablesAndColumns(tables);
    log.info("Tables with Columns {}", allSortedTablesWithColumns);

    if (StringUtils.isBlank(properties.getFromSchema())) {
      throw new IllegalArgumentException("From schema must not be empty.");
    }
    final String fromSchema = properties.getFromSchema() + DOT;
    final String toSchema = StringUtils.isBlank(properties.getToSchema()) ? "" : (properties.getToSchema() + DOT);

    String contents = "";
    for (final String entity : entities) {
      contents = SkeletonGenerator.generateRepository(entity, REPO_PACKAGE_SRC, DOMAIN_PACKAGE_SRC);
      FileWritingUtils.writeRepositoryFile(entity, REPO_PACKAGE_SRC, contents);

      final String destEntity = SkeletonGenerator.toDest(entity);
      contents = SkeletonGenerator.generateRepository(destEntity, REPO_PACKAGE_DEST, DOMAIN_PACKAGE_DEST);
      FileWritingUtils.writeRepositoryFile(destEntity, REPO_PACKAGE_DEST, contents);

      contents = SkeletonGenerator.generateItemProcessor(entity, STEPS_PACKAGE_SRC);
      FileWritingUtils.writeItemProcessorFile(entity, STEPS_PACKAGE_SRC, contents);

      contents = SkeletonGenerator.generateItemWriter(entity, STEPS_PACKAGE_SRC);
      FileWritingUtils.writeItemWriterFile(destEntity, STEPS_PACKAGE_SRC, contents);

      contents = SkeletonGenerator.generateItemJobStep(entity, STEPS_PACKAGE_SRC);
      FileWritingUtils.writeItemJobStepFile(entity, STEPS_PACKAGE_SRC, contents);


    }
    final Map<String, String> allContents = SkeletonGenerator.generateTurnOffIdInsertion(entities, STEPS_PACKAGE_SRC);
    FileWritingUtils.writeTurnOffIdInsertionFile(STEPS_PACKAGE_SRC, allContents);

    contents = SkeletonGenerator.generateDropBatchTables(properties.getArtefacts().getTables(), STEPS_PACKAGE_SRC);
    FileWritingUtils.writeDropBatchTablesFile(STEPS_PACKAGE_SRC, contents);
    contents = SkeletonGenerator.generateTruncateAllDestTables(tables, STEPS_PACKAGE_SRC);
    FileWritingUtils.writeTruncateAllDestTablesFile(STEPS_PACKAGE_SRC, contents);

    contents = SkeletonGenerator.generateCopyDbJobConfig(entities, COPYJOB_CONFIG_PACKAGE);
    FileWritingUtils.writeCopyDbJobConfigFile(COPYJOB_CONFIG_PACKAGE, contents);

    final Map<String, String> sqlCopiedTasks =
        SkeletonGenerator.generateSQLCopiedTasks(tables, allSortedTablesWithColumns, fromSchema, toSchema, TASKS_CONFIG_PACKAGE);
    FileWritingUtils.writeSQLCopiedTasksFile(TASKS_CONFIG_PACKAGE, sqlCopiedTasks);
    contents = SkeletonGenerator.generateCopySQLDbJobConfig(entities, COPYJOB_CONFIG_PACKAGE);
    FileWritingUtils.writeCopyDbSQLJobConfigFile(COPYJOB_CONFIG_PACKAGE, contents);
  }

  private Map<String, List<String>> getAllSortedTablesAndColumns(List<String> tables) {
    final Map<String, List<String>> allRawTablesAndColumns = exportService.searchTable(srcEntityManager, DbUtils.buildAllTablesQuery().toString());
    final Map<String, List<String>> allTablesAndColumns = new HashMap<>();
    if (CollectionUtils.isEmpty(tables)) {
      return allTablesAndColumns;
    }
    tables.stream().forEach(table -> {
      allTablesAndColumns.put(table, allRawTablesAndColumns.get(table));
    });
    return allTablesAndColumns;
  }

  /**
   * Returns all other table dependencies for a table in the copying table list.
   * <p>
   * This is required when we copying data to a table when the its referenced table should be copied first.
   * 
   * @return the map of table with its referenced tables.
   * @see copied the ideas from https://stackoverflow.com/questions/7647718/sqlserver-how-to-find-dependent-tables-on-my-table/11874493#11874493
   */
  @Transactional(readOnly = true)
  public Map<String, List<String>> getTablesDependencies() {
    final StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select");
    queryBuilder.append(" SO_P.name as [PARENT_TABLE]");
    queryBuilder.append(",SS_P.name as [PARENT_TABLE_SCHEMA]");
    queryBuilder.append(",SC_P.name as [PARENT_COLUMN]");
    queryBuilder.append(",'is a foreign key of' as [direction]");
    queryBuilder.append(",SO_R.name as [REFERENCED_TABLE]");
    queryBuilder.append(",SS_R.name as [REFERENCED_TABLE_SCHEMA]");
    queryBuilder.append(",SC_R.name as [REFERENCED_COLUMN]");
    queryBuilder.append(" from sys.foreign_key_columns FKC");
    queryBuilder.append(" inner join sys.objects SO_P on SO_P.object_id = FKC.parent_object_id");
    queryBuilder.append(" inner join sys.schemas SS_P on SS_P.schema_id = SO_P.schema_id");
    queryBuilder.append(" inner join sys.columns SC_P on (SC_P.object_id = FKC.parent_object_id) AND (SC_P.column_id = FKC.parent_column_id)");
    queryBuilder.append(" inner join sys.objects SO_R on SO_R.object_id = FKC.referenced_object_id");
    queryBuilder.append(" inner join sys.schemas SS_R on SS_R.schema_id = SO_P.schema_id");
    queryBuilder.append(" inner join sys.columns SC_R on (SC_R.object_id = FKC.referenced_object_id) AND (SC_R.column_id = FKC.referenced_column_id)");
    queryBuilder.append(" where SO_P.type = 'U' OR SO_R.type = 'U'");

    log.info("Query {}", queryBuilder);
    final Query query = srcEntityManager.createNativeQuery(queryBuilder.toString());
    @SuppressWarnings("unchecked")
    final List<String[]> dependencies = query.getResultList();
    final Map<String, List<String>> distinctTableDependencies = new HashMap<>();
    final List<String> validTables = properties.getTables();
    for (Object[] record : dependencies) {
      final String tableName = (String) record[0];
      if (validTables.contains(tableName)) {
        distinctTableDependencies.putIfAbsent(tableName, new ArrayList<>());
        distinctTableDependencies.get(tableName).add((String) record[4]);
      }
    }
    log.info("Dependency {}", distinctTableDependencies);
    return distinctTableDependencies;
  }

  private static List<String> toJavaEntities(List<String> tables) {
    return tables.stream().map(SkeletonGeneratorService::toJavaPojoNamesFromTableNames).collect(Collectors.toList());
  }

  public static String toJavaPojoNamesFromTableNames(String table) {
    return WordUtils.capitalizeFully(table.toLowerCase(), '_').replaceAll("_", "");
  }

  /**
   * Sorted the copied tables from its other dependent tables.
   * <p>
   * The results is the sorted tables to which the lowest dependencies will be the first ones to copied data.
   * 
   * @param copiedTables the copied tables from the source to the destination schema
   * @return a sorted list of tables
   */
  @Transactional(readOnly = true)
  public List<String> sortTableDependencies(final List<String> copiedTables) {
    if (CollectionUtils.isEmpty(copiedTables)) {
      return Collections.emptyList();
    }
    final Map<String, List<String>> dependencies = getTablesDependencies();

    final Map<String, List<String>> transitiveDependencies = new HashMap<>();
    for (final Entry<String, List<String>> dependency : dependencies.entrySet()) {
      final String table = dependency.getKey();
      final List<String> tableTransitiveDependencies = new ArrayList<>();
      tableTransitiveDependencies.addAll(getTransitiveDependencies(table, dependencies));

      transitiveDependencies.putIfAbsent(table, new ArrayList<>());
      transitiveDependencies.get(table).addAll(tableTransitiveDependencies);
    }

    final StringBuilder dependencyContents = new StringBuilder();
    for (final Entry<String, List<String>> dependency : transitiveDependencies.entrySet()) {
      dependencyContents.append(String.format("%n-%s", dependency.getKey()));
      for (String d : dependency.getValue()) {
        dependencyContents.append(String.format("%n\t-%s", d));
      }
    }
    log.info("Tables transitive dependencies {}", dependencyContents.toString());

    final List<String> sorted = new ArrayList<>(copiedTables);
    for (final Entry<String, List<String>> dependency : transitiveDependencies.entrySet()) {
      final String table = dependency.getKey();
      sorted.remove(table);
      final int maxIdx = findMaxIndexOf(sorted, dependency.getValue());
      sorted.add(maxIdx + 1, table); // move the table after the dependent ones
    }

    return Collections.unmodifiableList(sorted);
  }

  private static int findMaxIndexOf(List<String> sorted, List<String> tables) {
    final Optional<Integer> maxIdxOpt = tables.stream().map(sorted::indexOf).max(Comparator.comparing(Integer::valueOf));
    if (maxIdxOpt.isPresent()) {
      return maxIdxOpt.get().intValue();
    }
    return sorted.size() - 1;
  }

  private static Set<String> getTransitiveDependencies(String table, Map<String, List<String>> allDependencies) {
    final List<String> dependencies = allDependencies.get(table);
    if (CollectionUtils.isEmpty(dependencies)) {
      return Collections.emptySet();
    }
    final Set<String> transitiveDependencies = new HashSet<>(dependencies);
    for (String tableDependency : dependencies) {
      transitiveDependencies.addAll(getTransitiveDependencies(tableDependency, allDependencies));
    }
    return transitiveDependencies;
  }
}
