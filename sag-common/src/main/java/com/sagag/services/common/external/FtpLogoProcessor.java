package com.sagag.services.common.external;

import com.sagag.services.common.config.FtpClientConfiguration;
import com.sagag.services.common.ftp.EshopFtpClient;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Component
public class FtpLogoProcessor implements IUploadProcessor {

  private static final String LOGO_PATH = "collections";

  private static final String LOGO_SETTING_KEY = "logo_image";

  // Hardcoded default logo name to prevent delete them from FTP server
  private static final String[] LOCKED_FILE =
      new String[]{ "logo-der.png", "logo-der-at.png", "logo-mat.png", "logo-matik.png", "logo-matik-at.png",
          "logo-mch.png", "logo-rbe.png", "logo-tech.png", "logo-wbb.jpg", "technomag_logo.png",
          "derendinger_logo.png" };

  @Autowired
  private FtpClientConfiguration ftpConfig;

  @Autowired
  private EshopFtpClient ftpClient;

  @Override
  public Optional<FileUploadDto> upload(MultipartFile file) throws IOException {
    final String fileName = buildFileName(FilenameUtils.getExtension(file.getOriginalFilename()));
    final String urlAccess = ftpClient.uploadFile(LOGO_PATH, fileName, file);
    return Optional.of(FileUploadDto.builder().key(LOGO_SETTING_KEY).url(urlAccess).build());
  }

  @Override
  public void remove(String filename) throws IOException {
    Assert.hasText(filename, "filename must not be empty");
    Assert.isTrue(ftpConfig.hasFtpConfig(), "Ftp client not config");
    if (ArrayUtils.contains(LOCKED_FILE, filename)) {
      return;
    }
    ftpClient.deleteFile(LOGO_PATH, filename);
  }

  @Override
  public boolean isSupport(FileUploadType type) {
    return FileUploadType.COLLECTION_LOGO == type;
  }
}
