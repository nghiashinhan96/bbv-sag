package com.sagag.services.common.external;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.ftp.EshopFtpClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FtpPriceFileListProcessor {

  private static final DateTimeFormatter DF_PRICE_FILE_DATE_TIME_FOMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd");

  private static final String KEY_FILE_PATTERN = "customerNr";

  private static final String FILE_PATTERN = "PF-${customerNr}-";

  private static final String SUPPORTED_FILE_FORMAT = ".csv";

  @Value("${external.webservice.ftp.price-file-dir:}")
  private String priceFileDir;

  @Autowired
  private EshopFtpClient ftpClient;

  /**
   * Returns the list of price files of customer by number.
   *
   * @param customerNr the customer number
   * @return the list of file info.
   * @throws IOException
   */
  public List<FtpFileInfoDto> findPriceFilesByCustomerNr(String customerNr)
      throws IOException {
    if (StringUtils.isAnyBlank(priceFileDir, customerNr)) {
      return Collections.emptyList();
    }

    final Map<String, String> valueMap = new HashMap<>();
    valueMap.putIfAbsent(KEY_FILE_PATTERN, customerNr);
    final String customerFilePattern = StrSubstitutor.replace(FILE_PATTERN, valueMap);
    final String directory = StringUtils.join(new String[] { priceFileDir, customerNr },
        SagConstants.SLASH);
    return ftpClient.listFiles(directory, fileFilter(customerFilePattern)).stream()
        .sorted(sortPriceFileDateByDesc(customerFilePattern)).collect(Collectors.toList());
  }

  private static FTPFileFilter fileFilter(String customerFilePattern) {
    return ftpFile -> ftpFile.isFile()
        && StringUtils.startsWith(ftpFile.getName(), customerFilePattern)
        && StringUtils.endsWith(ftpFile.getName(), SUPPORTED_FILE_FORMAT);
  }

  private static Comparator<FtpFileInfoDto> sortPriceFileDateByDesc(String customerFilePattern) {
    return (item1, item2) -> extractLocalDateFromFileName(customerFilePattern).apply(item2)
        .compareTo(extractLocalDateFromFileName(customerFilePattern).apply(item1));
  }

  private static Function<FtpFileInfoDto, LocalDate> extractLocalDateFromFileName(
      String customerFilePattern) {
    return item -> {
      String dateStr = StringUtils.remove(
          StringUtils.remove(item.getFileName(), customerFilePattern), SUPPORTED_FILE_FORMAT);
      return LocalDate.parse(dateStr, DF_PRICE_FILE_DATE_TIME_FOMATTER);
    };
  }
}
