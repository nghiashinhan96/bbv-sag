package com.sagag.services.copydb.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sagag.services.copydb.AppProperties;
import com.sagag.services.copydb.utils.DbUtils;

@Service
public class DatabaseDifferenceExporterService {

  @Autowired
  @Qualifier("sourceEntityManager")
  private EntityManager srcEntityManager;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManager destEntityManager;

  @Autowired
  private AppProperties properties;

  /**
   * Exports the differences between 2 database schemas.
   * 
   * @return the differences
   * @throws SQLException thrown when exception
   */
  @Transactional(readOnly = true)
  public DbDifference exportDbDifference() throws SQLException {
    final Connection srcConn = srcEntityManager.unwrap(SessionImpl.class).connection();
    final Connection destConn = destEntityManager.unwrap(SessionImpl.class).connection();
    final DbDifference diff = new DbDifference(new DifferenceSummary(srcConn.getCatalog(), srcConn.getSchema(), srcConn.getMetaData().getURL(),
        destConn.getCatalog(), destConn.getSchema(), destConn.getMetaData().getURL()));

    final StringBuilder allTablesQuery = DbUtils.buildAllTablesQuery();

    final Map<String, List<String>> srcTables = searchTable(srcEntityManager, allTablesQuery.toString());
    final Map<String, List<String>> destTables = searchTable(destEntityManager, allTablesQuery.toString());

    diff.setMinuses(findRemovedTablesOrColumns(destTables, srcTables));
    diff.setPluses(findAddedTablesOrColumns(destTables, srcTables));

    return diff;
  }

  private List<TableDifference> findAddedTablesOrColumns(Map<String, List<String>> destTables, Map<String, List<String>> srcTables) {
    final List<TableDifference> pluses = new ArrayList<>();
    for (Entry<String, List<String>> destTable : destTables.entrySet()) {
      final String destTableName = destTable.getKey();
      if (!properties.getTables().contains(destTableName)) {
        continue; // no report for not migrated tables
      }
      if (!srcTables.containsKey(destTableName)) { // added this table at destination schema
        final TableDifference diff = new TableDifference(destTableName);
        diff.setDifferences(destTable.getValue());
        pluses.add(diff);
      } else { // still exist in the destination schema
        final List<String> destColumns = destTable.getValue();
        final List<String> srcColumns = srcTables.get(destTableName);
        final Collection<String> addedColumns = CollectionUtils.removeAll(destColumns, srcColumns);
        if (!CollectionUtils.isEmpty(addedColumns)) {
          final TableDifference diff = new TableDifference(destTableName);
          diff.setDifferences(new ArrayList<>(addedColumns));
          pluses.add(diff);
        }
      }
    }
    return pluses;
  }

  private List<TableDifference> findRemovedTablesOrColumns(Map<String, List<String>> destTables, Map<String, List<String>> srcTables) {
    final List<TableDifference> minuses = new ArrayList<>();
    for (Entry<String, List<String>> srcEntry : srcTables.entrySet()) {
      final String srcTableName = srcEntry.getKey();
      if (!properties.getTables().contains(srcTableName)) {
        continue; // no report for not migrated tables
      }
      if (!destTables.containsKey(srcTableName)) { // removed this table at destination schema
        minuses.add(new TableDifference(srcTableName));
      } else { // still exist in the destination schema
        final List<String> destColumns = destTables.get(srcTableName);
        final List<String> srcColumns = srcEntry.getValue();
        final Collection<String> removedColumns = CollectionUtils.removeAll(srcColumns, destColumns);
        if (!CollectionUtils.isEmpty(removedColumns)) {
          final TableDifference diff = new TableDifference(srcTableName);
          diff.setDifferences(new ArrayList<>(removedColumns));
          minuses.add(diff);
        }
      }
    }
    return minuses;
  }

  @SuppressWarnings("unchecked")
  @Transactional(readOnly = true)
  public Map<String, List<String>> searchTable(EntityManager em, String query) {
    final Query allTablesQuery = em.createNativeQuery(query);
    final List<String[]> allTables = allTablesQuery.getResultList();
    final Map<String, List<String>> distinctTables = new HashMap<>();
    for (Object[] srcRecord : allTables) {
      String tableName = (String) srcRecord[0];
      String tableColumn = (String) srcRecord[1];
      if (properties.isCompareIgnoreCase()) {
        tableName = StringUtils.upperCase(tableName);
        tableColumn = StringUtils.upperCase(tableColumn);
      }
      distinctTables.putIfAbsent(tableName, new ArrayList<>());
      distinctTables.get(tableName).add(tableColumn);
    }
    return distinctTables;
  }

}
