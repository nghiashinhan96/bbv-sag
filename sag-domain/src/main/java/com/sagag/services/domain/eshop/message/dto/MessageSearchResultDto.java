package com.sagag.services.domain.eshop.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSearchResultDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private long id;

  private String title;

  private String type;

  private String area;

  private String subArea;

  private String locationValue;

  private boolean active;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date dateValidFrom;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date dateValidTo;
}
