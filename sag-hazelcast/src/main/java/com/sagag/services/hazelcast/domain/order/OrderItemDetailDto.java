package com.sagag.services.hazelcast.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.cart.CategoryDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDetailDto implements Serializable {

  private static final long serialVersionUID = 2517049236676063196L;

  private ArticleDocDto article;
  private VehicleDto vehicle;
  private CategoryDto category;
  private String itemDesc;

  private boolean recycle;
  private boolean depot;
  private boolean vin;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long recycleArticleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long depotArticleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long vocArticleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long vrgArticleId;

  private String additionalText;

  @JsonIgnore
  public String getVehicleInfo() {
    if (vehicle == null) {
      return StringUtils.EMPTY;
    }
    return vehicle.getVehicleInfo();
  }

  public static OrderItemDetailDto createOrderItemDetail(ShoppingCartItem item,
          Map<String, String> additionalTextDocMap) {

    final ArticleDocDto article = item.getArticle();
    // #6753 Add missing info for VIN package
    if (StringUtils.isEmpty(article.getArtnr())) {
      article.setArtnr(article.getArtid());
    }
    if (StringUtils.isEmpty(article.getArtnrDisplay())
        && Objects.nonNull(article.getArticle().getKeyword())) {
      article.setArtnrDisplay(article.getArticle().getKeyword());
    }
    // #1277: ArtID/SAGsysID
    final String note =
        Objects.isNull(article) || Objects.isNull(article.getArtid()) ? StringUtils.EMPTY
            : additionalTextDocMap.get(item.getCartKey());

    removeUnusedFields(item);

    return OrderItemDetailDto.builder()
            .category(item.getCategory())
            .article(article)
            .vehicle(item.getVehicle())
            .vin(item.isVin())
            .additionalText(note)
            .itemDesc(item.getItemDesc()).build();
  }

  private static void removeUnusedFields(ShoppingCartItem item) {

    final ArticleDocDto article = item.getArticle();
    article.setArticle(null);
    article.setGenArtTxtEng(null);
    article.setStock(null);
    article.setCriteria(null);
    article.setInfos(null);

    article.setIamNumbers(null);
    article.setPnrnPccs(null);
    article.setPnrnEANs(null);
    article.setOeNumbers(null);

    article.setQtyStandardCh(null);
    article.setQtyStandardAt(null);
    article.setQtyStandardBe(null);
    article.setQtyLowestAt(null);
    article.setQtyLowestBe(null);
    article.setQtyLowestCh(null);
    article.setQtyMultipleAt(null);
    article.setQtyMultipleCh(null);
    article.setQtyMultipleBe(null);

    final VehicleDto vehicle = item.getVehicle();
    final VehicleDto reducedVehicle = VehicleDto.builder()
        .id(vehicle.getId())
        .vehId(vehicle.getVehId())
        .vehicleFullName(vehicle.getVehicleFullName())
        .vehicleBrand(vehicle.getVehicleBrand())
        .vehicleModel(vehicle.getVehicleModel())
        .vehicleName(vehicle.getVehicleName())
        .vehiclePowerKw(vehicle.getVehiclePowerKw())
        .vehicleEngineCode(vehicle.getVehicleEngineCode())
        .idMake(vehicle.getIdMake())
        .idModel(vehicle.getIdModel())
        .idMotor(vehicle.getIdMotor())
        .build();
    item.setVehicle(reducedVehicle);
  }

}
