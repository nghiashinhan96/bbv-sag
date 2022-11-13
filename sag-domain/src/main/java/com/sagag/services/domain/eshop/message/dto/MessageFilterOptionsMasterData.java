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
public class MessageFilterOptionsMasterData implements Serializable {

  private static final long serialVersionUID = 2177148116417650704L;

  private List<MessageTypeDto> types;

  private List<MessageAreaDto> areas;
}
