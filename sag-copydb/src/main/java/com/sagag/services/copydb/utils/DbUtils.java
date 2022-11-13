package com.sagag.services.copydb.utils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.SessionImpl;

import com.sagag.services.copydb.batch.BatchJobException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utilities for database.
 */
@UtilityClass
@Slf4j
public class DbUtils {

  private static final String SPACE = " ";

  private static final String SQL_ERROR_MSG = "SQL error {}";

  /**
   * Turn on the database IDENTITY_INSERT variable.
   * 
   * @param session the database connection session.
   * @param tableName the table to update
   * @return <code>true</code> if the statement run successfully. <code>false</code> otherwise.
   * @throws BatchJobException exception thrown when program failed
   */
  public static boolean turnOnIdentityInsert(final SessionImpl session, final String tableName) throws BatchJobException {
    return setIdentityInsertOnOrOff(session, tableName, true);
  }

  /**
   * Turn off the database IDENTITY_INSERT variable.
   * 
   * @param session the database connection session.
   * @param tableName the table to update
   * @return <code>true</code> if the statement run successfully. <code>false</code> otherwise.
   * @throws BatchJobException exception thrown when program failed
   */
  public static boolean turnOffIdentityInsert(final SessionImpl session, final String tableName) throws BatchJobException {
    return setIdentityInsertOnOrOff(session, tableName, false);
  }

  /**
   * Turn on/off the database IDENTITY_INSERT variable.
   * 
   * @param session the database connection session.
   * @param tableName the table to update
   * @param on the flag indicating the IDENTITY_INSERT to be on
   * @return <code>true</code> if the statement run successfully. <code>false</code> otherwise.
   * @throws BatchJobException exception thrown when program failed
   */

  private static boolean setIdentityInsertOnOrOff(final SessionImpl session, final String tableName, boolean on) throws BatchJobException {
    try (Statement statement = session.connection().createStatement();) {
      final String mode = on ? "ON" : "OFF";
      final String sql = new StringBuilder("SET IDENTITY_INSERT ").append(tableName).append(StringUtils.SPACE).append(mode).toString();
      log.info(sql);
      return statement.execute(sql);
    } catch (SQLException ex) {
      log.error(SQL_ERROR_MSG, ex);
      throw new BatchJobException(ex);
    }
  }

  /**
   * Drops all generated batch tables.
   */
  public static boolean dropTables(final SessionImpl session, final List<String> tables) throws BatchJobException {
    try (Statement statement = session.connection().createStatement();) {
      final StringBuilder queryBuilder = new StringBuilder();
      tables.forEach(tb -> queryBuilder.append("DROP TABLE ").append(tb).append(";"));
      final String sql = queryBuilder.toString();
      log.info(sql);
      return statement.execute(sql);
    } catch (SQLException ex) {
      log.error(SQL_ERROR_MSG, ex);
      throw new BatchJobException(ex);
    }
  }

  /**
   * Truncate all tables.
   */
  public static boolean truncateTables(final SessionImpl session, final List<String> tables) throws BatchJobException {
    try (Statement statement = session.connection().createStatement();) {
      final StringBuilder queryBuilder = new StringBuilder();
      tables.forEach(tb -> queryBuilder.append("DELETE FROM ").append(tb).append(";"));
      final String sql = queryBuilder.toString();
      log.info(sql);
      return statement.execute(sql);
    } catch (SQLException ex) {
      log.error(SQL_ERROR_MSG, ex);
      throw new BatchJobException(ex);
    }
  }

  /**
   * Copy data from a source table to destination table for those specific <code>copiedColumn</code>.
   * 
   * @param session the database connection
   * @param fromTable the source table
   * @param copiedColumns the copied columns of source table
   * @param toTable the destination table
   * @param toColumns the destination columns in the case different columns name mapping
   * @return <code>true</code> if the statement run successfully. <code>false</code> otherwise.
   */
  public static boolean copyTable(final SessionImpl session, final String fromTable, final String copiedColumns, final String toTable, final String toColumns)
      throws BatchJobException {
    try (Statement statement = session.connection().createStatement();) {
      final StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
      queryBuilder.append(toTable).append("(").append(toColumns).append(")");
      queryBuilder.append(" SELECT ").append(copiedColumns);
      queryBuilder.append(" FROM ").append(fromTable);
      final String sql = queryBuilder.toString();
      log.info(sql);
      return statement.execute(sql);
    } catch (SQLException ex) {
      log.error(SQL_ERROR_MSG, ex);
      throw new BatchJobException(ex);
    }
  }

  /**
   * Builds the get all tables query.
   * 
   * @return the query to get all tables and its columns
   */
  public static StringBuilder buildAllTablesQuery() {
    final StringBuilder allTablesQuery = new StringBuilder();
    allTablesQuery.append("SELECT t.name AS table_name,").append(SPACE);
    allTablesQuery.append("c.name AS column_name").append(SPACE);
    allTablesQuery.append("FROM sys.tables AS t").append(SPACE);
    allTablesQuery.append("INNER JOIN sys.columns c ON t.OBJECT_ID = c.OBJECT_ID").append(SPACE);
    allTablesQuery.append("ORDER BY table_name");
    return allTablesQuery;
  }
}
