package com.sagag.services.domain.eshop.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageLanguageDto implements Serializable {

  private static final long serialVersionUID = 5798224135921596956L;

  private String langIso;

  private String content;
}
