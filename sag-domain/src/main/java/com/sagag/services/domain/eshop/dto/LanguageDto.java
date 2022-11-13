package com.sagag.services.domain.eshop.dto;

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
  private String langcode;
  private String langiso;
  private String description;
}
