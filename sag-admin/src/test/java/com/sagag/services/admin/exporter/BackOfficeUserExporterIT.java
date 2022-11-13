package com.sagag.services.admin.exporter;

import com.google.common.collect.Lists;
import com.sagag.services.admin.app.AdminApplication;
import com.sagag.services.admin.exception.UserExportException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.domain.eshop.backoffice.dto.ExportingUserDto;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * IT for Back office User exporter.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AdminApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore("Just verify at local machine instead CI/CD instance")
public class BackOfficeUserExporterIT {

  private static final String DEST_DIR = "D:/dev/export_result";

  @Autowired
  private BackOfficeUserExporter exporter;

  @Test
  public void shouldExportAllUsers() throws IOException, UserExportException {
    final ExportStreamedResult result =
        exporter.exportExcel(new BackOfficeUserExportCriteria(getExportingUsers()));

    final File exportedFile = new File(DEST_DIR, result.getFileName());
    exportedFile.createNewFile();
    FileUtils.writeByteArrayToFile(exportedFile, result.getContent());
  }

  private static List<ExportingUserDto> getExportingUsers() {

    ExportingUserDto user1 = new ExportingUserDto();
    user1.setCustomerNumber("customer number 1");
    user1.setDvseCustomerName("dvse customer name 1");
    user1.setDvseUserName("dvse user name 1");
    user1.setUserName("user name 1");
    user1.setFirstName("first name 1");
    user1.setLastName("last name 1");
    user1.setEmail("email 1");
    user1.setZip("zip 1");

    return Lists.newArrayList(user1);
  }
}
