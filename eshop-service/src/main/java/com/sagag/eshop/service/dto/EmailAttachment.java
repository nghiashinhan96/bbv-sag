package com.sagag.eshop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class EmailAttachment implements Serializable {

  private static final long serialVersionUID = 4462045002131051947L;
  private String name;
  private ByteArrayResource content;

  private static EmailAttachment fromMultipartFile(MultipartFile file) throws IOException {
    return new EmailAttachment(file.getOriginalFilename(),
        new ByteArrayResource(IOUtils.toByteArray(file.getInputStream())));
  }

  public static EmailAttachment[] fromMultipartFiles(List<MultipartFile> files) throws IOException {
    if (CollectionUtils.isEmpty(files)) {
      return ArrayUtils.toArray();
    }
    int size = files.size();
    EmailAttachment[] attachments = new EmailAttachment[size];
    for (int i = 0; i <= size - 1; i++) {
      attachments[i] = fromMultipartFile(files.get(i));
    }
    return attachments;
  }
}
