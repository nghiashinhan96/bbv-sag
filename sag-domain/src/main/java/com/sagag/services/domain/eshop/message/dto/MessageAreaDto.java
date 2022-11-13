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
public class MessageAreaDto implements Serializable {

  private static final long serialVersionUID = -205641545821960348L;

  private int id;

  private String area;

  private String description;

  private List<MessageSubAreaDto> subAreas;
}
