package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "articleId",
  "stock",
  "status",
  "message"
})
public class ArticleStock implements Serializable {

  private static final long serialVersionUID = 3184135837940679155L;

  @JsonSerialize(using = ToStringSerializer.class)
  private String articleId;

  private Double stock;

  private String status;

  private String message;

  private String branchId;
  
  public ArticleStock(String articleId, Double stock) {
    setArticleId(articleId);
    setStock(stock);
  }

  @JsonIgnore
  public Double getStockAmount() {
    return Optional.ofNullable(stock).map(Function.identity()).orElse(NumberUtils.DOUBLE_ZERO);
  }
}
