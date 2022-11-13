package com.sagag.services.tools.utils;

import java.sql.SQLException;

import com.sagag.services.tools.exception.BatchJobException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.SessionImpl;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utilities for entity manager.
 */
@UtilityClass
@Slf4j
public class EntityManagerUtils {

  public static boolean turnOnIdentityInsert(final SessionImpl session,
      final String tableName) throws BatchJobException {
    return setIdentityInsertOnOrOff(session, tableName, true);
  }
  
  public static boolean turnOffIdentityInsert(final SessionImpl session,
      final String tableName) throws BatchJobException {
    return setIdentityInsertOnOrOff(session, tableName, false);
  }

  /**
   * Some tables set off Identity Insert mode is OFF,
   * to set specific id from EBL, I have to turn on mode and turn off again after finish
   */
  private static boolean setIdentityInsertOnOrOff(final SessionImpl session,
    final String tableName, boolean on) throws BatchJobException {
    try {
      String mode = on ? "ON" : "OFF";
      String sql = "SET IDENTITY_INSERT "+ tableName + StringUtils.SPACE + mode;
      log.info(sql);
      return session.connection().createStatement().execute(sql);
    } catch (SQLException ex) {
      throw new BatchJobException(ex);
    }
  }

}
