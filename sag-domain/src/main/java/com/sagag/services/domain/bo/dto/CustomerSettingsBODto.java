package com.sagag.services.domain.bo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;

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
@JsonPropertyOrder({ "id", "allocationId", "paymentMethod", "deliveryId",
    "collectiveDelivery", "sessionTimeoutSeconds", "emailNotificationOrder",
    "perms"})
public class CustomerSettingsBODto implements Serializable {

  private static final long serialVersionUID = -7550926489890016956L;

  private int id;

  private int allocationId;

  private int paymentId;

  private int deliveryId;

  private int collectiveDelivery;

  private Integer sessionTimeoutSeconds;

  private boolean emailNotificationOrder;

  private List<PermissionConfigurationDto> perms;

  private int orgId;

  private boolean showOciVat;

  private List<OrganisationCollectionDto> collections;

  private String collectionShortName;

  private boolean demoCustomer;

  private boolean normautoDisplay;

  private boolean hasPartnerprogramView;

  private boolean netPriceView;

  private boolean showDiscount;

  private Integer wssDeliveryId;
}
