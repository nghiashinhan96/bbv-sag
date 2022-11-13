package com.sagag.services.common.external;

import lombok.Data;

import java.util.Date;

@Data
public class FtpFileInfoDto {

  private String fileName;

  private String fileUrl;

  private Date modifiedDate;
}
