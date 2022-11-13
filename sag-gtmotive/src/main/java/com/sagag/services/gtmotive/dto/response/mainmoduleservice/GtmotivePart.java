package com.sagag.services.gtmotive.dto.response.mainmoduleservice;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

@Data
public class GtmotivePart {

  private String partCode;
  private String partDescription;
  private String functionalGroup;
  private String functionalGroupDescription;

  public boolean isValidItem() {
    return StringUtils.isNoneBlank(partCode, partDescription, functionalGroup,
        functionalGroupDescription);
  }
}
