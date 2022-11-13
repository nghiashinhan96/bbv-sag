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
public class MessageLocationTypeDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private int id;

  private String locationType;

  private String description;

  private List<MessageRoleTypeDto> roleTypes;
}
