package com.sagag.services.common.external;

import com.sagag.services.common.CommonApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CommonApplication.class })
@EshopIntegrationTest
@Slf4j
public class FtpPriceFileListProcessorIT {

  @Autowired
  private FtpPriceFileListProcessor processor;

  @Test
  public void test() throws IOException {
    List<FtpFileInfoDto> files = processor.findPriceFilesByCustomerNr("1000");
    files.forEach(file -> log.debug("File Info = {} - File URL = {}",
        file.getFileName(), file.getFileUrl()));
  }
}
