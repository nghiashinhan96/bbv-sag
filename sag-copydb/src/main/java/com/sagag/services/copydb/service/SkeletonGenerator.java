package com.sagag.services.copydb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for copying script skeleton generator.
 * 
 */
@UtilityClass
@Slf4j
public class SkeletonGenerator {

  private static final String SPACE = " ";
  private static final String EMPTY = "";
  private static final String DOT = ".";
  private static final Pattern COMPILE = Pattern.compile("(?<=[a-z])[A-Z]");

  /**
   * Generates the Spring JPA Repository template from the entity.
   * 
   * @param entity the JPA entity mapping
   * @param repoPackage the package of the generated repository
   * @param repoDomain the package of the entity
   * @return a Spring JPA Repository template class
   * @throws ClassNotFoundException thrown when no entity found in the <code>repoDomain</code>.
   */
  public static String generateRepository(String entity, String repoPackage, String repoDomain) throws ClassNotFoundException {
    log.info("Generate Repositories for entity {}", entity);
    final StringBuilder template = new StringBuilder();
    template.append("package ").append(repoPackage).append(";\r\n");
    template.append("\r\n");
    template.append("import ").append(repoDomain).append(DOT).append(entity).append(";\r\n");
    template.append("\r\n");
    template.append("import org.springframework.data.jpa.repository.JpaRepository;\r\n");
    template.append("\r\n");
    template.append("public interface %sRepository extends JpaRepository<%s, %s> {\r\n");
    template.append("}\r\n");
    final String entityIdType = findEntityIdType(repoDomain, entity);
    final String contents = String.format(template.toString(), entity, entity, entityIdType);
    log.info("JPA Repository content: {}", contents);
    return contents;
  }

  private static String findEntityIdType(String repoDomain, String entity) throws ClassNotFoundException {
    final Class<?> clzz = Class.forName(new StringBuilder(repoDomain).append(DOT).append(entity).toString());
    String idTypeName = "int";
    try {
      idTypeName = clzz.getDeclaredField("id").getType().getTypeName();
    } catch (NoSuchFieldException nsfe) {
      log.error("No Id field for class: {}", entity);
      idTypeName = "int";
    }
    if ("int".equals(idTypeName)) {
      return "Integer";
    }
    if ("long".equals(idTypeName)) {
      return "Long";
    }
    return idTypeName;
  }

  /**
   * Generates the Spring Batch Item Processor template from the entity.
   * 
   * @param entity the JPA entity mapping
   * @param stepsPackage the package of the generated template
   * @return a Spring Batch Item Processor template class
   */
  public static String generateItemProcessor(String entity, final String stepsPackage) {
    log.info("Generate Item processor for entity {}", entity);
    final StringBuilder template = new StringBuilder("package ").append(stepsPackage).append(";\r\n");
    final String destEntity = toDest(entity);
    template.append("\r\n");
    template.append("import com.sagag.services.copydb.config.CopyDbProfile;\r\n");
    template.append("import com.sagag.services.copydb.domain.src.").append(entity).append(";\r\n");
    template.append("import com.sagag.services.copydb.domain.dest.").append(destEntity).append(";\r\n");
    template.append("\r\n");
    template.append("import org.dozer.DozerBeanMapper;\r\n");
    template.append("import org.springframework.batch.item.ItemProcessor;\r\n");
    template.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
    template.append("import org.springframework.beans.factory.annotation.Qualifier;\r\n");
    template.append("import org.springframework.stereotype.Component;\r\n");
    template.append("\r\n");
    template.append("@Component\r\n");
    template.append("@CopyDbProfile\r\n");
    template.append("public class ").append(entity).append("Processor implements ItemProcessor<").append(entity).append(", ").append(destEntity)
        .append("> {\r\n");
    template.append("\r\n");
    template.append("  @Autowired\r\n");
    template.append("  @Qualifier(value = \"dozerBeanMapper\")\r\n");
    template.append("  private DozerBeanMapper dozerBeanMapper;\r\n");
    template.append("\r\n");
    template.append("  @Override\r\n");
    template.append("  public ").append(destEntity).append(" process(").append(entity).append(" item) throws Exception {\r\n");
    template.append("    return ").append(generateDozerCopy(entity)).append(";\r\n");
    template.append("  }\r\n");
    template.append("}\r\n");
    String contents = String.format(template.toString());
    log.info("Item processor {}", contents);
    return contents;
  }

  /**
   * Generates the Spring Batch Item Writer template from the entity.
   * 
   * @param entity the JPA entity mapping
   * @param stepsPackage the package of the generated template
   * @return a Spring Batch Item Writer template class
   */
  public static String generateItemWriter(String entity, final String stepsPackage) {
    log.info("Generate Item witer for entity {}", entity);
    final String destEntity = toDest(entity);
    final StringBuilder template = new StringBuilder("package ").append(stepsPackage).append(";\r\n");
    template.append("\r\n");
    template.append("import com.sagag.services.copydb.config.CopyDbProfile;\r\n");
    template.append("import com.sagag.services.copydb.domain.dest.").append(destEntity).append(";\r\n");
    template.append("import com.sagag.services.copydb.repo.dest.").append(destEntity).append("Repository;\r\n");
    template.append(generateImportDbUtils(entity));
    template.append("\r\n");
    template.append("import org.apache.commons.collections4.CollectionUtils;\r\n");
    template.append(generateSessionImpl(entity));
    template.append("import org.springframework.batch.item.ItemWriter;\r\n");
    template.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
    template.append(generateQualifier(entity));
    template.append("import org.springframework.stereotype.Component;\r\n");
    template.append("\r\n");
    template.append("import java.util.List;\r\n");
    template.append("import java.util.Objects;\r\n");
    template.append("import java.util.stream.Collectors;\r\n");
    template.append("\r\n");
    template.append(genImportEntityManager(entity));

    template.append("@Component\r\n");
    template.append("@CopyDbProfile\r\n");
    template.append("public class ").append(destEntity).append("Writer implements ItemWriter<").append(destEntity).append("> {\r\n");
    template.append("\r\n");
    template.append("  @Autowired\r\n");
    template.append("  private ").append(destEntity).append("Repository ").append(WordUtils.uncapitalize(destEntity)).append("Repository;\r\n");
    template.append("\r\n");
    template.append(genAutowireEntityManagerIfTurnOnIdentityInsert(entity));
    template.append("  @Override\r\n");
    template.append("  public void write(List<? extends ").append(destEntity).append("> items) throws Exception {\r\n");
    template.append("    List<").append(destEntity).append("> nonNullsItems = items.stream().filter(Objects::nonNull).collect(Collectors.toList());\r\n");
    template.append("    if (CollectionUtils.isEmpty(nonNullsItems)) {\r\n");
    template.append("      return;\r\n");
    template.append("    }\r\n");
    template.append(genTurnOnIdentificationIf(entity));
    template.append("    ").append(WordUtils.uncapitalize(destEntity)).append("Repository.saveAll(nonNullsItems);\r\n");
    template.append("  }\r\n");
    template.append("}\r\n");

    final String contents = String.format(template.toString());
    log.info("Item writer {}", contents);
    return contents;
  }

  private static boolean hasNotId(final String table) {
    return "OfferPersonProperty".equalsIgnoreCase(table);
  }

  private static String generateImportDbUtils(String name) {
    if (hasNotId(name)) {
      return EMPTY;
    }
    return "import com.sagag.services.copydb.utils.DbUtils;\r\n";
  }

  private static String generateQualifier(String name) {
    if (hasNotId(name)) {
      return EMPTY;
    }
    return "import org.springframework.beans.factory.annotation.Qualifier;\r\n";
  }

  private static String generateSessionImpl(String name) {
    if (hasNotId(name)) {
      return EMPTY;
    }
    return "import org.hibernate.internal.SessionImpl;\r\n";
  }

  private static String genImportEntityManager(String name) {
    if (hasNotId(name)) {
      return EMPTY;
    }
    return new StringBuilder("import javax.persistence.EntityManager;\r\n").append("\r\n").toString();
  }

  private static String genAutowireEntityManagerIfTurnOnIdentityInsert(String name) {
    if (hasNotId(name)) {
      return EMPTY;
    }
    return new StringBuilder("  @Autowired\r\n").append("  @Qualifier(\"targetEntityManager\")\r\n").append("  private EntityManager targetEntityManager;\r\n")
        .append("\r\n").toString();
  }

  private static String genTurnOnIdentificationIf(String tableName) {
    if (!hasNotId(tableName)) {
      return new StringBuilder("    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();\r\n")
          .append("    DbUtils.turnOnIdentityInsert(session, \"").append(toTableNameFromJava(tableName)).append("\");\r\n").toString();
    } else {
      return EMPTY;
    }
  }

  private static final String toTableNameFromJava(String javaName) {
    Matcher m = COMPILE.matcher(javaName);

    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      m.appendReplacement(sb, new StringBuilder("_").append(m.group().toLowerCase()).toString());
    }
    m.appendTail(sb);
    return StringUtils.upperCase(sb.toString());
  }

  /**
   * Generates the Spring Batch Copy Job template for the entity.
   * 
   * @param entity the JPA entity mapping
   * @param stepsPackage the package of the generated template
   * @return a Spring Batch Batch Copy Job template class
   */
  public static String generateItemJobStep(String entity, final String stepsPackage) {
    final String destEntity = toDest(entity);
    final String stepName = new StringBuilder("copy").append(entity).toString();
    final String uncapitalizedName = WordUtils.uncapitalize(entity);
    final StringBuilder template = new StringBuilder("package ").append(stepsPackage).append(";\r\n");
    template.append("\r\n");
    template.append("import com.sagag.services.copydb.batch.AbstractJobConfig;\r\n");
    template.append("import com.sagag.services.copydb.config.CopyDbProfile;\r\n");
    template.append("import com.sagag.services.copydb.domain.src.").append(entity).append(";\r\n");
    template.append("import com.sagag.services.copydb.domain.dest.").append(destEntity).append(";\r\n");
    template.append("import com.sagag.services.copydb.utils.Constants;\r\n");
    template.append("\r\n");
    template.append("import org.springframework.batch.core.Step;\r\n");
    template.append("import org.springframework.batch.core.configuration.annotation.StepScope;\r\n");
    template.append("import org.springframework.batch.item.database.JpaPagingItemReader;\r\n");
    template.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
    template.append("import org.springframework.context.annotation.Bean;\r\n");
    template.append("import org.springframework.stereotype.Component;\r\n");
    template.append("\r\n");
    template.append("@Component\r\n");
    template.append("@CopyDbProfile\r\n");
    template.append("public class ").append(entity).append("CopySteps extends AbstractJobConfig {\r\n");
    template.append("\r\n");
    template.append("  @Autowired\r\n");
    template.append("  private ").append(entity).append("Processor ").append(uncapitalizedName).append("Processor;\r\n");
    template.append("\r\n");
    template.append("  @Autowired\r\n");
    template.append("  private ").append(destEntity).append("Writer ").append(uncapitalizedName).append("Writer;\r\n");
    template.append("\r\n");
    template.append("  @Bean(name = \"").append(stepName).append("\")\r\n");
    template.append("  public Step ").append(stepName).append("() {\r\n");
    template.append("    return stepBuilderFactory.get(\"").append(stepName).append("\").<").append(entity).append(", ").append(destEntity)
        .append(">chunk(DF_CHUNK)\r\n");
    template.append("        .reader(").append(uncapitalizedName).append("Reader())\r\n");
    template.append("        .processor(").append(uncapitalizedName).append("Processor)\r\n");
    template.append("        .writer(").append(uncapitalizedName).append("Writer)\r\n");
    template.append("        .transactionManager(targetTransactionManager)\r\n");
    template.append("        .build();\r\n");
    template.append("  }\r\n");
    template.append("\r\n");
    template.append("  @Bean\r\n");
    template.append("  @StepScope\r\n");
    template.append("  public JpaPagingItemReader<").append(entity).append("> ").append(uncapitalizedName).append("Reader() {\r\n");
    template.append("    final JpaPagingItemReader<").append(entity).append("> reader = new JpaPagingItemReader<>();\r\n");
    template.append("    reader.setTransacted(true);\r\n");
    template.append("    reader.setEntityManagerFactory(getEntityManagerFactory());\r\n");
    template.append("    reader.setPageSize(Constants.MAX_PAGE_SIZE);\r\n");
    template.append("    reader.setQueryString(\"select e from ").append(entity).append(" e\");\r\n");
    template.append("    reader.setName(\"").append(uncapitalizedName).append("Reader\");\r\n");
    template.append("    return reader;\r\n");
    template.append("  }\r\n");
    template.append("\r\n");
    template.append("}\r\n");

    final String contents = template.toString();
    log.info("CopySteps contents {}", contents);
    return contents;
  }

  /**
   * Generates the Spring Batch Main Copy Job template for all <code>entities</code>.
   * 
   * @param entities all the JPA entities mapping
   * @param stepsPackage the package of the generated template
   * @return a Spring Batch Batch Main Copy Job template class
   */
  public static String generateCopyDbJobConfig(List<String> entities, final String toPackage) {
    final StringBuilder template = new StringBuilder("package ").append(toPackage).append(";\r\n");
    template.append("\r\n");
    template.append("import com.sagag.services.copydb.batch.*;\r\n");
    template.append("import com.sagag.services.copydb.config.CopyDbProfile;\r\n");
    template.append("import com.sagag.services.copydb.steps.*;\r\n");
    template.append("\r\n");
    template.append("import org.springframework.batch.core.Job;\r\n");
    template.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
    template.append("import org.springframework.context.annotation.Bean;\r\n");
    template.append("import org.springframework.context.annotation.Configuration;\r\n");
    template.append("import org.springframework.context.annotation.DependsOn;\r\n");
    template.append("\r\n");
    template.append("import javax.transaction.Transactional;\r\n");
    template.append("\r\n");
    template.append("@Configuration\r\n");
    template.append("@CopyDbProfile\r\n");
    template.append("@DependsOn(value = \"copyDbBatchConfig\")\r\n");
    template.append("public class CopyDbJobConfig extends AbstractJobConfig {\r\n");
    template.append("\r\n");
    template.append(appendAutowiredTasks(entities, "CopySteps"));
    template.append("  @Bean(name = \"CopyDatabase\")\r\n");
    template.append("  @Transactional\r\n");
    template.append("  public Job createCopyDataBaseJob(JobNotificationListener listener) {\r\n");
    template.append("    return jobBuilderFactory.get(Jobs.COPY_DATABASE.getName())\r\n");
    template.append("        .listener(listener)\r\n");
    template.append(appendStepsFlow(entities));
    template.append("        .build();\r\n");
    template.append("  }\r\n");
    template.append("\r\n");
    template.append("  @Bean(name = \"DropBatchTables\")\r\n");
    template.append("  @Transactional\r\n");
    template.append("  public Job dropBatchTables(JobNotificationListener listener,\r\n");
    template.append("      DropBatchTablesTasklet dropBatchTablesTasklet) {\r\n");
    template.append("    return jobBuilderFactory.get(Jobs.DROP_BATCH_TABLES.getName())\r\n");
    template.append("        .listener(listener)\r\n");
    template.append("        .start(toStep(dropBatchTablesTasklet))\r\n");
    template.append("        .build();\r\n");
    template.append("  }\r\n");
    template.append("  \r\n");
    template.append("  @Bean(name = \"TruncateAllDestTables\")\r\n");
    template.append("  @Transactional\r\n");
    template.append("  public Job truncateAllDestTables(JobNotificationListener listener,\r\n");
    template.append("      TruncateAllDestTablesTasklet truncateAllDestTablesTasklet) {\r\n");
    template.append("    return jobBuilderFactory.get(Jobs.TRUNCATE_ALL_DEST_TABLES.getName())\r\n");
    template.append("        .listener(listener)\r\n");
    template.append("        .start(toStep(truncateAllDestTablesTasklet))\r\n");
    template.append("        .build();\r\n");
    template.append("  }\r\n");
    template.append("\r\n");
    template.append("}\r\n");

    final String contents = template.toString();
    log.info("CopyDbJobConfig contents {}", contents);
    return contents;
  }

  private static String appendAutowiredTasks(final List<String> entities, final String clazzName) {
    final StringBuilder sb = new StringBuilder();
    entities.stream().forEach(entity -> {
      final String lowerCaseName = WordUtils.uncapitalize(entity);
      sb.append("  @Autowired\r\n");
      sb.append("  private ").append(entity).append(clazzName).append(SPACE).append(lowerCaseName).append(clazzName).append(";\r\n");
      sb.append("  \r\n");
      if (!hasNotId(entity)) {
        sb.append("  @Autowired\r\n");
        sb.append("  private ").append(entity).append("TurnOffIdentityInsertion ").append(lowerCaseName).append("turnOffIdentityInsertion;\r\n");
        sb.append("\r\n");
      }
    });
    return sb.toString();
  }

  private static String appendStepsFlow(List<String> entities) {
    final StringBuilder sb = new StringBuilder();
    int i = 0;
    for (final String entity : entities) {
      final String lowerCaseName = WordUtils.uncapitalize(entity);
      if (i == 0) {
        sb.append("        .start(").append(lowerCaseName).append("CopySteps").append(".").append("copy").append(entity).append("())\r\n");
      } else {
        sb.append("        .next(").append(lowerCaseName).append("CopySteps").append(".").append("copy").append(entity).append("())\r\n");
      }
      if (!hasNotId(entity)) {
        sb.append("        .next(toStep(").append(lowerCaseName).append("turnOffIdentityInsertion))\r\n");
      }
      i++;
    }
    return sb.toString();
  }

  private static String appendSQLStepsFlow(List<String> entities) {
    final StringBuilder sb = new StringBuilder();
    int i = 0;
    for (final String entity : entities) {
      final String lowerCaseName = WordUtils.uncapitalize(entity);
      if (i == 0) {
        sb.append("        .start(toStep(").append(lowerCaseName).append("SQLCopiedTasklet))\r\n");
      } else {
        sb.append("        .next(toStep(").append(lowerCaseName).append("SQLCopiedTasklet))\r\n");
      }
      if (!hasNotId(entity)) {
        sb.append("        .next(toStep(").append(lowerCaseName).append("turnOffIdentityInsertion))\r\n");
      }
      i++;
    }
    return sb.toString();
  }

  /**
   * Generates the Spring Batch Task to turn off the ID insertion template for all <code>entities</code>.
   * <p>
   * During the copying data, we have to turn on the ID insertion for that table. After copying process finished, we have to turn off again.
   * 
   * @param entities all the JPA entities mapping
   * @param stepsPackage the package of the generated template
   * @return a Spring Batch Batch Task Job template class
   */
  public static Map<String, String> generateTurnOffIdInsertion(List<String> entities, final String stepsPackage) {
    final Map<String, String> allFilesContents = new HashMap<>(entities.size());
    entities.stream().forEach(entity -> {
      if (!hasNotId(entity)) {
        final StringBuilder template = new StringBuilder("package ").append(stepsPackage).append(";\r\n");
        template.append("\r\n");
        template.append("import org.hibernate.internal.SessionImpl;\r\n");
        template.append("import org.springframework.batch.core.StepContribution;\r\n");
        template.append("import org.springframework.batch.core.scope.context.ChunkContext;\r\n");
        template.append("import org.springframework.batch.repeat.RepeatStatus;\r\n");
        template.append("import org.springframework.stereotype.Component;\r\n");
        template.append("\r\n");
        template.append("import com.sagag.services.copydb.batch.AbstractTasklet;\r\n");
        template.append("import com.sagag.services.copydb.config.CopyDbProfile;\r\n");
        template.append("import com.sagag.services.copydb.utils.DbUtils;\r\n");
        template.append("\r\n");
        template.append("import lombok.EqualsAndHashCode;\r\n");
        template.append("\r\n");
        template.append("@Component\r\n");
        template.append("@CopyDbProfile\r\n");
        template.append("@EqualsAndHashCode(callSuper = false)\r\n");
        template.append("public class ").append(entity).append("TurnOffIdentityInsertion extends AbstractTasklet {\r\n");
        template.append("\r\n");
        template.append("  @Override\r\n");
        template.append("  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {\r\n");
        template.append("    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();\r\n");
        template.append("    DbUtils.turnOffIdentityInsert(session, \"").append(toTableNameFromJava(entity)).append("\");\r\n");
        template.append("    return finish(contribution);\r\n");
        template.append("  }");
        template.append("\r\n");
        template.append("}\r\n");
        allFilesContents.putIfAbsent(entity, template.toString());
      }
    });
    return allFilesContents;
  }

  /**
   * Generates the Spring Batch Task to drop all artefact tables generated of the main Job.
   * <p>
   * During the copying data main Job, Spring Batch generated the artefact tables with prefix 'BATCH-' to track the errors. We need to delete them after the job
   * for some cases.
   * 
   * @param artefactsTables all the artefact tables generated
   * @param stepsPackage the package of the generated template
   * @return a Spring Batch Batch Task Job template class
   */
  public static String generateDropBatchTables(List<String> artefactsTables, final String stepsPackage) {
    final StringBuilder template = new StringBuilder("package ").append(stepsPackage).append(";\r\n");
    template.append("\r\n");
    template.append("import com.sagag.services.copydb.batch.AbstractTasklet;\r\n");
    template.append("import com.sagag.services.copydb.config.CopyDbProfile;\r\n");
    template.append("import com.sagag.services.copydb.utils.DbUtils;\r\n");
    template.append("\r\n");
    template.append("import org.hibernate.internal.SessionImpl;\r\n");
    template.append("import org.springframework.batch.core.StepContribution;\r\n");
    template.append("import org.springframework.batch.core.scope.context.ChunkContext;\r\n");
    template.append("import org.springframework.batch.repeat.RepeatStatus;\r\n");
    template.append("import org.springframework.stereotype.Component;\r\n");
    template.append("\r\n");
    template.append("import java.util.Arrays;\r\n");
    template.append("\r\n");
    template.append("import lombok.EqualsAndHashCode;\r\n");
    template.append("\r\n");
    template.append("@Component\r\n");
    template.append("@CopyDbProfile\r\n");
    template.append("@EqualsAndHashCode(callSuper = false)\r\n");
    template.append("public class DropBatchTablesTasklet extends AbstractTasklet {\r\n");
    template.append("\r\n");
    template.append("  @Override\r\n");
    template.append("  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {\r\n");
    template.append("    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();\r\n");
    template.append("    final String[] batchTables = new String[] {\r\n");
    template.append(appendArrayTables(artefactsTables));
    template.append("    };\r\n");
    template.append("    DbUtils.dropTables(session, Arrays.asList(batchTables));\r\n");
    template.append("    return finish(contribution);\r\n");
    template.append("  }\r\n");
    template.append("}\r\n");

    final String contents = template.toString();
    log.info("DropBatchTables contents {}", contents);
    return contents;
  }

  private static String appendArrayTables(List<String> tables) {
    final StringBuilder sb = new StringBuilder();
    tables.forEach(tb -> sb.append("        \"").append(tb).append("\",\r\n"));
    return sb.toString();
  }

  /**
   * Generates the Spring Batch Task to delete all data in all tables.
   * <p>
   * To reset all destination tables to be empty.
   * 
   * @param tables all the tables to delete its data
   * @param stepsPackage the package of the generated template
   * @return a Spring Batch Batch Task Job template class
   */
  public static String generateTruncateAllDestTables(List<String> tables, final String stepsPackage) {
    final StringBuilder template = new StringBuilder("package ").append(stepsPackage).append(";\r\n");
    template.append("\r\n");
    template.append("import com.sagag.services.copydb.batch.AbstractTasklet;\r\n");
    template.append("import com.sagag.services.copydb.config.CopyDbProfile;\r\n");
    template.append("import com.sagag.services.copydb.utils.DbUtils;\r\n");
    template.append("\r\n");
    template.append("import org.hibernate.internal.SessionImpl;\r\n");
    template.append("import org.springframework.batch.core.StepContribution;\r\n");
    template.append("import org.springframework.batch.core.scope.context.ChunkContext;\r\n");
    template.append("import org.springframework.batch.repeat.RepeatStatus;\r\n");
    template.append("import org.springframework.stereotype.Component;\r\n");
    template.append("\r\n");
    template.append("import java.util.Arrays;\r\n");
    template.append("\r\n");
    template.append("import lombok.EqualsAndHashCode;\r\n");
    template.append("\r\n");
    template.append("@Component\r\n");
    template.append("@CopyDbProfile\r\n");
    template.append("@EqualsAndHashCode(callSuper = false)\r\n");
    template.append("public class TruncateAllDestTablesTasklet extends AbstractTasklet {\r\n");
    template.append("\r\n");
    template.append("  @Override\r\n");
    template.append("  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {\r\n");
    template.append("    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();\r\n");
    template.append("    final String[] tables = new String[] {\r\n");
    List<String> reversedTables = new ArrayList<>(tables); // reverse to release the constraints
    Collections.reverse(reversedTables);
    template.append(appendArrayTables(reversedTables));
    template.append("    };\r\n");
    template.append("    DbUtils.truncateTables(session, Arrays.asList(tables));\r\n");
    template.append("    return finish(contribution);\r\n");
    template.append("  }\r\n");
    template.append("}\r\n");

    final String contents = template.toString();
    log.info("TruncateAllDestTables contents {}", contents);
    return contents;
  }

  public static String toDest(final String entity) {
    return new StringBuilder("Dest").append(entity).toString();
  }

  private static String generateDozerCopy(final String entity) {
    return new StringBuilder("dozerBeanMapper.map(item, ").append(toDest(entity)).append(".class)").toString();
  }

  /**
   * Generates the Spring Batch Task to copy a table by pure SQL.
   * 
   * @param tables all the tables and its columns to generate copied tasks
   * @param jobPackage the package of the generated template
   * @return a list of Spring Batch Batch Task Job template class
   */
  public static final Map<String, String> generateSQLCopiedTasks(final List<String> tables, final Map<String, List<String>> tablesWithColumns,
      String fromSchema, String toSchema, final String jobPackage) {
    final Map<String, String> allFilesContents = new HashMap<>(tables.size());
    tables.stream().forEach(table -> {
      final StringBuilder template = new StringBuilder("package ").append(jobPackage).append(";\r\n");
      template.append("\r\n");
      template.append("import com.sagag.services.copydb.batch.AbstractTasklet;\r\n");
      template.append("import com.sagag.services.copydb.config.CopyDbProfile;\r\n");
      template.append("import com.sagag.services.copydb.utils.DbUtils;\r\n");
      template.append("\r\n");
      template.append("import org.hibernate.internal.SessionImpl;\r\n");
      template.append("import org.springframework.batch.core.StepContribution;\r\n");
      template.append("import org.springframework.batch.core.scope.context.ChunkContext;\r\n");
      template.append("import org.springframework.batch.repeat.RepeatStatus;\r\n");
      template.append("import org.springframework.stereotype.Component;\r\n");
      template.append("\r\n");
      template.append("import lombok.EqualsAndHashCode;\r\n");
      template.append("\r\n");
      template.append("@Component\r\n");
      template.append("@CopyDbProfile\r\n");
      template.append("@EqualsAndHashCode(callSuper = false)\r\n");
      final String tableEntityName = SkeletonGeneratorService.toJavaPojoNamesFromTableNames(table);
      final String copiedColumns = buildCopiedColumns(table, tablesWithColumns.get(table)).toString();
      String toColumns = copiedColumns; // most of the case, copied the same columns
      if ("USER_SETTINGS".equalsIgnoreCase(table)) { // due to # 4253
        toColumns = toColumns.replace("HAS_PARTNER_PROGRAM_VIEW", "SHOW_HAPPY_POINTS").replace("HAS_PARTNER_PROGRAM_LOGIN", "ACCEPT_HAPPY_POINT_TERM");
      }
      log.info("generateSQLCopiedTasks -> {}, {}, {}", table, tableEntityName, copiedColumns);
      template.append("public class ").append(tableEntityName).append("SQLCopiedTasklet").append(" extends AbstractTasklet {\r\n");
      template.append("\r\n");
      template.append("  @Override\r\n");
      template.append("  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {\r\n");
      template.append("    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();\r\n");
      template.append("    final String fromTable = \"").append(fromSchema).append(table).append("\";\r\n");
      template.append("    final String toTable = \"").append(toSchema).append(table).append("\";\r\n");
      template.append("    final String copiedColumns = \"").append(copiedColumns).append("\";\r\n");
      template.append("    final String toColumns = \"").append(toColumns).append("\";\r\n");
      if (!hasNotId(tableEntityName)) {
        template.append("    DbUtils.turnOnIdentityInsert(session, \"").append(table).append("\");\r\n");
      }
      template.append("    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);\r\n");
      template.append("    return finish(contribution);\r\n");
      template.append("  }\r\n");
      template.append("}\r\n");

      allFilesContents.putIfAbsent(tableEntityName, template.toString());
    });
    return allFilesContents;
  }

  private static StringBuilder buildCopiedColumns(final String originalTable, final List<String> copiedColumns) {
    final StringBuilder columnsBuilder = new StringBuilder("");
    if (CollectionUtils.isEmpty(copiedColumns)) {
      return columnsBuilder;
    }
    if ("CUSTOMER_SETTINGS".equalsIgnoreCase(originalTable)) {
      copiedColumns.remove("HAS_PARTNER_PROGRAM_VIEW");// # 4905: removed column HAS_PARTNER_PROGRAM_VIEW in CUSTOMER_SETTINGS
    }
    copiedColumns.stream().forEach(column -> columnsBuilder.append(column).append(","));
    return columnsBuilder.deleteCharAt(columnsBuilder.length() - 1);
  }

  /**
   * Generates the Spring Batch Main Copy SQL Job template for all <code>entities</code>.
   * 
   * @param entities all the JPA entities mapping
   * @param stepsPackage the package of the generated template
   * @return a Spring Batch Batch Main Copy Job template class
   */
  public static String generateCopySQLDbJobConfig(List<String> entities, final String toPackage) {
    final StringBuilder template = new StringBuilder("package ").append(toPackage).append(";\r\n");
    template.append("\r\n");
    template.append("import com.sagag.services.copydb.batch.*;\r\n");
    template.append("import com.sagag.services.copydb.batch.tasks.*;\r\n");
    template.append("import com.sagag.services.copydb.config.CopyDbProfile;\r\n");
    template.append("import com.sagag.services.copydb.steps.*;\r\n");
    template.append("\r\n");
    template.append("import org.springframework.batch.core.Job;\r\n");
    template.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
    template.append("import org.springframework.context.annotation.Bean;\r\n");
    template.append("import org.springframework.context.annotation.Configuration;\r\n");
    template.append("import org.springframework.context.annotation.DependsOn;\r\n");
    template.append("\r\n");
    template.append("import javax.transaction.Transactional;\r\n");
    template.append("\r\n");
    template.append("@Configuration\r\n");
    template.append("@CopyDbProfile\r\n");
    template.append("@DependsOn(value = \"copyDbBatchConfig\")\r\n");
    template.append("public class CopyDbSQLJobConfig extends AbstractJobConfig {\r\n");
    template.append("\r\n");
    template.append(appendAutowiredTasks(entities, "SQLCopiedTasklet"));
    template.append("  @Bean(name = \"CopyDatabaseUsingSQL\")\r\n");
    template.append("  @Transactional\r\n");
    template.append("  public Job createSQLCopyDataBaseJob(JobNotificationListener listener) {\r\n");
    template.append("    return jobBuilderFactory.get(Jobs.COPY_DATABASE_BY_SQL.getName())\r\n");
    template.append("        .listener(listener)\r\n");
    template.append(appendSQLStepsFlow(entities));
    template.append("        .build();\r\n");
    template.append("  }\r\n");
    template.append("\r\n");
    template.append("}\r\n");

    final String contents = template.toString();
    log.info("CopyDbJobConfig contents {}", contents);
    return contents;
  }

}
