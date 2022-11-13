package com.sagag.services.tools.analysis;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvPerson;
import com.sagag.services.tools.domain.source.SourcePerson;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.source.SourcePersonRepository;
import com.sagag.services.tools.support.Folders;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
@OracleProfile
@Slf4j
public class ExportPersonDataFromOracleDBTasklet extends AbstractTasklet{

  private static final String FILE_NAME = "PERSON_";

  @Value("${page.start:0}")
  private int startPage;

  @Autowired(required = false)
  private SourcePersonRepository sourcePersonRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {

    int size = ToolConstants.MAX_SIZE;
    long total = sourcePersonRepo.count();
    int totalPage = (int) total / size;

    log.info("Total record = {} - total page = {}", total, totalPage);
    for (int curPage = startPage; curPage < totalPage; curPage++) {
      final Page<SourcePerson> personPage = sourcePersonRepo.findAll(PageRequest.of(curPage, size));
      if (!personPage.hasContent()) {
        continue;
      }
      final List<CsvPerson> csvPeople = new ArrayList<>();
      personPage.forEach(person -> csvPeople.add(CsvPerson.of(person)));

      final String filePath = Folders.EXPORT_PERSON_DIR_PATH.getPath() +
        FILE_NAME + curPage + ToolConstants.CSV_SUFFIX;
      CsvUtils.write(filePath, csvPeople);
    }

    return finish(contribution);
  }
}
