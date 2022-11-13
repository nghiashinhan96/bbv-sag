package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@JsonInclude
@Document(
      indexName = "ext_parts",
      type = "doc",
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder(
    {
      "prnrd",
      "prnr",
      "id_brand",
      "product_addon",
      "is_replacement_for",
      "has_replacement",
      "id_pim"
    })
public class ExternalPartDoc implements Serializable {

  private static final long serialVersionUID = -578751105172354589L;

  @Id
  @JsonProperty("prnrd")
  private String prnrd;

  @JsonProperty("prnr")
  private String prnr;

  @JsonProperty("id_brand")
  private String brandId;

  @JsonProperty("product_addon")
  private String productAddon;

  @JsonProperty("is_replacement_for")
  private String isReplacement;

  @JsonProperty("has_replacement")
  private String hasReplacement;

  @JsonProperty("id_pim")
  private String idPim;
}
