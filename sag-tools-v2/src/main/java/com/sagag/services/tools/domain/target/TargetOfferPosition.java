package com.sagag.services.tools.domain.target;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "OFFER_POSITION")
public class TargetOfferPosition implements Serializable {

  private static final long serialVersionUID = 2210802975893997405L;

  @Id
  @GeneratedValue(generator = "specificIdGenerator")
  @GenericGenerator(name = "specificIdGenerator", strategy = "com.sagag.services.tools.support.SpecificIdentityGenerator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "OFFER_ID")
  private Long offerId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "UMSART_ID")
  private String umsartId;

  @Column(name = "SHOP_ARTICLE_ID")
  private Long shopArticleId;

  @Column(name = "ARTICLE_NUMBER")
  private String articleNumber;

  @Column(name = "ARTICLE_DESCRIPTION")
  private String articleDescription;

  @Column(name = "VEHICLE_ID")
  private String vehicleId;

  @Column(name = "VEHICLE_DESCRIPTION")
  private String vehicleDescription;

  @Column(name = "CALCULATED")
  private Date calculated;

  @Column(name = "SEQUENCE")
  private Integer sequence;

  @Column(name = "CONTEXT")
  private String context;

  @Column(name = "QUANTITY")
  private Double quantity;

  @Column(name = "UOM_ISO")
  private String uomIso;

  @Column(name = "CURRENCY_ID")
  private Integer currencyId;

  @Column(name = "GROSS_PRICE")
  private Double grossPrice;

  @Column(name = "NET_PRICE")
  private Double netPrice;

  @Column(name = "TOTAL_GROSS_PRICE")
  private Double totalGrossPrice;

  @Column(name = "REMARK")
  private String remark;

  @Column(name = "ACTION_TYPE")
  private String actionType;

  @Column(name = "ACTION_VALUE")
  private Double actionValue;

  @Column(name = "DELIVERY_TYPE_ID")
  private Integer deliveryTypeId;

  @Column(name = "DELIVERY_DATE")
  private Date deliveryDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "MAKE_ID")
  private String makeId;

  @Column(name = "MODEL_ID")
  private String modelId;

  @Column(name = "VEHICLE_BOM_ID")
  private String vehicleBomId;

  @Column(name = "VEHICLE_BOM_DESCRIPTION")
  private String vehicleBomDescription;

  @Column(name = "ARTICLE_ENHANCED_DESCRIPTION")
  private String articleEnhancedDescription;

  @Column(name = "PRICED_UNIT")
  private String pricedUnit;

  @Column(name = "CATALOG_PATH")
  private String catalogPath;

  @Column(name = "VEHICLE_TYPE_CODE")
  private String vehicleTypeCode;

  @Column(name = "PRICED_QUANTITY")
  private Double pricedQuantity;

  @Column(name = "VERSION")
  private Integer version;

  @Column(name = "CONNECT_VEHICLE_ID")
  private String connectVehicleId;

  @Column(name = "SAGSYS_ID")
  private String sagsysId;

}
