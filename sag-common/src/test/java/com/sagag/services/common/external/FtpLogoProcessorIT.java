package com.sagag.services.common.external;

import com.sagag.services.common.CommonApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.apache.commons.io.FilenameUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CommonApplication.class })
@EshopIntegrationTest
public class FtpLogoProcessorIT {

  @Autowired
  private FtpLogoProcessor processor;

  @Test
  public void shouldCreateAndDeleteSucess() throws IOException {
    Assert.assertThat(processor.isSupport(FileUploadType.COLLECTION_LOGO), Matchers.is(true));
    MultipartFile mockFile = new MockMultipartFile("file", "test.jpg",
        "image/jpeg", "test image content".getBytes());
    FileUploadDto createdFile = processor.upload(mockFile).get();
    Assert.assertThat(createdFile.getKey(), Matchers.is("logo_image"));
    processor.remove(FilenameUtils.getName(createdFile.getUrl()));
  }
}
