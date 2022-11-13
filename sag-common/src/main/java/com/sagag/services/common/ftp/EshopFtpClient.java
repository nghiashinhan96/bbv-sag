package com.sagag.services.common.ftp;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.config.FtpClientConfiguration;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.external.FtpFileInfoDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class EshopFtpClient {

  @Autowired
  private FtpClientConfiguration ftpConfig;

  @LogExecutionTime
  public String uploadFile(String workingDirectory, String fileName, MultipartFile file)
      throws IOException {
    checkFTPConfiguration();
    Assert.hasText(workingDirectory, "The given working directory must not be empty");
    Assert.hasText(fileName, "The given file name must not be empty");
    Assert.notNull(file, "The given file must not null");
    FTPClient ftpClient = openFtpClient();
    try (InputStream stream = file.getInputStream()) {
      ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
      ftpClient.changeWorkingDirectory(workingDirectory);
      if (!ftpClient.storeFile(fileName, stream)) {
        throw new IOException(String.format("Failed to upload file to ftp, response: %s",
            ftpClient.getReplyString()));
      }
    } finally {
      closeFtpClient(ftpClient);
    }
    return buildUrlAccess(workingDirectory, fileName);
  }

  @LogExecutionTime
  public boolean deleteFile(String workingDirectory, String fileName) throws IOException {
    checkFTPConfiguration();
    Assert.hasText(fileName, "The given file name must not be empty");
    FTPClient ftpClient = openFtpClient();
    ftpClient.changeWorkingDirectory(workingDirectory);
    log.debug("Changed directory, response: {}", ftpClient.getReplyString());
    try {
      return ftpClient.deleteFile(fileName);
    } catch (IOException ex) {
      throw new IOException("Failed to delete file");
    } finally {
      closeFtpClient(ftpClient);
    }
  }

  @LogExecutionTime
  public List<FtpFileInfoDto> listFiles(String workingDirectory, FTPFileFilter fileFilter)
      throws IOException {
    checkFTPConfiguration();
    if (StringUtils.isBlank(workingDirectory)) {
      return Collections.emptyList();
    }
    final FTPClient ftpClient = openFtpClient();
    final FTPFile[] ftpFiles;
    try {
      if (fileFilter != null) {
        ftpFiles = ftpClient.listFiles(workingDirectory, fileFilter);
      } else {
        ftpFiles = ftpClient.listFiles(workingDirectory);
      }
    } catch (IOException ex) {
      throw new IOException("Failed to list files");
    } finally {
      closeFtpClient(ftpClient);
    }

    return Stream.of(ftpFiles).map(ftpFileInfoConverter(workingDirectory))
        .collect(Collectors.toList());
  }

  private Function<FTPFile, FtpFileInfoDto> ftpFileInfoConverter(String workingDirectory) {
    return ftpFile -> {
      FtpFileInfoDto item = new FtpFileInfoDto();
      item.setFileName(ftpFile.getName());
      item.setFileUrl(buildUrlAccess(workingDirectory, item.getFileName()));
      item.setModifiedDate(ftpFile.getTimestamp().getTime());
      return item;
    };
  }

  private void checkFTPConfiguration() {
    Assert.isTrue(ftpConfig.hasFtpConfig(), "Ftp client not config");
  }

  private FTPClient openFtpClient() throws IOException {
    if (!ftpConfig.hasFtpConfig()) {
      throw new IOException("ftpClient is not config");
    }

    FTPClient ftpClient = new FTPClient();
    ftpClient.setDataTimeout(ftpConfig.getTimeout());
    ftpClient.connect(ftpConfig.getUpload(), ftpConfig.getPort());
    log.debug("Opened connection, response: {}", ftpClient.getReplyString());

    ftpClient.login(ftpConfig.getUser(), ftpConfig.getPass());

    log.debug("Logined, response: {}", ftpClient.getReplyString());
    ftpClient.enterLocalPassiveMode();
    return ftpClient;
  }

  private void closeFtpClient(FTPClient ftpClient) throws IOException {
    ftpClient.logout();
    log.debug("Logout, response: {}", ftpClient.getReplyString());
    ftpClient.disconnect();
    log.debug("Disconnected, response: {} ", ftpClient.getReplyString());
  }

  private String buildUrlAccess(String workingDir, String fileName) {
    final String[] infos = new String[] { ftpConfig.getView(), ftpConfig.getParentDir(), workingDir,
        fileName };
    return StringUtils.join(infos, SagConstants.SLASH);
  }
}
