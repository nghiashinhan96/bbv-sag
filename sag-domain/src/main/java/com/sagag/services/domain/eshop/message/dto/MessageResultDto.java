package com.sagag.services.domain.eshop.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResultDto implements Serializable {

  private static final long serialVersionUID = -3247142961682714800L;

  private long id;

  private String title;

  private boolean active;

  private boolean ssoTraining;

  private int locationTypeId;

  private String affiliateShortName;

  private MessageLocationDto messageLocation;

  private String customerNr;

  private int roleTypeId;

  private int accessRightId;

  private int typeId;

  private int visibilityId;

  private int styleId;

  private int areaId;

  private int subAreaId;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date dateValidFrom;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date dateValidTo;

  private List<MessageLanguageDto> messageLanguages;
}
