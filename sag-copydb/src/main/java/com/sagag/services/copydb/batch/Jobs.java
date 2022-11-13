package com.sagag.services.copydb.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 */
@AllArgsConstructor
@Getter
public enum Jobs {

  COPY_DATABASE("CopyDatabase"), // copy data from source to destination schema job
  DROP_BATCH_TABLES("DropBatchTables"), // drop all artefacts batch tables job
  TRUNCATE_ALL_DEST_TABLES("TruncateAllDestTables"), // truncate all destination tables job
  COPY_DATABASE_BY_SQL("CopyDatabaseUsingSQL"); // truncate all destination tables job

  private String name;

}
