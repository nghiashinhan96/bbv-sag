package com.sagag.services.domain.eshop.message.dto;

import com.sagag.services.domain.eshop.dto.LanguageDto;

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
public class MessageMasterDataDto implements Serializable{

  private static final long serialVersionUID = 3382926349737907281L;

  private List<MessageLocationTypeDto> locationTypes;

  private List<MessageTypeDto> types;

  private List<MessageStyleDto> styles;

  private List<MessageVisibilityDto> visibilities;

  private List<SupportedAffiliateDto> supportedAffiliates;

  private List<LanguageDto> supportedLanguages;
}
