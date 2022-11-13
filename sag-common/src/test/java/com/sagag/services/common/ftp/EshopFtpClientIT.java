package com.sagag.services.common.ftp;

import com.sagag.services.common.CommonApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.external.FtpFileInfoDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFileFilter;
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
public class EshopFtpClientIT {

  @Autowired
  private EshopFtpClient ftpClient;

  @Test
  public void test() throws IOException {
    final List<FtpFileInfoDto> files = ftpClient.listFiles("eh-ch/1000",
        fileFilter());
    files.forEach(file -> log.debug("File Info = {} - File Url = {} - Date modified = {}",
        file.getFileName(), file.getFileUrl(), file.getModifiedDate()));
  }

  private FTPFileFilter fileFilter() {
    return ftpFile -> ftpFile.isFile() && StringUtils.endsWith(ftpFile.getName(), ".csv");
  }

}
