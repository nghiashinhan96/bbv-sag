package com.sagag.services.tools.domain.target;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto implements Serializable {
  private static final long serialVersionUID = 1L;
  private int id;

  private String langCode;
  private String langiso;
  private String description;
}
