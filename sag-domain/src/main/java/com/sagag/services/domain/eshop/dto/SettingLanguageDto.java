package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingLanguageDto implements Serializable {

  private static final long serialVersionUID = 5798224135921596956L;

  private String langIso;

  private String content;

  public String getLangIso() {
    return StringUtils.trimToEmpty(langIso).toUpperCase();
  }
}
