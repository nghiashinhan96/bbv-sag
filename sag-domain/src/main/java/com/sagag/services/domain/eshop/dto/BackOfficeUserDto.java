package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackOfficeUserDto {
  private UserSettingsDto userSettingsDto;

  private String companyName;
  private String orgCode;
  private String email;
  private String telephone;
  private String hourlyRate;

  private List<EshopRoleDto> types;
  private List<LanguageDto> languageDtos;
  private List<SalutationDto> salutationDtos;

  private List<PaymentMethodDto> paymentMethod;
  private List<CollectiveDeliveryDto> collectiveDelivery;
  private List<DeliveryTypeDto> deliveryTypes;
  private List<InvoiceTypeDto> invoiceTypes;

  private int salutationId;
  private int languageId;
  private int typeId;
  private String userName;

  private String firstName;
  private String lastName;
  private String fax;

  private String externalCustomerName;
  private String externalUserName;

  private Boolean isUserActive;
  private String affiliate;
  private String type;
}
