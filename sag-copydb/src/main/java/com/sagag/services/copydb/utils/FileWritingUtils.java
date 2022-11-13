package com.sagag.services.copydb.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class FileWritingUtils {

  private static final String GENERATED_FILE_INFO_MSG = "Generated file {}";
  private static final String UTF_8 = StandardCharsets.UTF_8.name();
  private static final String FORWARD_SLASH = "/";
  private static final Path PROJECT_PATH = Paths.get("src/main/java/");

  /**
   * Writes the contents of the Spring Batch Item processor to file.
   * 
   * @param entity the entity to generate its processor
   * @param toPackage the package that the generated processor file will be located
   * @param contents the entity processor contents
   * @throws IOException error while writing file
   */
  public static void writeItemProcessorFile(String entity, String toPackage, String contents) throws IOException {
    final String fileName = toGeneratedPackage(toPackage).append(toProcessorFile(entity)).toString();
    log.info(GENERATED_FILE_INFO_MSG, fileName);
    FileUtils.writeStringToFile(new File(fileName), contents, UTF_8);
  }

  private static StringBuilder toGeneratedPackage(final String toPackage) {
    return new StringBuilder(PROJECT_PATH.toAbsolutePath().toString()).append(FORWARD_SLASH).append(toRelativePath(toPackage)).append(FORWARD_SLASH);
  }

  private static String toRelativePath(final String toPackage) {
    return toPackage.replaceAll("\\.", "/");
  }

  private static StringBuilder toProcessorFile(String entity) {
    return new StringBuilder(entity).append("Processor.java");
  }

  private static StringBuilder toRepositoryFile(String entity) {
    return new StringBuilder(entity).append("Repository.java");
  }

  /**
   * Writes the contents of the JPA repository to file.
   * 
   * @param entity the entity to generate its JPA repository
   * @param toPackage the package that the generated repository will be located
   * @param contents the JPA repository contents
   * @throws IOException error while writing file
   */
  public static void writeRepositoryFile(String entity, String toPackage, String contents) throws IOException {
    final String fileName = toGeneratedPackage(toPackage).append(toRepositoryFile(entity)).toString();
    log.info(GENERATED_FILE_INFO_MSG, fileName);
    FileUtils.writeStringToFile(new File(fileName), contents, UTF_8);
  }

  /**
   * Writes the contents of the Spring Batch Item Writer to file.
   * 
   * @param entity the entity to generate its Item Writer
   * @param toPackage the package that the generated Item Writer will be located
   * @param contents the Item Writer contents
   * @throws IOException error while writing file
   */
  public static void writeItemWriterFile(String entity, String toPackage, String contents) throws IOException {
    final String fileName = toGeneratedPackage(toPackage).append(toItemWriterFile(entity)).toString();
    log.info(GENERATED_FILE_INFO_MSG, fileName);
    FileUtils.writeStringToFile(new File(fileName), contents, UTF_8);
  }

  private static StringBuilder toItemWriterFile(String entity) {
    return new StringBuilder(entity).append("Writer.java");
  }

  /**
   * Writes the contents of the Spring Batch Job Step to file.
   * 
   * @param entity the entity to generate its Job Step
   * @param toPackage the package that the generated Job Step will be located
   * @param contents the Job Step contents
   * @throws IOException error while writing file
   */
  public static void writeItemJobStepFile(String entity, String toPackage, String contents) throws IOException {
    final String fileName = toGeneratedPackage(toPackage).append(toItemJobStepFile(entity)).toString();
    log.info(GENERATED_FILE_INFO_MSG, fileName);
    FileUtils.writeStringToFile(new File(fileName), contents, UTF_8);
  }

  private static StringBuilder toItemJobStepFile(String entity) {
    return new StringBuilder(entity).append("CopySteps.java");
  }

  /**
   * Writes the contents of the Spring Batch Job Configuration to file.
   * 
   * @param toPackage the package that the generated file will be located
   * @param contents the Job Configuration contents
   * @throws IOException error while writing file
   */
  public static void writeCopyDbJobConfigFile(String toPackage, String contents) throws IOException {
    final String fileName = toGeneratedPackage(toPackage).append("CopyDbJobConfig.java").toString();
    log.info(GENERATED_FILE_INFO_MSG, fileName);
    FileUtils.writeStringToFile(new File(fileName), contents, UTF_8);
  }

  /**
   * Writes the contents of the Turn off ID Insertion job to file.
   * 
   * @param toPackage the package that the generated file will be located
   * @param contents the file contents for all entities
   * @throws IOException error while writing file
   */
  public static void writeTurnOffIdInsertionFile(String toPackage, Map<String, String> contents) {
    contents.entrySet().stream().forEach(content -> {
      final String fileName = toGeneratedPackage(toPackage).append(content.getKey()).append("TurnOffIdentityInsertion.java").toString();
      log.info(GENERATED_FILE_INFO_MSG, fileName);
      try {
        FileUtils.writeStringToFile(new File(fileName), content.getValue(), UTF_8);
      } catch (IOException ex) {
        log.error("Error while writing file {}", fileName);
        log.error("{}", ex); // just logging the error, continue doing for other files
      }
    });
  }

  /**
   * Writes the contents of the drop artefacts batch tables job file.
   * 
   * @param toPackage the package that the generated file will be located
   * @param contents the Job Configuration contents
   * @throws IOException error while writing file
   */
  public static void writeDropBatchTablesFile(String toPackage, String contents) throws IOException {
    final String fileName = toGeneratedPackage(toPackage).append("DropBatchTablesTasklet.java").toString();
    log.info(GENERATED_FILE_INFO_MSG, fileName);
    FileUtils.writeStringToFile(new File(fileName), contents, UTF_8);
  }

  /**
   * Writes the contents of the table truncate job file.
   * 
   * @param toPackage the package that the generated file will be located
   * @param contents the file contents
   * @throws IOException error while writing file
   */
  public static void writeTruncateAllDestTablesFile(String toPackage, String contents) throws IOException {
    final String fileName = toGeneratedPackage(toPackage).append("TruncateAllDestTablesTasklet.java").toString();
    log.info(GENERATED_FILE_INFO_MSG, fileName);
    FileUtils.writeStringToFile(new File(fileName), contents, UTF_8);
  }

  /**
   * Writes the contents of SQL Copied table job to file.
   * 
   * @param toPackage the package that the generated file will be located
   * @param contents the file contents for all entities
   * @throws IOException error while writing file
   */
  public static void writeSQLCopiedTasksFile(String toPackage, Map<String, String> contents) {
    contents.entrySet().stream().forEach(content -> {
      final String fileName = toGeneratedPackage(toPackage).append(content.getKey()).append("SQLCopiedTasklet.java").toString();
      log.info(GENERATED_FILE_INFO_MSG, fileName);
      try {
        FileUtils.writeStringToFile(new File(fileName), content.getValue(), UTF_8);
      } catch (IOException ex) {
        log.error("Error while writing file {}", fileName);
        log.error("{}", ex); // just logging the error, continue doing for other files
      }
    });
  }

  /**
   * Writes the contents of the Spring Batch SQL Job Configuration to file.
   * 
   * @param toPackage the package that the generated file will be located
   * @param contents the Job Configuration contents
   * @throws IOException error while writing file
   */
  public static void writeCopyDbSQLJobConfigFile(String toPackage, String contents) throws IOException {
    final String fileName = toGeneratedPackage(toPackage).append("CopyDbSQLJobConfig.java").toString();
    log.info(GENERATED_FILE_INFO_MSG, fileName);
    FileUtils.writeStringToFile(new File(fileName), contents, UTF_8);
  }

}
