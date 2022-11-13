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
public class MessageSavingRequestDto implements Serializable {

  private static final long serialVersionUID = -8809711288178249880L;

  private String title;

  private Integer locationTypeId;

  private String locationValue;

  private MessageLocationDto messageLocation;

  private Integer accessRightId;

  private Integer typeId;

  private Integer subAreaId;

  private Integer styleId;

  private Integer visibilityId;

  private Boolean active;

  private String dateValidFrom;

  private String dateValidTo;

  private Boolean ssoTraining;

  private List<MessageLanguageDto> messageLanguages;

}
