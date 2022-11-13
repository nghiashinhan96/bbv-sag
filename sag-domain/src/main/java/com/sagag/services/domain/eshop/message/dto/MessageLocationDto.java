package com.sagag.services.domain.eshop.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageLocationDto implements Serializable {

  private static final long serialVersionUID = 136272921290308574L;

  private Integer locationTypeId;
  private List<String> locationValues;

}
