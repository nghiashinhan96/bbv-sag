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
public class MessageTypeDto implements Serializable{

  private static final long serialVersionUID = 1L;

  private int id;

  private String type;

  private String description;
}
