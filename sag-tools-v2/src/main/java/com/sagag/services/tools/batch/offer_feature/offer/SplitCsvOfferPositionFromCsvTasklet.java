package com.sagag.services.tools.batch.offer_feature.offer;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvStringOfferPosition;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.utils.CsvUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@Slf4j
@OracleProfile
public class SplitCsvOfferPositionFromCsvTasklet extends AbstractTasklet {

  @Value("${file.csv.offer:${user.dir}/csv/import/OFFERPOSITION.csv}")
  private String offerPositionFilePath;

  @Value("${csv.separtor:;}")
  private char separator;

  @Value("${file.dir.offer:${user.dir}/csv/position/position_}")
  private String offerPositionDirPath;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {

    File file = new File(offerPositionFilePath);
    List<CsvStringOfferPosition> positions =
      CsvUtils.read(file, separator, CsvStringOfferPosition.class);
    if (CollectionUtils.isEmpty(positions)) {
      log.warn("No any splitters is executed");
      return finish(contribution);
    }

    CsvUtils.split(offerPositionDirPath, positions);

    return finish(contribution);
  }
}
