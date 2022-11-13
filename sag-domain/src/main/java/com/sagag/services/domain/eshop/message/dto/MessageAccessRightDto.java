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
public class MessageAccessRightDto implements Serializable {

  private static final long serialVersionUID = 4579924051576775895L;

  private int id;

  private String userGroup;

  private String userGroupKey;

  private String description;

  private List<MessageAreaDto> areas;
}
